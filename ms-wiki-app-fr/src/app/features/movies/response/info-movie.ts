import { IMovie } from "./movie";

export interface IInfoMovie {
    page: number;
    movies: IMovie[];
    totalPages: number;
    totalMovies: number;
    total: number;
}