import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { IRegisterRequest } from '@core/interfaces/requests/register.request';
import { catchError, Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);

  register(request: IRegisterRequest): Observable<string> {
    const customHeader = new HttpHeaders({
      'X-MS-TYPE': 'auth',
    });

    return this.http
      .post<string>(`${environment.BASE_URL}/signup`, request, {
        headers: customHeader,
      })
      .pipe(catchError((err) => {
        console.log('err :>> ', err);
        return '';
      }));
  }
}
