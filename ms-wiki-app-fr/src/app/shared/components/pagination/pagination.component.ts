import { Component, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { IInfoMovie } from '@app/features/movies/response/info-movie';

@Component({
  selector: 'app-pagination',
  standalone: false,
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css'
})
export class PaginationComponent implements OnChanges {
  ngOnChanges(changes: SimpleChanges): void {
    console.log('info movie from component son: ', this.infoMovie);
    // throw new Error('Method not implemented.');
  }

  @Input()
  public infoMovie: IInfoMovie = {
    page: 0,
    movies: [],
    total: 0,
    totalMovies: 0,
    totalPages: 0
  };




}
