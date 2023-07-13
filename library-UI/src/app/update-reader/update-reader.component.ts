import { Component, OnInit } from '@angular/core';
import {ReaderService} from "../reader.service"
import {ActivatedRoute,Router} from "@angular/router";
import {Reader} from "../reader";

@Component({
  selector: 'app-update-reader',
  templateUrl: './update-reader.component.html',
  styleUrls: ['./update-reader.component.css']
})
export class UpdateReaderComponent implements OnInit {
  id:number;
  reader: Reader;

  constructor(private readerService: ReaderService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.reader=new Reader();

    this.id=this.route.snapshot.params['id'];

    this.readerService.getReader(this.id)
      .subscribe(data =>{
        this.reader=data;
      },error=>console.log(error));
  }

  updateReader(){
    this.readerService.updateReader(this.id,this.reader)
      .subscribe(data =>{
        this.reader=<Reader>data;
        this.gotoList()
      },error =>console.log(error));
  }

  onSubmit() {
    this.updateReader();
  }

  gotoList() {
    this.router.navigate(['readers']);
  }
}
