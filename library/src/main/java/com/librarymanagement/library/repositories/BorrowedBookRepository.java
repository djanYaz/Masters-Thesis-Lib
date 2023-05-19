package com.librarymanagement.library.repositories;

import com.librarymanagement.library.entities.BorrowedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    @Query("SELECT COUNT(b.reader.id) FROM BorrowedBook b WHERE b.reader.id = :reader_id")
    Long getReaderCountBooks(Long reader_id);

    @Query("SELECT b FROM BorrowedBook b ORDER BY b.returnDate ASC")
    List<BorrowedBook> getOrderedBorrowedBooks();

    @Query("SELECT b FROM BorrowedBook b WHERE b.returnDate < :instant")
    List<BorrowedBook> getBookBorrowingsWithReturnDateEarlierThan(Instant instant);

    @Query("SELECT b FROM BorrowedBook b WHERE b.reader.id = :readerId")
    List<BorrowedBook> getBookBorrowingsOfReader(Long readerId);
}
