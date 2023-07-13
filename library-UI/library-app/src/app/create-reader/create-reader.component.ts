import { Component, OnInit } from '@angular/core';
import {ReaderService} from "../reader.service"
import {Router} from "@angular/router";
@Component({
  selector: 'app-create-reader',
  templateUrl: './create-reader.component.html',
  styleUrls: ['./create-reader.component.css']
})
export class CreateReaderComponent implements OnInit {

  first_name:string;
  last_name:string;
  city:string;
  phone:number;
  email:string;
  msg: string = "";

  constructor(private readerService: ReaderService, private router: Router) { }

  ngOnInit(): void {
  }

  save() {
    return this.readerService.createReader(this.first_name, this.last_name, this.city, this.phone,this.email)
  }
  async onSubmit() {
    await this.save().toPromise();
    this.gotoList();
  }

  gotoList() {
    this.router.navigate(['readers']);
  }
}
