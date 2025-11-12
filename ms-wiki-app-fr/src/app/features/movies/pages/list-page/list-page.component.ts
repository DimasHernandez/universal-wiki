import { Component, inject, Input, OnInit, Output } from '@angular/core';
import { MovieService } from '../../services/movie.service';
import { IInfoMovie } from '../../response/info-movie';
import { IErrorResponse } from '@core/interfaces/response/error.response';

@Component({
  selector: 'app-list-page',
  standalone: false,
  templateUrl: './list-page.component.html',
  styleUrl: './list-page.component.css'
})
export class ListPageComponent implements OnInit {

  public infoMovie: IInfoMovie = {
    page: 0,
    movies: [],
    total: 0,
    totalMovies: 0,
    totalPages: 0
  };

  private readonly movieService = inject(MovieService);

  ngOnInit(): void {
    this.getMovies();

  }

  getMovies(): void {
    this.movieService.getMovies(1, 2)
      .subscribe({
        next: (response: IInfoMovie) => {
          console.log('response API Movie: ', response);
          console.log('Movies: ', response.movies);
          this.infoMovie = {
            page: response.page,
            movies: response.movies,
            total: response.total,
            totalMovies: response.totalMovies,
            totalPages: response.totalPages,
          };
        },
        error: (error: IErrorResponse) => {
          console.log('error: ', error.error);
        }
      });
  }
}
