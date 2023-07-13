import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Stock} from "./Stock";

@Injectable({
  providedIn: 'root'
})
export class BorrowBookService {

  private baseUrl = 'http://localhost:8080/springboot-crud-rest/borrows';
  constructor(private http: HttpClient ) { }

  getBorrowBookList(): Observable<any> {
    return this.http.get(`${this.baseUrl}` + `/all`)
  }

  borrowBook(reader_id: number, book_id: number, return_date: Date): Observable<any> {
    const params = new HttpParams()
      .set('reader_id', reader_id)
      .set('book_id', book_id)
      .set('return_date',return_date.toISOString?return_date.toISOString():new Date(return_date).toISOString())
    return this.http.post(`${this.baseUrl}` + `/givebook`,null, {params: params, responseType: "text"});
  }

  returnBook(id: number): Observable<Object> {
    return this.http.put(`${this.baseUrl}` + '/decrease/' + `${id}`, null);
  }

}
