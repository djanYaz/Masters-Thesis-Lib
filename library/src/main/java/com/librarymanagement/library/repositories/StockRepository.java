package com.librarymanagement.library.repositories;

import com.librarymanagement.library.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT s FROM Stock s WHERE s.book.id = :id")
    Stock getStockByBookId(Long id);
}
