import { Component, OnInit } from '@angular/core';
import html2canvas from 'html2canvas';
import jsPDF from "jspdf";
import {Book} from "../book";
import {Reader} from "../reader";
import {BookService} from "../book.service";
import {Router} from "@angular/router";
import {ReaderService} from "../reader.service";
import {BorrowBookService} from "../borrow-book.service";

@Component({
  selector: 'app-borrow-book',
  templateUrl: './borrow-book.component.html',
  styleUrls: ['./borrow-book.component.css']
})
export class BorrowBookComponent implements OnInit {
  today = new Date().toLocaleString();
  public returnDate= new Date();
  books: Array<Book> = [];
  readers: Array<Reader> = [];
  selectedBookId: number;
  selectedReaderId: number;
  book: Book;
  reader: Reader;
  msg: string= "";
  phone: number;
  email: string;
  author: string;
  year: number;

  constructor(private bookService: BookService, private readerService: ReaderService,
              private borrowBookService: BorrowBookService, private router: Router) { }

  ngOnInit(): void {
    this.getBooks();
    this.getReaders();
  }

  getBooks() {
    this.bookService.getBookList()
      .subscribe(
        (books: Array<Book>) => {
          this.books = books;
        },
        (error: any) => {
          console.log(error);
        }
      );
  }

  getReaders() {
    this.readerService.getReaderList()
      .subscribe(
        (readers: Array<Reader>) => {
          this.readers = readers;
        },
        (error: any) => {
          console.log(error);
        }
      );
  }

  onChangeReader(){
    this.selectedReaderId = this.reader.id;
    this.phone = this.reader.phone;
    this.email = this.reader.email;
  }

  onChangeBook(){
    this.selectedBookId = this.book.id;
    this.author = this.book.author;
    this.year = this.book.yearPublished;
  }
  onSubmit() {
    this.borrowBook();
  }

  borrowBook() {
    this.msg="";
    this.borrowBookService.borrowBook(this.selectedReaderId, this.selectedBookId,this.returnDate)
      .subscribe(
        data => {
          this.msg = data;
        },
        (error: any) => {
          console.log(error);
        }
      );
  }

  public openPDF(): void {
    const DATA = document.getElementById('borrow-book');
    if (DATA) {
      html2canvas(DATA).then(canvas => {
        const fileWidth = 208;
        const fileHeight = canvas.height * fileWidth / canvas.width;

        const FILEURI = canvas.toDataURL('image/png');
        const PDF = new jsPDF('l', 'mm', 'a4');
        const position = 0;
        PDF.addImage(FILEURI, 'PNG', 50, 50, fileWidth, fileHeight);

        PDF.save('doc.pdf');
      });
    }
  }
  goToNewReader() {
    this.router.navigate(['addreader']);
  }

}
