import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ListPageComponent } from '@app/features/movies/pages/list-page/list-page.component';
import { IMovie } from '@app/features/movies/response/movie';

@Component({
  selector: 'app-my-dialog-component',
  standalone: false,
  templateUrl: './my-dialog-component.component.html',
  styleUrl: './my-dialog-component.component.css',
})
export class MyDialogComponentComponent {

  readonly data: IMovie[] = inject<IMovie[]>(MAT_DIALOG_DATA);

  onClose() {
    console.log('Hello...');
  }

}
