import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { ReactiveFormsModule } from '@angular/forms';
import { CoreRoutingModule } from './core-routing.module';
import { SecurityPageComponent } from './pages/security-page/security-page.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from './services/auth.service';
import { MaterialModule } from '@app/material/material.module';


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
    MaterialModule
  ]
})
export class CoreModule { }
