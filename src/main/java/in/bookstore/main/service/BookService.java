package in.bookstore.main.service;

import java.util.List;

import in.bookstore.main.dto.BookRequest;
import in.bookstore.main.entity.Book;

public interface BookService {
	List<Book> getAll();
	Book add(BookRequest dto);
	public void updateBulkInventory(List<Book> updatedBooks);
}
