package com.restapi.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restapi.app.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
	public List<Book> findByWriter(String writer);
}
