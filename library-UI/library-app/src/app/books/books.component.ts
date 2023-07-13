import { Component, OnInit } from '@angular/core';
import {BookService} from "../book.service";
import { Router } from '@angular/router';
import {Book} from "../book";
import {Message} from "@angular/compiler/src/i18n/i18n_ast";

const pageSize = 5;

@Component({
  selector: 'app-books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {

  books: Array<Book> = [];
  currentSelectedPage = 0;
  totalPages = 0;
  pageIndexes: Array<number> = [];

  // genre list
  genres: Array<string> = [];
  selectedGenre = '';

  // sorting
  asc: boolean = true;

  constructor(private bookService: BookService, private router: Router) {
  }

  ngOnInit(): void {
    this.reloadData();
  }

  reloadData(){
    this.getPage(1, '', true);
    this.getGenres();
  }

  sortNow(){
    this.getPage(1, this.selectedGenre, this.asc);
  }

  getPage(page: number, selectedGenre: string, asc: boolean){
    this.bookService.getPageableBooks(page, pageSize, selectedGenre,
      asc)
      .subscribe(
        data => {
          this.books = data.books;
          this.totalPages = data.totalPages;
          this.pageIndexes = Array(this.totalPages).fill(0).map((x, i) => i);
          this.currentSelectedPage = data.pageNumber;
        },
        (error) => {
          console.log(error);
        }
      );
  }

  getPaginationWithIndex(index: number) {
    this.getPage(index+1, this.selectedGenre, this.asc);
  }

  getBooksPagesWithGenreFiltering(optionValue: any) {
    if (optionValue !== 'Всички'){
      this.selectedGenre = optionValue;
    } else {
      this.selectedGenre = '';
    }

    this.getPage(1, this.selectedGenre, this.asc);
  }

  getGenres(){
    this.bookService.getListGenres()
      .subscribe(
        (genres: Array<string>) => {
          this.genres = genres;
        },
        (error: any) => {
          console.log(error);
        }
      );
  }

  active(index: number) {
    if (this.currentSelectedPage === index ){
      return {
        active: true
      };
    } else {
      return  {
        active: false
      }
    }
  }

  nextClick(){
    if (this.currentSelectedPage < this.totalPages - 1){
      this.getPage(++this.currentSelectedPage + 1 ,
        this.selectedGenre, this.asc);
    }
  }

  previousClick(){
    if (this.currentSelectedPage > 0){
      this.getPage(--this.currentSelectedPage + 1,
        this.selectedGenre, this.asc);
    }
  }

  deleteBook(id: number,currentNumbers:number){
    if(currentNumbers&&window.confirm('Сигурни ли сте, че искате да изтриете книгата?')) {
      this.bookService.deleteBookByID(id).subscribe(
        () => {
          this.reloadData();
        },
        error => console.log(error));
    }
  }

  goToNewBook(){
    this.router.navigate(['add']);
  }

  AddOneBook(id: number){
    this.bookService.AddOneBookById(id).subscribe(
      () => {
        this.reloadData();
      },
      error => console.log(error));
  }

}
