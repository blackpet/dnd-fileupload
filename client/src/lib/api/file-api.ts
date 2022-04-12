import type {AttachFile, AttachFileResponse, UUID} from '../../type';
import {api} from '../http/http-helper';

async function uploadFile(file: File): Promise<AttachFileResponse> {
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

async function deleteFile(id: UUID | undefined): Promise<boolean> {
  if (!id) return false

  try {
    await api.delete(`/files/${id}`)
    return true
  } catch (e) {
    return false
  }
}

export {
  uploadFile,
  deleteFile,
}
