package com.librarymanagement.library.repositories;

import com.librarymanagement.library.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT g FROM Genre g WHERE lower(g.genreType) = :genre")
    Genre getGenreByType(String genre);

}
