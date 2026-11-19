import {LabelResponse} from './label-response';

export interface DocumentResponse {
  uuid: string,
  name: string,
  labels: LabelResponse[],
  status: 'NEW' | 'CONTENT_CHANGED' | 'OCR_FINISHED' | 'GEN_AI_FINISHED',
  aiSummary?: string,
}
