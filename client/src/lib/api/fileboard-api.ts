import {FileBoardResponse, AttachFile, CreateFileBoardRequest, UUID, ModifyFileBoardRequest} from '../../type';
import {api} from '../http/http-helper';

async function createFileBoard(request: CreateFileBoardRequest): Promise<UUID> {
  const res = await api.post('/fileBoards', request)
  return res.data;
}

async function modifyFileBoard(request: ModifyFileBoardRequest): Promise<boolean> {
  const res = await api.put('/fileBoards', request)
  return res.data;
}

async function getFileBoardList(): Promise<Array<FileBoardResponse>> {
  const res = await api.get('/fileBoards')
  return res.data;
}

async function getFileBoard(id: UUID): Promise<FileBoardResponse> {
  const res = await api.get(`/fileBoards/${id}`)
  return res.data;
}

export {
  createFileBoard,
  modifyFileBoard,
  getFileBoardList,
  getFileBoard,
}
