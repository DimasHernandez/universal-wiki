import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-pagination',
  standalone: false,
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent {

  @Input()
  length: number = 1;

  @Input()
  pageSize: number = 5;

  @Output()
  triggerPage = new EventEmitter<PageEvent>();

  onPageChange(event: PageEvent): void {
    this.triggerPage.emit(event);
  }

}
