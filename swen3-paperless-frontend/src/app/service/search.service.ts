import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {SearchPagingResponse} from '../dto/response/SearchPagingResponse';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  private http = inject(HttpClient);

  public searchDocuments(searchRequest: any): Observable<SearchPagingResponse> {
    return this.http.post<SearchPagingResponse>(`${environment.baseApiUrl}/search`, searchRequest);
  }
}
