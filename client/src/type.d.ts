export type UUID = string

export interface FileResponse {
  id: UUID
  originFilename: string
}

export interface AttachedFile extends File {
  id?: UUID
  attached?: boolean
}
