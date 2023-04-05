package com.librarymanagement.library.entities;

import java.util.List;

public class ResponseReader {

        private List<Reader> readers;
        private int totalPages;
        private int pageNumber;
        private int pageSize;

    public ResponseReader(List<Reader> readers, int totalPages, int pageNumber, int pageSize) {
        this.readers = readers;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public void setReaders(List<Reader> readers) {
        this.readers = readers;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
