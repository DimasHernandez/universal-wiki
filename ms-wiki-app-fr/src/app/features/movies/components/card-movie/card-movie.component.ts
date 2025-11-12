import { Component, Input, OnInit } from '@angular/core';
import { IMovie } from '../../response/movie';

@Component({
  selector: 'card-movie',
  standalone: false,
  templateUrl: './card-movie.component.html',
  styleUrl: './card-movie.component.css'
})
export class CardMovieComponent implements OnInit {

  ngOnInit(): void {
    if (!this.movie || this.movie.genres.length === 0) throw new Error('Movie property is required.');
  }

  @Input()
  public movie!: IMovie;

}
