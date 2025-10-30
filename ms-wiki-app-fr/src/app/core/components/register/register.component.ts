import { Component, inject } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  Validators,
} from '@angular/forms';
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
  private readonly authService = inject(AuthService);
  fb = inject(FormBuilder);

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
    roles: new FormControl(null, {
      validators: [Validators.required],
    }),
    isActivate: new FormControl(true),
  });

  onSubmit(): void {
    if (!this.registerForm.valid) {
      this.formDirty();
      return;
    }

    const { username, password, roles, isActivate } = this.registerForm.value;
    if (username && password && roles && isActivate) {
      const request: IRegisterRequest = {
        username,
        password,
        roles: [roles],
        active: isActivate,
      };
      console.log('request :>> ', request);

      this.authService.register(request).subscribe({
        next: (value) => {
          console.log('value :>> ', value);
        },
        error: (err) => {
          console.log('err :>> ', err);
        },
        complete: () => {
          console.log('completed');
        },
      });
      // ?? ||
    }
  }

  formDirty(): void {
    const controlsKey = Object.keys(this.registerForm.controls);
    for (const key of controlsKey) {
      this.registerForm.get(key)?.markAsDirty();
    }
  }

  checkValidPassword(): string {
    console.log('this.registerForm.value :>> ', this.registerForm.value);

    const { password, passwordConfirm } = this.registerForm.controls;
    if (passwordConfirm.pristine) {
      return '';
    }

    if (passwordConfirm.value && password.value === passwordConfirm.value) {
      return 'is-valid';
    }

    return 'is-invalid';
  }

  checkClassValid(field: string): string {
    const fieldControl = this.registerForm.get(field);

    if (!fieldControl?.pristine && fieldControl?.errors) {
      return 'is-invalid';
    }

    if (!fieldControl?.pristine) {
      return 'is-valid';
    }
    return '';
  }
}
