package com.restapi.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
	
	@GetMapping("/book/all")
	public ResponseEntity<List<Book>> getAllBooks() {
		ArrayList<Book> allBooks = (ArrayList<Book>) this.bookDao.findAll();
		if(allBooks.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.of(Optional.of(allBooks));
	}
	
	@GetMapping("/book/{id}")
	public ResponseEntity<Optional<Book>> getBook(@PathVariable("id")int id) {
		Optional<Book> mybook = null;
		mybook = this.bookDao.findById(id);
		if(!mybook.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.of(Optional.of(mybook));
	}
	
	@GetMapping("/book/all/{writer}")
	public ResponseEntity<List<Book>> getBookByWriter(@PathVariable("writer")String writer) {
		ArrayList<Book> allBooksByWriter = null;
		allBooksByWriter = (ArrayList<Book>) this.bookDao.findByWriter(writer);
		if(allBooksByWriter.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.of(Optional.of(allBooksByWriter));
	}
	
	
	@PostMapping("/add/book")
	public ResponseEntity<Book> createBook(@RequestBody Book book) {
		Optional<Book> chkbk = this.bookDao.findById(book.getId());
		if(chkbk.isPresent()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		Book rv = this.bookDao.save(book);
		return ResponseEntity.of(Optional.of(rv));
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
