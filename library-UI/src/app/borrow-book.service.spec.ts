import { TestBed } from '@angular/core/testing';

import { BorrowBookService } from './borrow-book.service';

describe('BorrowBookService', () => {
  let service: BorrowBookService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BorrowBookService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
