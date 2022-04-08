<script setup lang="ts">
import type {MimeTypeString} from './lib/util/mime-type';
import {bytesToSize} from './lib/util/size-converter';
import DndFileUploader from './components/DndFileUploader.vue';
import {onMounted, ref, watch} from 'vue';
import {FileBoardResponse, AttachFile, UUID} from './type';
import {createFileBoard, getFileBoardList} from './lib/api/attachboard-api';
import FileIcon from './components/FileIcon.vue';

const title1 = ref<string>()
const files1 = ref<Array<AttachFile>>()
const fileBoardList = ref<Array<FileBoardResponse>>()

onMounted(() => {
  loadFileBoardList();
})

watch(files1, (value => console.log('watch', value)))

async function loadFileBoardList() {
  fileBoardList.value = await getFileBoardList()
}

async function save() {
  const request = {
    title: title1.value!,
    fileIds: files1.value!.map(f => f.id) as Array<UUID>
  }
  const id = await createFileBoard(request)
  console.log('new board created', id);

  // reload list
  loadFileBoardList();
}
</script>

<template>
  <h1>DnD File Uploader</h1>

  <section>
    <h3 class="section">File Board</h3>
    <div>
      <input type="text" class="board-title" v-model="title1" placeholder="첨부 제목을 입력해주세요.">
      <button @click="save" :disabled="!title1 || !files1?.length">Save</button>
    </div>
    <DndFileUploader class="dnd" v-model:files="files1" />
    <p>Any file uploader</p>

    <h3 class="section">File Board List</h3>
    <div v-if="!fileBoardList?.length">
      No File Board Contents...
    </div>
    <ul v-else>
      <li v-for="board in fileBoardList" :key="board.id" class="board-item">
        {{board.title}}
        <ul class="board-files">
          <li v-for="file in board.files" :key="file.id">
            <FileIcon :mime="(file.contentType as MimeTypeString)" /> [{{file.contentType}}] [{{file.id}}] <strong>{{name}}</strong> ({{bytesToSize(file.size)}})
          </li>
        </ul>
      </li>
    </ul>
  </section>

  <section class="separator">
    <h3 class="section">image file uploader (accept="image/*")</h3>
    <DndFileUploader class="dnd" accept="image/*" placeholder="이미지 파일을 여기에 Drag & Drop 해 주세요" />
    <p>image file uploader (accept="image/*")</p>
  </section>


</template>

<style>
li.board-item {
  border-bottom: 1px solid #6186c9;
  padding-top: 1rem;
}
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
.board-title {width: 40vw; margin-right: 1rem;}
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
