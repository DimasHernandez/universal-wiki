import { Component, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
  standalone: false
})
export class RegisterComponent {

  fb = inject(FormBuilder);

  registerForm = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(2)]],
    password: ['', [Validators.required, Validators.minLength(5)]],
    roles: ['', [Validators.required]],
    isActivate: [true, [Validators.required]],
  });

  onSubmit() {
    console.log('Yeah! submit ...');

    const {
      username,
      password,
      roles,
      isActivate,
    } = this.registerForm.value;

  const isValid = this.registerForm.valid;
  console.log(isValid);

  console.log(username, password, roles, isActivate);
  }
}
