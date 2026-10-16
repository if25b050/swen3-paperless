import {DocumentResponse} from './document-response';
import {SearchPagingResponse} from './search-paging-response';

export interface DocumentSearchResponse {
  list: DocumentResponse[];
  paging: SearchPagingResponse;
}
