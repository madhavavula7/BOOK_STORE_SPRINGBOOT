package in.bookstore.main.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.bookstore.main.dto.BookRequest;
import in.bookstore.main.entity.Book;
import in.bookstore.main.repository.BookRepository;
import in.bookstore.main.service.BookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;


	@Override
	public List<Book> getAll(){
		return bookRepository.findAll();
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
		return bookRepository.save(book);
	}
	
	@Override
	@Transactional
    public void updateBulkInventory(List<Book> updatedBooks) {
        for (Book updatedBook : updatedBooks) {
            // 1. Find existing record
            Book existingBook = bookRepository.findById(updatedBook.getId())
                    .orElseThrow(() -> new RuntimeException("Book ID " + updatedBook.getId() + " not found"));

            // 2. Rewrite/Update the values
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setPrice(updatedBook.getPrice());
            existingBook.setStockQuantity(updatedBook.getStockQuantity());
            existingBook.setGenre(updatedBook.getGenre());
            existingBook.setImageUrl(updatedBook.getImageUrl());

            // 3. Save inside the loop (Hibernate batches these automatically)
            bookRepository.save(existingBook);
        }
    }
}

