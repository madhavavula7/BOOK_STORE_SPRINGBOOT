package in.bookstore.main.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.bookstore.main.dto.BookRequest;
import in.bookstore.main.entity.Book;
import in.bookstore.main.repository.BookRepository;
import in.bookstore.main.service.BookService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository repo;


	@Override
	public List<Book> getAll(){
		return repo.findAll();
	}

	@Override
	public Book add(BookRequest dto) {
		Book book = new Book();
		book.setTitle(dto.getTitle());
		book.setAuthor(dto.getAuthor());
		book.setGenre(dto.getGenre());
		book.setIsbn(dto.getIsbn());
		book.setPrice(dto.getPrice());
		book.setDescription(dto.getDescription());
		book.setStockQuantity(dto.getStockQuantity());
		book.setImageUrl(dto.getImageUrl());
		return repo.save(book);
	}
}

