<script setup lang="ts">
import {HandleX} from '@icon-park/vue-next';
import {ref} from 'vue';
import {bytesToSize} from '../lib/util/size-converter';
import FileIcon from './FileIcon.vue';
import {uploadFile} from '../lib/api/file-api';
import type {AttachedFile, FileResponse, HTMLInputEvent} from '../type';

interface DndFileUploaderProps {
  accept?: string
  placeholder?: string
}
enum UPLOAD_STATE {
  READY = 0,
  UPLOADING = 1
}
const props = defineProps<DndFileUploaderProps>()
const emits = defineEmits<{
  (e: 'update:files', value: Array<AttachedFile>): void
}>()

const container = ref<HTMLDivElement>()
const attachedFiles = ref<Map<string, AttachedFile>>(new Map())
const isHighlight = ref<boolean>(false)
const currentState = ref<UPLOAD_STATE>()

const randomId = Math.random().toString(36).substring(2, 18);

const highlight = () => isHighlight.value = true
const unhighlight = () => isHighlight.value = false


function dropFile(e: DragEvent) {
  unhighlight()

  const data = e.dataTransfer
  let fileList = data?.files

  if (!fileList || fileList?.length === 0) return

  // accept validation
  const files = Array.from(fileList).filter(f => validateAccept(f.type))

  addFiles(files);
}

function handleInputFileChange(e: HTMLInputEvent) {
  const fileList = e.target?.files;

  if (fileList) {
    addFiles(Array.from(fileList));
  }
}

function validateAccept(type: string) {
  if (!props.accept || props.accept === '' || props.accept === '*/*') return true

  const acceptWithoutStar = props.accept.replace('/*', '')
  return type.startsWith(acceptWithoutStar)
}

async function addFiles(files: Array<File>) {
  // state to uploading...
  currentState.value = UPLOAD_STATE.UPLOADING

  const promises: Array<Promise<FileResponse>> = []
  files.forEach(f => {
    // upload to server!
    promises.push(uploadFile(f));
    attachedFiles.value.set(f.name, f)
  })

  // Promise 는 일괄 처리 하자!
  const responses = await Promise.allSettled(promises)

  responses.forEach((res: PromiseSettledResult<FileResponse>) => {
    // 업로드 된 파일이면 list 에 추가하자!
    if (res.status === 'fulfilled') {
      const file = attachedFiles.value.get(res.value.originFilename);
      if (!file) return
      file.id = res.value.id
      file.attached = true
    }
  })

  // 업로드 실패한 파일들은 제껴~!
  attachedFiles.value.forEach(f => {
    if (!f.attached) {
      attachedFiles.value.delete(f.name)
    }
  })

  // initialize state
  currentState.value = UPLOAD_STATE.READY

  // emit
  emits('update:files', [...attachedFiles.value.values()])
}

function removeFile(name: string) {
  attachedFiles.value.delete(name)

  // emit
  emits('update:files', [...attachedFiles.value.values()])
}

</script>

<template>
  <div ref="container" class="dnd-area"
       :class="{highlight: isHighlight}"
       @dragenter.prevent.stop="highlight"
       @dragover.prevent.stop="highlight"
       @dragleave.prevent.stop="unhighlight"
       @drop.prevent.stop="dropFile"
  >
    <form class="dnd-form">
      <p class="dnd-placeholder">{{placeholder || 'Drag & Drop Files here'}}</p>
      <input type="file" multiple :id="`fileInput-${randomId}`" :accept="props.accept" @change="handleInputFileChange">
      <label class="button" :for="`fileInput-${randomId}`">Select some files</label>
    </form>

    <ul class="file-list">
      <li v-for="[name, file] of attachedFiles" :key="name" class="file-list-item">
        <template v-if="file.attached">
          <FileIcon :mime="file.type" /> [{{file.type}}] [{{file.id}}] <strong>{{name}}</strong> ({{bytesToSize(file.size)}})
          <HandleX size="18" fill="#ff4f4f" @click="removeFile(name)" />
        </template>
      </li>
    </ul>

    <div v-if="currentState === UPLOAD_STATE.UPLOADING" class="loading-block">
      <div class="dim"></div>
      <p>Uploading...</p>
    </div>
  </div>
</template>

<style scoped>
.file-list-item {
  display: flex;
  align-items: center;
  gap: 10px;
}
.loading-block {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  font-size: 1.5rem;
  font-weight: bold;
  color: black;
  display: flex;
  justify-content: center;
  align-items: center;
}
.loading-block .dim {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #ccc;
  opacity: .4;
}
.dnd-area {
  border: dotted #ffb7b7 3px;
  min-height: 5rem;
  padding: 1rem;
  position: relative;
}
.dnd-area.highlight {
  border-color: #ff4f4f;
}
.dnd-form {
  display: flex;
  gap: .5rem;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.dnd-placeholder {
  color: #868686;
}
input[type=file] {
  display: none;
}
label.button {
  border: 1px solid #c4c4c4;
  border-radius: .3rem;
  background-color: #d9d9d9;
  padding: .5rem 1rem;
  cursor: pointer;
}
</style>
