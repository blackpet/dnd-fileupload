import type {AttachFile, FileResponse} from '../../type';
import {api} from '../http/http-helper';

async function uploadFile(file: File): Promise<FileResponse> {
  if (!file) throw new Error(`file does not exists`)

  const formData = new FormData()
  formData.append('file', file)
  try {
    const res = await api.post('/files/upload', formData)
    return res.data
  } catch (err) {
    throw new Error(`error occur during upload file [${file.name}]`)
  }
}

export {
  uploadFile,
}
