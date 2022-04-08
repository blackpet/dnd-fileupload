export type UUID = string

export interface FileResponse {
  id: UUID
  originFilename: string
}

export interface AttachedFile extends File {
  id?: UUID
  attached?: boolean
}

export interface CreateAttachBoardRequest {
  title: string
  fileIds: Array<UUID>
}

export interface HTMLInputEvent extends Event {
  target: HTMLInputElement & EventTarget;
}
