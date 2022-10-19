package org.sample.web;

import org.sample.domain.Book;
import org.sample.service.MongoDbService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/mongodb")
public class MongoDbController {

    @Resource
    private MongoDbService mongoDbService;


    @PostMapping("/mongo/save")
    public String saveObj(@RequestBody Book book) {return mongoDbService.save(book).toString();}

    @GetMapping("/mongo/findAll")
    public List<Book> findAll() {return mongoDbService.queryList(new Book());}

    @GetMapping("/mongo/findOne")
    public Book findOne(@RequestParam String id) {return mongoDbService.queryById(Integer.valueOf(id));}

//    @GetMapping("/mongo/findOneByName")
//    public Book findOneByName(@RequestParam String name) {return mongoDbService.getBookByName(name);}
//
//    @PostMapping("/mongo/update")
//    public String update(@RequestBody Book book) {return mongoDbService.updateBook(book);}
//
//    @PostMapping("/mongo/delOne")
//    public String delOne(@RequestBody Book book) {return mongoDbService.deleteBook(book);}
//
//    @GetMapping("/mongo/delById")
//    public String delById(@RequestParam String id) {return mongoDbService.deleteBookById(id);}

}
