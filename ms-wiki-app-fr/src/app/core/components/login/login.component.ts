import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormControl, Validators, } from '@angular/forms';
import { Router } from '@angular/router';

import { MatSnackBar } from '@angular/material/snack-bar';
import { DataMessage, ILoginForm } from '@core/interfaces';
import { ILoginRequest } from '@core/interfaces/requests/login.request';
import { ILoginResponse } from '@core/interfaces/response/login-response';
import { AuthService } from '@core/services/auth.service';
import { SnackMessageComponent } from '@shared/components/snack-message/snack-message.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
  standalone: false
})
export class LoginComponent {
  hide = signal(true);

  private readonly authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private readonly _snackBar = inject(MatSnackBar);

  loginForm = this.fb.group<ILoginForm>({
    username: new FormControl('', { validators: [Validators.required, Validators.minLength(5)] }),
    password: new FormControl('', { validators: [Validators.required, Validators.minLength(5)] }),
  });

  onSubmit(): void {

    if (this.loginForm.invalid) {
      console.log('form invalid');
      this.loginForm.markAllAsTouched();
      return;
    }

    const { username, password } = this.loginForm.value;
    console.log('username: ', username, ' password: ', password);

    if (username && password) {
      const request: ILoginRequest = {
        username: username,
        password: password,
      }

      this.authService.login(request).subscribe({
        next: (response: ILoginResponse) => {
          localStorage.setItem('access_token', response.accessToken);
          localStorage.setItem('refresh_token', response.refreshToken);

          const data: DataMessage = {
            message: 'Login successfully!',
            typeMessage: 'success'
          }
          this._snackBar.openFromComponent(SnackMessageComponent, {
            data
          });
          this.router.navigateByUrl('/movies/list');
        },
        error: (error) => {
          const data: DataMessage = {
            message: 'Login failed',
            typeMessage: 'error'
          };
          this._snackBar.openFromComponent(SnackMessageComponent, {
            data
          });
        },
        complete: () => {
          console.log('completed');
        }
      });
    }
  }

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
    event.preventDefault();
  }
}
