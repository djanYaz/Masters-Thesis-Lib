package com.librarymanagement.library.controllers;

import com.librarymanagement.library.entities.*;
import com.librarymanagement.library.repositories.BookRepository;
import com.librarymanagement.library.repositories.GenreRepository;
import com.librarymanagement.library.repositories.StockRepository;
import com.librarymanagement.library.services.emailService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/app")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    EmailService emailService;

    @GetMapping(value="/books")
    public List<Book> getBooks(){
        return bookRepository.findAll();
    }

    @PutMapping(value = "/decrease/{id}")
    public ResponseEntity<Stock> deleteBook(@PathVariable(value = "id") Long book_id){
        Stock stock = stockRepository.getStockByBookId(book_id);
        Integer stockNum = stock.getNumbers();
        if(stockNum > 1) {
            stock.setNumbers(stockNum - 1);
        } else {
            stock.setNumbers(0);
        }
        Stock updatedStock = stockRepository.save(stock);

        return ResponseEntity.ok(updatedStock);
    }

    @PutMapping(value = "/addOne/{id}")
    public ResponseEntity<Stock> AddOneBook(@PathVariable(value = "id") Long book_id) throws Exception {
        Stock stock = stockRepository.getStockByBookId(book_id);
        Integer stockNum = stock.getNumbers();
        stock.setNumbers(stockNum + 1);
        Stock updatedStock = stockRepository.save(stock);
        emailService.InformOfAvailabilityAllRequesters(book_id);
        return ResponseEntity.ok(updatedStock);
    }

    @PostMapping("/newbook")
    public Book createBook(@RequestParam(required = false, name = "title") String title,
                           @RequestParam(required = false, name = "author") String author,
                           @RequestParam(required = false, name = "year") Integer year,
                           @RequestParam(required = false, name = "genre") String genre) {

        Long id = bookRepository.getDuplicateBook(title, author, year);
        Book newBook = new Book();
        newBook.setGenre(genreRepository.getGenreByType(genre));
        newBook.setAuthor(author);
        newBook.setYearPublished(year);
        newBook.setTitle(title);
        if(id == null) {
            stockRepository.save(new Stock(newBook, 1));
            return bookRepository.save(newBook);
        } else {
            Stock stock = stockRepository.getStockByBookId(id);
            stock.setNumbers(stock.getNumbers() + 1);
            stockRepository.save(stock);
            return newBook;
        }
    }


    @PutMapping("/updateBook/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable(value = "id") Long book_id,
                                           @RequestBody Book bookData) throws Exception {
        Book book = bookRepository.findById(book_id)
                .orElseThrow(() -> new Exception("Book not found for this id :: " + book_id));

        book.setTitle(bookData.getTitle());
        book.setAuthor(bookData.getAuthor());
        book.setYearPublished(bookData.getYearPublished());
        book.setGenre(genreRepository.getGenreByType(bookData.getGenre().getGenreType()));
        final Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/books/pageable")
    public Response retrieveBooks(
            @Param(value = "genre") String genre,
            @Param(value = "page") int page,
            @Param(value = "size") int size,
            @Param(value = "asc") boolean asc){

        Page<Book> books = null;

        // не се ползва жанра
        if(genre.equals("")) {
            // с години във възходящ
            if(asc) {
                Pageable requestedPage = PageRequest.of(page-1, size, Sort.by("yearPublished"));
                books  = bookRepository.getBookView(requestedPage);
            }
            // с години и в низходящ
            else {
                Pageable requestedPage = PageRequest.of(page-1, size,
                        Sort.by("yearPublished").descending());
                books  = bookRepository.getBookView(requestedPage);
            }

            // ползва се филтър за жанр
        } else {
            // с години във възходящ
            if(asc) {
                Pageable requestedPage = PageRequest.of(page-1, size, Sort.by("yearPublished"));
                books  = bookRepository.findAllByGenre(genre, requestedPage);
            }
            // с години и в низходящ
            else {
                Pageable requestedPage = PageRequest.of(page-1, size,
                        Sort.by("yearPublished").descending());
                books  = bookRepository.findAllByGenre(genre, requestedPage);
            }
        }

        return new Response(books.getContent(), books.getTotalPages(), books.getNumber(), books.getSize());
    }

    @GetMapping("/book/genres")
    public List<String> getListGenres() {
        try {
            return bookRepository.findDistinctGenreTypes();
        }catch (Exception e){
            System.out.println(e);
            return Arrays.asList();
        }
    }

}
