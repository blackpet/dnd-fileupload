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
  tobeDeleted?: boolean // 삭제 예정 파일 여부
  temped?: boolean // 임시 첨부 여부
}

export interface CreateFileBoardRequest {
  title: string
  fileIds: Array<UUID>
}

export interface ModifyFileBoardRequest {
  id: UUID
  title: string
  attachments: Array<{
    fileId: UUID
    tobeDeleted?: boolean
    temped?: boolean
  }>
}

export interface HTMLInputEvent extends Event {
  target: HTMLInputElement & EventTarget;
}
