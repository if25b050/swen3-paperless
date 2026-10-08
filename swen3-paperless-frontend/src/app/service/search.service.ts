import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {DocumentSearchRequest} from '../dto/request/DocumentSearchRequest';
import {DocumentSearchResponse} from '../dto/response/document-search-response';
import {environment} from '../environment';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  private http = inject(HttpClient);

  public searchDocuments(searchRequest: DocumentSearchRequest): Observable<DocumentSearchResponse> {
    return this.http.post<DocumentSearchResponse>(`${environment.baseApiUrl}/search`, searchRequest);
  }
}
