import { Component, OnInit } from '@angular/core';
import {BorrowBookService} from "../borrow-book.service";
import {BookService} from "../book.service";
import {Router} from "@angular/router";
import {BorrowedBook} from "../borrowbook";

@Component({
  selector: 'app-return-book',
  templateUrl: './return-book.component.html',
  styleUrls: ['./return-book.component.css']
})
export class ReturnBookComponent implements OnInit {

  borrowBooks: Array<BorrowedBook>;

  constructor(private borrowBookService: BorrowBookService, private router: Router) { }

  ngOnInit(): void {
    this.getAllBorrowedBooks();
  }

  getAllBorrowedBooks() {
    this.borrowBookService.getBorrowBookList().subscribe(
      (borrows: Array<BorrowedBook>) => {
        borrows.forEach(a=>{a.borrowDate=new Date(a.borrowDate).toLocaleString();a.returnDate=new Date(a.returnDate).toLocaleString();})
        this.borrowBooks = borrows;
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  returnSelectedBook(id: number) {
    if(window.confirm('Сигурни ли сте, че искате да върнете книгата?')) {
      this.borrowBookService.returnBook(id).subscribe(
        () => {
          this.getAllBorrowedBooks();
        },
        error => console.log(error));
    }
  }


  getColor(returnDate: string) {
    let today: Date = new Date();
    let rd: Date = new Date(returnDate);
    let timeInMilisec: number = rd. getTime() - today. getTime();
    let daysBetweenDates: number = Math. ceil(timeInMilisec / (1000 * 60 * 60 * 24));

    if(rd < today) {
      return '#ff4c3c';
    } else if(daysBetweenDates <= 5) {
      return 'orange';
    } else {
      return '#00d700';
    }
  }
}
