package com.librarymanagement.library.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.librarymanagement.library.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="book")
public class Book {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    Set<BorrowedBook> borrowedBooks;

    @OneToMany(mappedBy = "book")
    Set<OutOfStock> outOfStocks;

    public Book() {
    }

    public Book(String title, String author, Integer yearPublished) {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
    }

    public Book(String title, String author, Integer yearPublished, Genre genre, Stock stock) {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.genre = genre;
        this.stock = stock;
    }

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column (name="year_published")
    private Integer yearPublished;

    @ManyToOne
    @JoinColumn(name="genre_id", nullable=false)
    private Genre genre;

    @OneToOne(mappedBy = "book")
    private Stock stock;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
