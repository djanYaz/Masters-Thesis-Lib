import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BooksComponent } from './books/books.component';
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import { CreateBookComponent } from './create-book/create-book.component';
import {ReadersComponent} from "./readers/readers.component";
import { CreateReaderComponent } from './create-reader/create-reader.component';
import { UpdateReaderComponent } from './update-reader/update-reader.component';
import { BorrowBookComponent } from './borrow-book/borrow-book.component';
import { ReturnBookComponent } from './return-book/return-book.component';

@NgModule({
  declarations: [
    AppComponent,
    BooksComponent,
    ReadersComponent,
    CreateBookComponent,
    CreateReaderComponent,
    UpdateReaderComponent,
    BorrowBookComponent,
    ReturnBookComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
