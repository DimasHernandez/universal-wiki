import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '@app/material/material.module';
import { SnackMessageComponent } from './components/snack-message/snack-message.component';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material/snack-bar';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '@core/services/auth.service';
import { PaginationComponent } from './components/pagination/pagination.component';

@NgModule({
  declarations: [SnackMessageComponent, PaginationComponent],
  imports: [CommonModule, MaterialModule, HttpClientModule],
  exports: [MaterialModule, SnackMessageComponent, PaginationComponent],
  providers: [
    {
      provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
      useValue: {
        duration: 5000,
        horizontalPosition: 'end',
        verticalPosition: 'bottom',
      },
    },
    AuthService
  ],
})
export class SharedModule { }
