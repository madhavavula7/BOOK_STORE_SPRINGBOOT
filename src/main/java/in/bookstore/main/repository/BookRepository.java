package in.bookstore.main.repository;

import java.awt.print.Pageable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.bookstore.main.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long>{
	Optional<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

}
