import {LabelResponse} from './label-response';

export interface DocumentResponse {
  uuid: string,
  name: string,
  labels: LabelResponse[];
}
