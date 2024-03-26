package com.restapi.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.app.dao.BookRepository;
import com.restapi.app.model.Book;

@RestController
public class FrontController {
	@Autowired
	private BookRepository bookDao;
	
	@GetMapping("/books")
	public List<Book> getAllBooks() {
		ArrayList<Book> allBooks = (ArrayList<Book>) this.bookDao.findAll();
		return allBooks;
	}
	
	@GetMapping("/book/{id}")
	public Optional<Book> getBook(@PathVariable("id")int id) {
		Optional<Book> mybook = null;
		mybook = this.bookDao.findById(id);
		return mybook;
	}
	
	@GetMapping("/book/author/{writer}")
	public List<Book> getBookByWriter(@PathVariable("writer")String writer) {
		ArrayList<Book> allBooksByWriter = null;
		allBooksByWriter = (ArrayList<Book>) this.bookDao.findByWriter(writer);
		return allBooksByWriter;
	}
	
	
	@PostMapping("/add/book")
	public Book createBook(@RequestBody Book book) {
		Book rv = this.bookDao.save(book);
		return rv;
	}
	
	 @PostMapping("/add/books")
	  public ResponseEntity<List<Book>> uploadBooks(@RequestBody List<Book> books) {
	    try {
	      // Save each book in the list to the database
	      List<Book> savedBooks = books.stream()
	          .map(bookDao::save)
	          .collect(Collectors.toList());
	      return ResponseEntity.ok(savedBooks); // Return the list of saved books (status code 201)
	    } catch (Exception e) {
	      // Handle any exceptions during saving (e.g., database errors)
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	  }
}
