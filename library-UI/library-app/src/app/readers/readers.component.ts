import { Component, OnInit } from '@angular/core';
import {ReaderService} from "../reader.service";
import { Router } from '@angular/router';
import {Reader} from "../reader";
import {Message} from "@angular/compiler/src/i18n/i18n_ast";
import {Observable} from "rxjs";

const pageSize = 5;

@Component({
  selector: 'app-readers',
  templateUrl: './readers.component.html',
  styleUrls: ['./readers.component.css']
})

export class ReadersComponent implements OnInit {

  hasBooks: boolean;
  readers: Array<Reader> = [];
  id: number;
  currentSelectedPage = 0;
  totalPages = 0;
  pageIndexes: Array<number> = [];
  currentId: number;
  deletemsg: string="";

  city = '';
  cities: Array<string> = [];

  constructor(private readerService: ReaderService, private router: Router) {
  }

  ngOnInit(): void {
    this.reloadData();
  }

  reloadData() {
    this.getPage(1, '');
    this.getCities();
  }

  sortNow() {
    if (!this.city) {
      return;
    }
    this.getPage(1, this.city);
  }

  getPage(page: number, city: string) {
    this.readerService.getPageableReaders(page, pageSize, city
    )
      .subscribe(
        data => {
          this.readers = data.readers;
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
    this.getPage(index + 1, this.city);
  }

  getReadersPagesWithCityFiltering(optionValue: any) {
    if (optionValue !== 'Всички') {
      this.city = optionValue;
    } else {
      this.city = '';
    }

    this.getPage(1, this.city);
  }


  active(index: number) {
    if (this.currentSelectedPage === index) {
      return {
        active: true
      };
    } else {
      return {
        active: false
      }
    }
  }

  nextClick() {
    if (this.currentSelectedPage < this.totalPages - 1) {
      this.getPage(++this.currentSelectedPage + 1,
        this.city);
    }
  }

  previousClick() {
    if (this.currentSelectedPage > 0) {
      this.getPage(--this.currentSelectedPage + 1,
        this.city);
    }
  }

  getCities() {
    this.readerService.getListCities()
      .subscribe(
        (cities: Array<string>) => {
          this.cities = cities;
        },
        (error: any) => {
          console.log(error);
        }
      );
  }

  goToNewReader() {
    this.router.navigate(['addreader']);
  }

  gotoUpdateReader(id: number) {
    this.router.navigate(['updatereader', id]);

  }

  deleteReader(id: number) {
    this.readerService.deleteReaderById(id).subscribe(
      (data) => {
        this.reloadData();
        this.deletemsg = data;
      },
      error => console.log(error));
  }

  checkButton(id: number) {
    this.readerService.howManyBooks(id)
      .subscribe(data => {
        this.hasBooks = data;
      });
  }
}

