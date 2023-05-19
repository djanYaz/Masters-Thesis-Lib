package com.librarymanagement.library.repositories;

import com.librarymanagement.library.entities.Book;
import com.librarymanagement.library.entities.Reader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ReaderRepository extends JpaRepository<Reader,Long> {


    @Query(value = "SELECT id FROM reader WHERE first_name = :first_name AND last_name = :last_name AND phone = :phone", nativeQuery = true)
    Long getDuplicateReader(String first_name, String last_name,String phone);

    @Query(value = "SELECT r FROM Reader r WHERE r.city=:city")
    Page<Reader> getReaderByCity (String city,Pageable pageable);

    @Query("SELECT DISTINCT r.city FROM Reader r")
    List<String> findDistinctCity();

}
