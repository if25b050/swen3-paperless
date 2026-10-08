import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DocumentResponse} from '../dto/response/document-response';
import {DocumentUpdateRequest} from '../dto/request/DocumentUpdateRequest';
import {environment} from '../environment';

@Injectable({
  providedIn: 'root',
})
export class DocumentService {
  private http = inject(HttpClient);


  public getDocuments(): Observable<DocumentResponse[]> {
    return this.http.get<DocumentResponse[]>(`${environment.baseApiUrl}/documents`, {})
  }

  public getDocument(uuid: string): Observable<DocumentResponse> {
    return this.http.get<DocumentResponse>(`${environment.baseApiUrl}/documents/${uuid}`, {})
  }

  public createDocument(document: File): Observable<DocumentResponse> {
    let formData = new FormData();
    formData.append('file', document);
    return this.http.post<DocumentResponse>(`${environment.baseApiUrl}/documents`, formData)
  }

  public updateDocument(uuid: string, document: DocumentUpdateRequest): Observable<DocumentResponse> {
    return this.http.put<DocumentResponse>(`${environment.baseApiUrl}/documents/${uuid}`, document, {})
  }

  public getDocumentFile(uuid: string): Observable<ArrayBuffer> {
    return this.http.get(`${environment.baseApiUrl}/documents/${uuid}/file`, {responseType: 'arraybuffer'});
  }

  public updateDocumentFile(uuid: string, document: File): Observable<void> {
    let formData = new FormData();
    formData.append('file', document);
    return this.http.post<void>(`${environment.baseApiUrl}/documents/${uuid}/file`, formData)
  }

  public deleteDocument(uuid: string): Observable<void> {
    return this.http.delete<void>(`${environment.baseApiUrl}/documents/${uuid}`, {})
  }
}
