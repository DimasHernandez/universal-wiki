import { ChangeDetectionStrategy, Component, inject, Input, OnInit, Output } from '@angular/core';
import { MovieService } from '../../services/movie.service';
import { IInfoMovie } from '../../response/info-movie';
import { IErrorResponse } from '@core/interfaces/response/error.response';
import { PageEvent } from '@angular/material/paginator';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ISearchForm } from '../../interfaces/search-form.interface';
import { debounceTime, delay, filter, tap } from 'rxjs';
import { MyDialogComponentComponent } from '@shared/components/my-dialog-component/my-dialog-component.component';
import { MatDialog } from '@angular/material/dialog';
import { IMovie } from '../../response/movie';

@Component({
  selector: 'app-list-page',
  standalone: false,
  templateUrl: './list-page.component.html',
  styleUrl: './list-page.component.css',
})
export class ListPageComponent implements OnInit {
  private readonly movieService = inject(MovieService);
  private fb = inject(FormBuilder);
  private dialog = inject(MatDialog);

  infoMovie: IInfoMovie = {
    page: 0,
    movies: [],
    total: 0,
    totalMovies: 0,
    totalPages: 0
  };

  infoMovieSearch: IInfoMovie = {
    page: 0,
    movies: [],
    total: 0,
    totalMovies: 0,
    totalPages: 0
  };

  searchForm = this.fb.group<ISearchForm>({
    search: new FormControl('', {
      validators: [Validators.minLength(1)]
    })
  });

  private moviesCache = new Map<string, IInfoMovie>();

  ngOnInit(): void {
    this.getMovies();
    this.searchForm.controls['search'].valueChanges
      .pipe(
        tap(value => { console.log('value in tap: ', value); }
        ),
        filter(value => !!value && value.length > 3),
        debounceTime(900)
      )
      .subscribe(
        value => {
          console.log('value change: ', value);
          this.getMoviesSearch(1, 20, value!);
        });
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

  getMoviesSearch(page: number = 1, pageSize: number = 20, query: string): void {
    this.movieService.getMoviesSearch(page, pageSize, query)
      .subscribe({
        next: (response: IInfoMovie) => {
          console.log('Calling API Movie Search!');
          console.log('response :>> ', response);
          this.infoMovieSearch = response;
          this.openDialog();
        },
        error: (error: IErrorResponse) => {
          console.log('error: ', error.error);
        }
      })
  }

  pageEvent(event: PageEvent): void {
    const { pageIndex, pageSize } = event;
    console.log('pageIndex: ', pageIndex, 'pageSize: ', pageSize);

    this.getMovies(pageIndex + 1, pageSize);
  }

  onSubmit() {
    const { search } = this.searchForm.value;

    if (!search || this.searchForm.invalid) {
      console.log('form invalid!!');
      return;
    }

    console.log('search: ', search);

    // this.getMoviesSearch(1, 20, search);

    this.resetForm();
  }

  private resetForm() {
    this.searchForm.reset();
  }

  openDialog(): void {
    const data: IMovie[] = this.infoMovieSearch.movies;
    console.log('data_movies: ', data)

    this.dialog.open(MyDialogComponentComponent, {
      width: '600px',
      height: '550px',
      data
    });
  }
}
