import { LabelModel } from '../../api/models/label-model';

export interface DocumentResponse {
  uuid: string,
  name: string,
  labels: LabelModel[];
}
