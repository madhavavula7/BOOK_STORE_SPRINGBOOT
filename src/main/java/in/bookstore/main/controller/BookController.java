package in.bookstore.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bookstore.main.dto.BookRequest;
import in.bookstore.main.entity.Book;
import in.bookstore.main.service.BookService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookController {

	@Autowired
	private BookService service;

	

	@GetMapping
	public List<Book> getAll(){
		return service.getAll();
	}

	@PostMapping
	public Book add(@RequestBody BookRequest dto){
		return service.add(dto);
	}
}
