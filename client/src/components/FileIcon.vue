<script setup lang="ts">
import {FileQuestion} from '@icon-park/vue-next'
import type {MimeTypeString} from '../lib/util/mime-type';
import {mimeTypeIconMap} from '../lib/util/mime-type';


interface FileIconProps {
  mime: string
  color?: string
  size?: number
}

const {mime, color, size} = withDefaults(defineProps<FileIconProps>(), {
  color: '#333',
  size: 24
})

function getIconComponent(mime: MimeTypeString) {
  if (mime in mimeTypeIconMap) {
    return mimeTypeIconMap[mime].icon
  } else {
    return FileQuestion
  }
}


</script>

<template>
  <component :is="getIconComponent(mime)" :size="size" :fill="color" />
</template>

<style scoped>

</style>
