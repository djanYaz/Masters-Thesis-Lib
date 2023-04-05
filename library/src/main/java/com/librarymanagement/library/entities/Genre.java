package com.librarymanagement.library.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="genre")
public class Genre {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(name="genre_type")
    private String genreType;

    @OneToMany(mappedBy="genre")
    private Set<Book> books = new HashSet<>();

    public Genre() {
    }

    public Genre(String genreType) {
        this.genreType = genreType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenreType() {
        return genreType;
    }

    public void setGenreType(String genreType) {
        this.genreType = genreType;
    }
}
