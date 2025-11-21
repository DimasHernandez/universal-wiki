import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { IRegisterRequest } from '@core/interfaces/requests/register.request';
import { catchError, Observable, of, throwError } from 'rxjs';
import { environment } from '@environments/environment';
import { IErrorResponse } from '@core/interfaces/response/error.response';
import { ILoginRequest } from '@core/interfaces/requests/login.request';
import { ILoginResponse } from '@core/interfaces/response/login-response';
import { Router } from '@angular/router';
import { CustomHeader } from '@core/interfaces/enums';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);

  private route = inject(Router);

  register(request: IRegisterRequest): Observable<string> {
    const customHeader = this.getCustomHeaders();

    return this.http
      .post<string>(`${environment.BASE_URL}/signup`, request, {
        headers: customHeader,
      })
      .pipe(catchError((err: IErrorResponse) => {
        // console.log('err :>> ', err);
        return throwError(() => err.error);
      }));
  }

  login(request: ILoginRequest): Observable<ILoginResponse> {
    return this.http.
      post<ILoginResponse>(`${environment.BASE_URL}/login`, request, {
        headers: this.getCustomHeaders(),
      })
      .pipe(catchError((err: IErrorResponse) => {
        // console.log('err :>> ', err);
        return throwError(() => err.error);
      }));
  }

  private getCustomHeaders(): HttpHeaders {
    let headers: HttpHeaders = new HttpHeaders();
    headers = headers.set(CustomHeader.X_MS_TYPE, environment.X_MS_TYPE_AUTH_HEADER_VALUE);
    return headers;
  }

  logout(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    this.route.navigateByUrl('/auth/login');
  }
}
