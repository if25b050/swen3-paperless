import {DocumentResponse} from './response/document-response';

export interface SearchResult {
  document: DocumentResponse;
  type: 'filename' | 'content';
  snippet?: string;
}
