import {FileExcel, FileMusic, FilePdf, FileText, FileWord, Movie, Picture, Powerpoint, Zip} from '@icon-park/vue-next';

export type MimeTypeString = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' |
  'application/vnd.openxmlformats-officedocument.presentationml.presentation' |
  'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' |
  'application/haansofthwp' |
  'application/pdf' |
  'audio/mpeg' |
  'video/mp4' |
  'application/zip' |
  'image/jpeg' |
  'image/gif' |
  'image/png'

export type MimeType = Record<MimeTypeString, { type: string, icon: any }>

export const mimeTypeIconMap: MimeType = {
  'application/vnd.openxmlformats-officedocument.wordprocessingml.document': {
    type: 'docx',
    icon: FileWord,
  },
  'application/vnd.openxmlformats-officedocument.presentationml.presentation': {
    type: 'pptx',
    icon: Powerpoint,
  },
  'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': {
    type: 'xlsx',
    icon: FileExcel,
  },
  'application/haansofthwp': {
    type: 'hwp',
    icon: FileText,
  },
  'application/pdf': {
    type: 'pdf',
    icon: FilePdf,
  },
  'audio/mpeg': {
    type: 'mp3',
    icon: FileMusic,
  },
  'video/mp4': {
    type: 'mp4',
    icon: Movie,
  },
  'application/zip': {
    type: 'zip',
    icon: Zip,
  },
  'image/jpeg': {
    type: 'jpg',
    icon: Picture,
  },
  'image/gif': {
    type: 'gif',
    icon: Picture,
  },
  'image/png': {
    type: 'png',
    icon: Picture,
  }
}

