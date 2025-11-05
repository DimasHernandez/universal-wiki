import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { IRegisterRequest } from '@core/interfaces/requests/register.request';
import { of, throwError } from 'rxjs';
import { IErrorResponse } from '@core/interfaces/response/error.response';

fdescribe('AuthService', () => {
  let authService: AuthService;
  let http: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [AuthService],
    });
    http = TestBed.inject(HttpClient);
    authService = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  describe('register', () => {
    it('should call register and return string', (done: DoneFn) => {
      const responseMock = "User signed up successfully";
      const request: IRegisterRequest = {
        username: 'testuser',
        password: 'Test@1234',
        roles: ['ROLE_USER'],
        active: true,
      }

      const httpSpy = spyOn(http, 'post').and.returnValue(of(responseMock));

      authService.register(request).subscribe({
        next: (response) => {
          expect(response).toEqual(responseMock);
          done();
        },
        error: done.fail,
      });

      expect(httpSpy.calls.count()).toBe(1);
    });

    it('should return an error when service return 500', (done: DoneFn) => {
      const request: IRegisterRequest = {
        username: '',
        password: '',
        roles: [''],
        active: false,
      }
      const errorResponse: IErrorResponse = {
        timestamp: "2025-11-05T16:08:36.210+00:00",
        path: "/api/v1/signup",
        status: 500,
        error: "Internal Server Error",
        requestId: "74cbdbc0-3"
      };

      const httpSpy = spyOn(http, 'post').and.returnValue(throwError(() => (errorResponse)));

      authService.register(request).subscribe({
        next: () => done.fail(''),
        error: (error) => {
          console.log('error: ', error);

          expect(error.status).toEqual(500);
          expect(error.error).toEqual('Internal Server Error');
          expect(error.requestId).toEqual('74cbdbc0-3');
          done();
        },
      });
      expect(httpSpy.calls.count()).toBe(1);
    });
  });
});
