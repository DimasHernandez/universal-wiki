import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { MatSnackBar } from '@angular/material/snack-bar';
import { DataMessage, IRegisterForm } from '@core/interfaces';
import { IRegisterRequest } from '@core/interfaces/requests/register.request';
import { AuthService } from '@core/services/auth.service';
import { SnackMessageComponent } from '@shared/components/snack-message/snack-message.component';

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
  private readonly _snackBar = inject(MatSnackBar);

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
      validators: [Validators.required],
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
        next: () => {
          const data: DataMessage = {
            message: 'Register successfully',
            typeMessage: 'success'
          }
          this._snackBar.openFromComponent(SnackMessageComponent, {
            data
          });
          this.router.navigateByUrl('/auth/login');
        },
        error: (err) => {
          const data: DataMessage = {
            message: 'Something went wrong, try again',
            typeMessage: 'error',
          };
          this._snackBar.openFromComponent(SnackMessageComponent, {
            duration: 100000,
            horizontalPosition: 'end',
            verticalPosition: 'bottom',
            data,
          });
        },
        complete: () => {
          console.log('completed');
        },
      });
    }
  }

  formDirty(): void {
    const controlsKey = Object.keys(this.registerForm.controls);
    for (const key of controlsKey) {
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
}
