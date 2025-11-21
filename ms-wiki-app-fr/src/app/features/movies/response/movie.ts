export interface IMovie {
    id: string;
    title: string;
    originalTitle: string;
    summary: string;
    urlImage: string;
    type: string;
    genres: string[];
    releaseDate: Date;
}