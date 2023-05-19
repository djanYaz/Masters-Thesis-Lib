package com.librarymanagement.library.repositories;

import com.librarymanagement.library.entities.OutOfStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OutOfStockRepository extends JpaRepository<OutOfStock, Long> {
    @Query("SELECT COUNT(o.reader.id) FROM OutOfStock o WHERE o.reader.id = :reader_id")
    Long hasRequestedBooks(Long reader_id);

    @Query("SELECT o FROM OutOfStock o WHERE o.reader.id = :id")
    List<OutOfStock> getAllBorrowingAttemptsAReaderHasMade(Long id);
    @Query("SELECT o FROM OutOfStock o WHERE o.book.id = :id")
    List<OutOfStock> getAllBorrowingAttemptsForSpecificBook(Long id);
}
