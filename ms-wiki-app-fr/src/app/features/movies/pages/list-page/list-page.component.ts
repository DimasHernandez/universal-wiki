import { Component, inject, Input, OnInit, Output } from '@angular/core';
import { MovieService } from '../../services/movie.service';
import { IInfoMovie } from '../../response/info-movie';
import { IErrorResponse } from '@core/interfaces/response/error.response';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-list-page',
  standalone: false,
  templateUrl: './list-page.component.html',
  styleUrl: './list-page.component.css'
})
export class ListPageComponent implements OnInit {
  private readonly movieService = inject(MovieService);

  infoMovie: IInfoMovie = {
    page: 0,
    movies: [],
    total: 0,
    totalMovies: 0,
    totalPages: 0
  };

  private moviesCache = new Map<string, IInfoMovie>();

  ngOnInit(): void {
    this.getMovies();
  }

  getMovies(page: number = 1, pageSize: number = 5): void {
    const key = `${pageSize}-${page}`;

    if (this.moviesCache.has(key)) {
      this.infoMovie = this.moviesCache.get(key)!
      console.log(`Extract InfoMovie from cache! - Key ${key} - Map size: ${this.moviesCache.size}`,);
      return;
    }

    this.movieService.getMovies(page, pageSize)
      .subscribe({
        next: (response: IInfoMovie) => {
          console.log('Calling API Movie!');
          this.infoMovie = response;
          this.moviesCache.set(key, response);
        },
        error: (error: IErrorResponse) => {
          console.log('error: ', error.error);
        }
      });
  }

  pageEvent(event: PageEvent): void {
    const { pageIndex, pageSize } = event;
    this.getMovies(pageIndex + 1, pageSize);
  }
}
