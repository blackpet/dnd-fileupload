export type UUID = string

export interface FileResponse {
  id: UUID
  originFilename: string
}

export interface FileBoardResponse {
  id: UUID
  title: string
  files: Array<AttachFile>
}

export interface AttachFile extends File {
  id?: UUID
  originFilename: string
  contentType: string
  size: number
  attached: boolean
}

export interface CreateFileBoardRequest {
  title: string
  fileIds: Array<UUID>
}

export interface HTMLInputEvent extends Event {
  target: HTMLInputElement & EventTarget;
}
