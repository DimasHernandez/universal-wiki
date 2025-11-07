import { Component, inject, signal } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { IRegisterForm } from '@core/interfaces';
import { IRegisterRequest } from '@core/interfaces/requests/register.request';
import { AuthService } from '@core/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  standalone: false,
})
export class RegisterComponent {
  hide = signal(true);

  private readonly authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  registerForm = this.fb.group<IRegisterForm>({
    username: new FormControl('', {
      validators: [Validators.required, Validators.minLength(5)],
    }),
    password: new FormControl('', {
      validators: [Validators.required, Validators.minLength(5)],
    }),
    passwordConfirm: new FormControl('', {
      validators: [Validators.required, Validators.minLength(5)],
    }),
    role: new FormControl(null, {
      validators: [Validators.required],
    }),
    isActivate: new FormControl(true, {
      validators: [Validators.required]
    }),
  });

  onSubmit(): void {
    if (!this.registerForm.valid || !this.registerForm.value.isActivate) {
      this.formDirty();
      return;
    }

    const { username, password, role, isActivate } = this.registerForm.value;
    if (username && password && role && isActivate) {
      const request: IRegisterRequest = {
        username,
        password,
        roles: [role],
        active: isActivate,
      };
      console.log('request :>> ', request);

      this.authService.register(request).subscribe({
        next: (value) => {
          console.log('value :>> ', value);
          return this.router.navigateByUrl('/auth/login');
        },
        // error: (err) => {
        //   console.log('err :>> ', err);
        // },
        // complete: () => {
        //   console.log('completed');
        // },
      });
      // ?? ||
    }
    this.clearForm();
  }

  formDirty(): void {
    const controlsKey = Object.keys(this.registerForm.controls);
    for (const key of controlsKey) {
      // this.registerForm.get(key)?.markAsDirty();
      this.registerForm.get(key)?.markAsTouched();
    }
  }

  checkValidPassword(): boolean {
    const { password, passwordConfirm } = this.registerForm.controls;
    if (passwordConfirm.pristine) {
      return true;
    }

    if (passwordConfirm.value && password.value === passwordConfirm.value) {
      return true;
    }

    return false;
  }

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
    event.preventDefault();
  }

  clearForm(): void {
    this.registerForm.reset({
      username: '',
      password: '',
      passwordConfirm: '',
      role: null,
      isActivate: true,
    });

    const controlsKey = Object.keys(this.registerForm.controls);
    console.log('controlsKey: ', controlsKey);

    for (const key of controlsKey) {
      this.registerForm.get(key)?.setErrors(null);
      this.registerForm.get(key)?.markAsPristine();
      this.registerForm.get(key)?.markAsUntouched();
      this.registerForm.get(key)?.updateValueAndValidity();
    }
  }
}
