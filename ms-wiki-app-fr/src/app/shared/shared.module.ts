import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '@app/material/material.module';
import { SnackBarService } from './services/snack-bar.service';

@NgModule({
  declarations: [],
  imports: [CommonModule, MaterialModule],
  providers: [SnackBarService],
  exports: [MaterialModule],
})
export class SharedModule {}
