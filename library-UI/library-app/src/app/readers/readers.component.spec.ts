import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReadersComponent } from './readers.component';

describe('BooksComponent', () => {
  let component: ReadersComponent;
  let fixture: ComponentFixture<ReadersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReadersComponent ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReadersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
