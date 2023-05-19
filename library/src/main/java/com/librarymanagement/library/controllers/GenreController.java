package com.librarymanagement.library.controllers;

import com.librarymanagement.library.entities.Genre;
import com.librarymanagement.library.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/app")
public class GenreController {
    @Autowired
    GenreRepository genreRepository;

    @GetMapping("/genres")
    public List<Genre> getListGenres() {
        try {
            return genreRepository.findAll();
        }catch (Exception e){
            System.out.println(e);
            return Arrays.asList();
        }
    }

    @PostMapping("/newgenre")
    public String addGenre(@RequestParam(value = "genreType") String genreType){
        Genre genre = genreRepository.getGenreByType(genreType.toLowerCase(Locale.ROOT));
        if(genre == null) {
            Genre newGenre = new Genre(genreType.toLowerCase());
            genreRepository.save(newGenre);
            return "Успешно добавен жанр!";
        } else {
            return "Този жанр вече съществува!";
        }
    }
}

