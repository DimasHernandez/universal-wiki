import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { IInfoMovie } from '../response/info-movie';
import { CustomHeader } from '@core/interfaces/enums';
import { catchError, throwError } from 'rxjs';
import { IErrorResponse } from '@core/interfaces/response/error.response';

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  private readonly http = inject(HttpClient);

  public getMovies(page: number, pageSize: number) {
    return this.http
      .get<IInfoMovie>(`${environment.BASE_URL_MOVIES}/movies?page=${page}&pageSize=${pageSize}`,
        {
          headers: this.getCustomHeader()
        })
      .pipe(
        catchError((err: IErrorResponse) => {
          return throwError(() => err.error)
        })
      );
  }

  private getCustomHeader(): HttpHeaders {
    let headers = new HttpHeaders();
    headers = headers.set(CustomHeader.X_MS_TYPE, environment.X_MS_TYPE_AUTH_MOVIE_HEADER_VALUE);
    headers = headers.set(CustomHeader.AUTHORIZATION, `Bearer ${localStorage.getItem('refresh_token')!}`);
    return headers;
  }
}
