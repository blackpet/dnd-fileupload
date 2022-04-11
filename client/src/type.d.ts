export type UUID = string

export interface AttachFileResponse {
  id: UUID
  name: string
  contentType?: string
  size?: number
  attached?: boolean
}

export interface FileBoardAttachmentResponse {
  attachmentId: UUID
  fileId: UUID
  name: string
  contentType?: string
  size?: number
  attached?: boolean
}

export interface FileBoardResponse {
  id: UUID
  title: string
  attachments: Array<FileBoardAttachmentResponse>
}

export interface AttachFile extends File {
  id?: UUID
  name: string
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
