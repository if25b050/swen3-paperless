import {PdfItem} from './pdf-item';

export interface SearchResult {
  id: string;
  type: 'filename' | 'content';
  fileItem: PdfItem;
  snippet?: string;
}
