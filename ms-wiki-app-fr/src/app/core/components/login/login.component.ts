import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormControl, Validators, } from '@angular/forms';
import { Router } from '@angular/router';
import { ILoginForm } from '@core/interfaces';
import { ILoginRequest } from '@core/interfaces/requests/login.request';
import { ILoginResponse } from '@core/interfaces/response/login-response';
import { AuthService } from '@core/services/auth.service';

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
          console.log('response :>> ', response);
        },
        // error: (error) => {
        //   console.log('error :>> ', error);
        // },
        // complete: () => {
        //   console.log('completed');
        // }
      });
    }
    this.clearForm();
  }

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
    event.preventDefault();
  }

  clearForm(): void {
    this.loginForm.reset({
      username: '',
      password: '',
    });

    const controlsKey = Object.keys(this.loginForm.controls);
    console.log('controlsKey: ', controlsKey);

    for (const key of controlsKey) {
      this.loginForm.get(key)?.setErrors(null);
      this.loginForm.get(key)?.markAsPristine();
      this.loginForm.get(key)?.markAsUntouched();
      this.loginForm.get(key)?.updateValueAndValidity();
    }
  }

}
