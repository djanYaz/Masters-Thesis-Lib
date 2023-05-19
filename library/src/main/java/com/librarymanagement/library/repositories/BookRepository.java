package com.librarymanagement.library.repositories;

import com.librarymanagement.library.entities.Book;
import com.librarymanagement.library.entities.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Transactional
    @Query(value = "SELECT b FROM Book b LEFT JOIN Stock s ON b.id = s.book.id")
    Page<Book> getBookView(Pageable requestedPage);

    @Query(value = "SELECT id FROM book WHERE title = :title AND author = :author AND year_published = :year_published", nativeQuery = true)
    Long getDuplicateBook(String title, String author, Integer year_published);

    @Transactional
    @Query(value = "SELECT b FROM Book b WHERE b.genre.genreType= :genre")
    Page<Book> findAllByGenre(String genre, Pageable requestedPage);

    @Query("SELECT DISTINCT b.genre.genreType FROM Book b")
    List<String> findDistinctGenreTypes();

    @Query("SELECT b.stock.numbers FROM Book b WHERE b.id = :book_id")
    Long getBookStockById(Long book_id);

}
