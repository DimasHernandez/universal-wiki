import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { ReactiveFormsModule } from '@angular/forms';
import { CoreRoutingModule } from './core-routing.module';
import { SecurityPageComponent } from './pages/security-page/security-page.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';


@NgModule({
  declarations: [
    SecurityPageComponent,
    LoginComponent,
    RegisterComponent,
  ],
  imports: [
    CommonModule,
    CoreRoutingModule,
    ReactiveFormsModule,
  ]
})
export class CoreModule { }
