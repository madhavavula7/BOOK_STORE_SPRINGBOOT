package in.bookstore.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.bookstore.main.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	List<Order> findByUserEmail(String email);

}
