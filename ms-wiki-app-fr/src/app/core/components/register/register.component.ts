import { Component, inject, signal } from '@angular/core';
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
  hide = signal(true);

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
    isActivate: new FormControl(true, {
      validators: [Validators.requiredTrue]
    }),
  });

  onSubmit(): void {
    console.log('Button yeah!');

    if (!this.registerForm.valid || !this.registerForm.value.isActivate) {
      console.log('form invalid');

      this.formDirty();
      return;
    }

    console.log('form valid: ', this.registerForm.valid);
    console.log('form valid: ', this.registerForm.value);

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

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }
}
