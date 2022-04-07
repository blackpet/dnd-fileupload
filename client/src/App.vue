<script setup lang="ts">
import DndFileUploader from './components/DndFileUploader.vue';
import {ref, watch} from 'vue';
import {AttachedFile} from './type';

const title1 = ref<string>()
const files1 = ref<Array<AttachedFile>>()

watch(files1, (value => console.log('watch', value)))
</script>

<template>
  <h1>DnD File Uploader</h1>

  <h3 class="section">Attach Board</h3>
  <div>
    <input type="text" v-model="title1" placeholder="첨부 제목을 입력해주세요.">
    <button>Save</button>
  </div>
  <DndFileUploader class="dnd" v-model:files="files1" />
  <p>Any file uploader</p>

  <h3 class="section">image file uploader (accept="image/*")</h3>
  <DndFileUploader class="dnd" accept="image/*" placeholder="이미지 파일을 여기에 Drag & Drop 해 주세요" />
  <p>image file uploader (accept="image/*")</p>


</template>

<style>
.dnd {
  margin: 0 1rem;
}
input {height: 2rem; padding: 5px;}
h3.section {margin-top: 2rem;}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

</style>
