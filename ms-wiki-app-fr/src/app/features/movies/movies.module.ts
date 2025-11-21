import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { MoviesRoutingModule } from './movies-routing.module';
import { LayoutPageComponent } from './pages/layout-page/layout-page.component';
import { ListPageComponent } from './pages/list-page/list-page.component';
import { MovieService } from './services/movie.service';
import { CardMovieComponent } from './components/card-movie/card-movie.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    LayoutPageComponent,
    ListPageComponent,
    CardMovieComponent
  ],
  imports: [
    CommonModule,
    MoviesRoutingModule,
    SharedModule,
    ReactiveFormsModule
  ],
  providers: [
    MovieService
  ]
})
export class MoviesModule { }
