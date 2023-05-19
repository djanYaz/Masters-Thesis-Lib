package com.librarymanagement.library.controllers;

import com.librarymanagement.library.entities.*;
import com.librarymanagement.library.repositories.*;
import com.librarymanagement.library.services.emailService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/borrows")
public class BorrowedBookController {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    BorrowedBookRepository borrowedBookRepository;

    @Autowired
    ReaderRepository readerRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    OutOfStockRepository outOfStockRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    TaskExecutionRepository taskExecutionRepository;

    @GetMapping("all")
    public List<BorrowedBook> getAllBorrows(){
        return borrowedBookRepository.getOrderedBorrowedBooks();
    }

    @PostMapping("givebook")
    public String GiveBook(@RequestParam(required = true, name = "reader_id") Long reader_id,
                           @RequestParam(required = true, name = "book_id") Long book_id,
                           @RequestParam(required = true, name="return_date") String return_date
    ) throws Exception {
        Instant today = Instant.now();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant returnDate = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(return_date));
        Reader reader = readerRepository.findById(reader_id).orElseThrow(() -> new Exception("Reader not found for this id :: " + reader_id));
        Book book = bookRepository.findById(book_id).orElseThrow(() -> new Exception("Book not found for this id :: " + book_id));

        Integer bookStock = book.getStock().getNumbers();
        int readerGottenBooks = reader.getBorrowedBooks().size();
        if(readerGottenBooks < 5 && bookStock != 0){
            BorrowedBook borrowedBook = new BorrowedBook(book, reader, today, returnDate);
            borrowedBookRepository.save(borrowedBook);
            Stock stock = book.getStock();
            stock.setNumbers(stock.getNumbers()-1);
            stockRepository.save(stock);
            emailService.InformUserOfBorrowingSuccess(reader.getEmail(),book.getTitle(),borrowedBook.getReturnDate());
            return "Успешна заявка!";
        } else if(bookStock == 0) {
            OutOfStock outOfStock = new OutOfStock(book, reader, today);
            outOfStockRepository.save(outOfStock);
            return "Книгата " + book.getTitle() + " не е налична";
        } else if(readerGottenBooks == 5 || readerGottenBooks > 5) {
            return "Вече сте взели допустимото количество книги";
        }
        return "Не е успешно!";
    }

    @PutMapping(value = "/decrease/{id}")
    public void returnBook(@PathVariable(value = "id") Long id) throws Exception {
        BorrowedBook forDeletion = borrowedBookRepository.getById(id);
        Long bookId= forDeletion.getBook().getId();
        Stock stock = stockRepository.getStockByBookId(bookId);
        stock.setNumbers(stock.getNumbers() + 1);
        stockRepository.save(stock);
        var te =taskExecutionRepository.getAllTaskExecutionsForBorrowedBook(id);
        if(te!=null){
            taskExecutionRepository.deleteAll(te);
        }
        borrowedBookRepository.deleteById(id);
        emailService.InformOfAvailabilityAllRequesters(bookId);
    }
}

