import { Component, Inject, inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';
import { DataMessage } from '@core/interfaces';

@Component({
  selector: 'app-snack-message',
  standalone: false,
  templateUrl: './snack-message.component.html',
  styleUrl: './snack-message.component.css'
})
export class SnackMessageComponent {
    snackBarRef = inject(MatSnackBarRef);

    constructor(@Inject(MAT_SNACK_BAR_DATA) public data: DataMessage){}
}
