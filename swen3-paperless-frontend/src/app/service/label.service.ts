import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {LabelResponse} from '../dto/response/LabelResponse';
import {DocumentResponse} from '../dto/response/DocumentResponse';

@Injectable({
  providedIn: 'root',
})
export class LabelService {
  private http = inject(HttpClient);

  public getLabels(): Observable<LabelResponse[]> {
    return this.http.get<LabelResponse[]>(`${environment.baseApiUrl}/labels`, {})
  }

  public getLabel(uuid: string): Observable<LabelResponse> {
    return this.http.get<LabelResponse>(`${environment.baseApiUrl}/labels/${uuid}`, {})
  }

  public getDocumentsWithLabel(uuid: string): Observable<DocumentResponse[]> {
    return this.http.get<DocumentResponse[]>(`${environment.baseApiUrl}/labels/${uuid}/documents`, {})
  }


  public createLabel(label: string): Observable<LabelResponse> {
    return this.http.post<LabelResponse>(`${environment.baseApiUrl}/labels`, label, {})
  }

  public updateLabel(uuid: string, label: string): Observable<LabelResponse> {
    return this.http.put<LabelResponse>(`${environment.baseApiUrl}/labels/${uuid}`, label, {})

  }

  public deleteLabel(uuid: string): Observable<void> {
    return this.http.delete<void>(`${environment.baseApiUrl}/labels/${uuid}`, {})
  }
}
