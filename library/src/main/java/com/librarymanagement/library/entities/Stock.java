package com.librarymanagement.library.entities;

import org.apache.tomcat.jni.Address;

import javax.persistence.*;

@Entity
@Table(name="stock")
public class Stock {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @Column (name="numbers")
    private Integer numbers;

    public Stock() {
    }

    public Stock(Book book, Integer numbers) {
        this.book = book;
        this.numbers = numbers;
    }

    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }
}
