import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, Observable, retry, throwError} from "rxjs";
import {Genre} from "./genre";

@Injectable({
  providedIn: 'root'
})
export class GenreService {
  private baseUrl = 'http://localhost:8080/springboot-crud-rest/app';
  constructor(private http: HttpClient ) { }

  getListGenres(): Observable<Array<Genre>>{
    return this.http.get<Array<Genre>>(`${this.baseUrl}` + `/genres`)
      .pipe(
        retry(3),
        catchError(this.handleError)
      );
  }

  addNewGenreType(genreType: string): Observable<any> {
    const params = new HttpParams().set('genreType', genreType);
    return this.http.post(`${this.baseUrl}` + `/newgenre`, null, {params: params, responseType: "text"});
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(
      'Something bad happened; please try again later.');
  }
}
