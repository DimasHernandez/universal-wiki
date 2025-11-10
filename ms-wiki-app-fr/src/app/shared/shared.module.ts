import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '@app/material/material.module';
import { SnackMessageComponent } from './components/snack-message/snack-message.component';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material/snack-bar';

@NgModule({
  declarations: [SnackMessageComponent],
  imports: [CommonModule, MaterialModule],
  exports: [MaterialModule, SnackMessageComponent],
  providers: [
    {
      provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
      useValue: {
        duration: 5000,
        horizontalPosition: 'end',
        verticalPosition: 'bottom',
      },
    },
  ],
})
export class SharedModule {}
