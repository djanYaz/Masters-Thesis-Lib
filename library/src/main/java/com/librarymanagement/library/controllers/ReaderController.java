package com.librarymanagement.library.controllers;

import com.librarymanagement.library.entities.*;
import com.librarymanagement.library.repositories.BorrowedBookRepository;
import com.librarymanagement.library.repositories.OutOfStockRepository;
import com.librarymanagement.library.repositories.ReaderRepository;
import com.librarymanagement.library.repositories.TaskExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping("/customer")
public class ReaderController {

    @Autowired
    ReaderRepository readerRepository;

    @Autowired
    BorrowedBookRepository borrowedBookRepository;

    @Autowired
    OutOfStockRepository outOfStockRepository;
    @Autowired
    TaskExecutionRepository taskExecutionRepository;

    @GetMapping(value = "/readers")
    public List<Reader> getReaders(){return readerRepository.findAll();}

    @GetMapping(value="/readers/{id}")
    public ResponseEntity<Reader> getReaderById(@PathVariable(value = "id") Long id)
            throws Exception {
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new Exception("Ненамерен читател със следното ID :: " + id));
        return ResponseEntity.ok().body(reader);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReader(@PathVariable (value = "id") Long readerId){
        Long numberofBorrows=borrowedBookRepository.getReaderCountBooks(readerId);
        if(numberofBorrows > 0) {
            return ResponseEntity.ok("Записът не може да бъде изтрит. Читателят има книги за връщане!");
        }
        if(!readerRepository.existsById(readerId)){
            return ResponseEntity.ok("Няма такъв читател!");
        }
        List<OutOfStock> allOutOfStocks = outOfStockRepository.getAllBorrowingAttemptsAReaderHasMade(readerId);
        if(!allOutOfStocks.isEmpty()) {
            outOfStockRepository.deleteAll(allOutOfStocks);
        }
        readerRepository.deleteById(readerId);
        return ResponseEntity.ok("Успешно изтрит!");

    }


    @PostMapping("/newreader")
    public ResponseEntity<?>  createReader(@RequestParam(required = false) String first_name,
                                           @RequestParam(required = false) String last_name,
                                           @RequestParam(required = false) String city,
                                           @RequestParam(required = false) String phone,
                                           @RequestParam(required = false) String email){

        Long id=readerRepository.getDuplicateReader(first_name,last_name,phone);

        Reader newReader= new Reader();
        newReader.setFirst_name(first_name);
        newReader.setLast_name(last_name);
        newReader.setCity(city);
        newReader.setPhone(phone);
        newReader.setEmail(email);

        if(id==null){
            newReader =readerRepository.save(newReader);
            return ResponseEntity.ok(newReader);
        }else {
            return ResponseEntity.ok("Читателя вече съществува.");
        }

    }
    @PutMapping("/updatereader/{id}")
    public ResponseEntity<Reader> updateReader(@PathVariable(value = "id") Long reader_id,
                                               @RequestBody Reader readerData) throws Exception {
        Reader reader = readerRepository.findById(reader_id)
                .orElseThrow(() -> new Exception("Reader not found for this id :: " + reader_id));

        reader.setFirst_name(readerData.getFirst_name());
        reader.setLast_name(readerData.getLast_name());
        reader.setCity(readerData.getCity());
        reader.setPhone(readerData.getPhone());
        reader.setEmail(readerData.getEmail());

        final Reader updatedReader = readerRepository.save(reader);
        return ResponseEntity.ok(updatedReader);

    }
    @GetMapping("/readers/pageable")
    public ResponseReader retrieveEmployee(
            @Param(value = "page") int page,
            @Param(value = "size") int size,
            @Param(value = "city") String city)
    {

        Page<Reader> readers = null;
        Pageable pageable =PageRequest.of(page-1,size);
        if(city.equals("")) {
            readers=readerRepository.findAll(pageable);
        }
        else {
            readers= readerRepository.getReaderByCity(city,pageable);
        }

        return new ResponseReader(readers.getContent(), readers.getTotalPages(), readers.getNumber(), readers.getSize());
    }

    @GetMapping(value = "/cities")
    public List<String> getCities() {
        try {
            return readerRepository.findDistinctCity();
        } catch (Exception e) {
            System.out.println(e);
            return Arrays.asList();
        }
    }
//
//    @GetMapping(value="/number")
//    public boolean howMany(Long reader_id)
//    {
//        Long numberofBorrows=borrowedBookRepository.getReaderCountBooks(reader_id);
//        if(numberofBorrows==0)
//        {
//            return true;
//        }
//        return false;
//     //  return numberofBorrows;
//    }


    @GetMapping(value="/number/{id}")
    public boolean howMany(@PathVariable(value = "id") Long reader_id)
    {
        Long numberofBorrows=borrowedBookRepository.getReaderCountBooks(reader_id);
        if(numberofBorrows==0)
        {
            return true;
        }
        return false;
        //  return numberofBorrows;
    }

    @GetMapping(value = "/borrowedbooks")
    public boolean hasBooks(Long reader_id){
        Long numberofborrows= borrowedBookRepository.getReaderCountBooks(reader_id);
        if(numberofborrows==0){
            return true;
        }
        return false;
    }
}



