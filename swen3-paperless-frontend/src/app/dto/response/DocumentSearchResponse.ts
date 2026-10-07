import { DocumentResponse } from './DocumentResponse';
import { SearchPagingResponse } from '../../api/models/search-paging-response';

export interface DocumentSearchResponse {
  list: DocumentResponse[];
  paging: SearchPagingResponse;
}
