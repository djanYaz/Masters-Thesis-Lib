import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, generate, Observable, retry, throwError} from "rxjs";
import {Message} from "@angular/compiler/src/i18n/i18n_ast";
import {Stock} from "./Stock";

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private baseUrl = 'http://localhost:8080/springboot-crud-rest/app';
  constructor(private http: HttpClient ) { }

  getBookList(): Observable<any> {
    return this.http.get(`${this.baseUrl}` + `/books`)
  }

  getPageableBooks(pageNumber: number,
                   pageSize: number, genre: string,
                   asc: boolean): Observable<any> {

    let params = new HttpParams();
    params = params.append('page', pageNumber.toString());
    params = params.append('size', pageSize.toString());
    params = params.append('genre', genre.toString());
    params = params.append('asc', asc.toString());

    return this.http.get<any>(`${this.baseUrl}` + `/books/pageable`, { params })
      .pipe(
        retry(3),
        catchError(this.handleError)
      );
  }

  getListGenres(): Observable<Array<string>>{
    return this.http.get<Array<string>>(`${this.baseUrl}` + `/book/genres`)
      .pipe(
        retry(3),
        catchError(this.handleError)
      );
  }

  deleteBookByID(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}` + '/decrease/' + `${id}`, { responseType: Stock });
  }

  AddOneBookById(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}` + '/addOne/' + `${id}`, { responseType: Stock });
  }

  createBook(title: string, author: string, year: number, genre: string): Observable<Object> {
    const params = new HttpParams()
      .set('title', title)
      .set('author', author)
      .set('year', year)
      .set('genre', genre);
    return this.http.post(`${this.baseUrl}` + `/newbook`,null, {params: params});
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
