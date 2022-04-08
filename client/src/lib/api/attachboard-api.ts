import {FileBoardResponse, AttachFile, CreateFileBoardRequest, UUID} from '../../type';
import {api} from '../http/http-helper';

async function createFileBoard(request: CreateFileBoardRequest): Promise<UUID> {
  const res = await api.post('/fileBoards', request)
  return res.data;
}

async function getFileBoardList(): Promise<Array<FileBoardResponse>> {
  const res = await api.get('/fileBoards')
  return res.data;
}

export {
  createFileBoard,
  getFileBoardList,
}
