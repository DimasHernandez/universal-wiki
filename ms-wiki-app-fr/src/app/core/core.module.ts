import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { AuthService } from './services/auth.service';
import { CoreRoutingModule } from './core-routing.module';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { SecurityPageComponent } from './pages/security-page/security-page.component';
import { SharedModule } from '@shared/shared.module';


@NgModule({
  declarations: [
    SecurityPageComponent,
    LoginComponent,
    RegisterComponent,
  ],
  providers:[AuthService],
  imports: [
    CommonModule,
    CoreRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    SharedModule
  ]
})
export class CoreModule { }
