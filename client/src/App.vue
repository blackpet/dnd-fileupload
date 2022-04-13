<script setup lang="ts">
import type {MimeTypeString} from './lib/util/mime-type';
import {bytesToSize} from './lib/util/size-converter';
import DndFileUploader from './components/DndFileUploader.vue';
import {onMounted, reactive, ref, watch} from 'vue';
import {FileBoardResponse, AttachFile, UUID, ModifyFileBoardRequest} from './type';
import {createFileBoard, getFileBoard, getFileBoardList, modifyFileBoard} from './lib/api/fileboard-api';
import FileIcon from './components/FileIcon.vue';

const title1 = ref<string>()
const files1 = ref<Array<AttachFile>>([])
const fileBoardList = ref<Array<FileBoardResponse>>()
const apiPath = import.meta.env.VITE_API_PATH;
const board = ref<FileBoardResponse>()

const files2 = ref<Array<AttachFile>>([])

onMounted(() => {
  loadFileBoardList();
})

watch(files1, (value => console.log('watch', value)))

async function loadFileBoardList() {
  fileBoardList.value = await getFileBoardList()
}


async function save() {
  if (!board.value?.id) {
    await create();
  } else {
    await modify();
  }

  // reload list
  loadFileBoardList();

  // init form
  title1.value = ''
  files1.value = []
  board.value = undefined
}

async function view(boardId: UUID) {
  board.value = await getFileBoard(boardId)

  // init view
  title1.value = board.value?.title
  // convert FileBoardAttachmentResponse to AttachFile
  files1.value = board.value?.attachments?.map(({fileId, attachmentId, ...rest}) => ({...rest, id: fileId})) as Array<AttachFile>
  console.log('view title, files', title1.value, files1.value)
}

async function create() {
  const request = {
    title: title1.value!,
    fileIds: files1.value!.map(f => f.id) as Array<UUID>
  };
  const id = await createFileBoard(request)
  console.log('new board created', id);
}

async function modify() {
  const request = {
    id: board.value!.id,
    title: title1.value!,
    attachments: files1.value.map(({id, tobeDeleted, temped}) => ({fileId: id, tobeDeleted, temped}))
  } as ModifyFileBoardRequest

  const modified = await modifyFileBoard(request)
}
</script>

<template>
  <h1>DnD File Uploader</h1>

  <section>
    <h3 class="section">File Board</h3>
    <div>
      <input type="text" class="board-title-input" v-model="title1" placeholder="첨부 제목을 입력해주세요.">
      <button @click="save" :disabled="!title1 || !files1?.length">Save</button>
    </div>
    <DndFileUploader class="dnd" v-model="files1" />
    <p>Any file uploader</p>

    <h3 class="section">File Board List</h3>
    <div v-if="!fileBoardList?.length">
      No File Board Contents...
    </div>
    <ul v-else>
      <li v-for="board in fileBoardList" :key="board.id" class="board-item">
        <div class="board-title" @click="view(board.id)">{{ board.title }}</div>
        <ul class="board-files">
          <li v-for="att in board.attachments" :key="att.id">
            <a :href="`${apiPath}/files/download/${att.fileId}`" :download="att.name">
              <FileIcon :mime="(att.contentType as MimeTypeString)" /> [{{att.contentType}}] [{{att.fileId}}] <strong>{{att.name}}</strong> ({{bytesToSize(att.size)}})
            </a>
          </li>
        </ul>
      </li>
    </ul>
  </section>

  <section class="separator">
    <h3 class="section">image file uploader (accept="image/*")</h3>
    <DndFileUploader class="dnd" v-model="files2" accept="image/*" placeholder="이미지 파일을 여기에 Drag & Drop 해 주세요" />
    <p>image file uploader (accept="image/*")</p>
  </section>


</template>

<style>
li.board-item {
  border-bottom: 1px solid #6186c9;
  padding-top: 1rem;
}
.board-title {cursor: pointer;}
.board-files {
  background-color: #e9e9e9;
}
hr {
  margin: 2rem 0;
  border: none;
  height: 1rem;
  background-color: #0033ff;
}
.dnd {
  margin: 0 1rem;
}
.board-title-input {width: 40vw; margin-right: 1rem;}
input {height: 2rem; padding: 5px;}
section, h3.section {margin-top: 2rem;}
section.separator {border-top: 3px solid #649243;}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

</style>
