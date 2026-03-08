package in.bookstore.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bookstore.main.dto.ApiResponse;
import in.bookstore.main.entity.Book;
import in.bookstore.main.repository.BookRepository;
import in.bookstore.main.repository.OrderRepository;
import in.bookstore.main.repository.UserRepository;
import in.bookstore.main.service.BookService;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private BookService bookService;

    // 1. Access all Users
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // 2. Access all Books/Inventory
    @GetMapping("/inventory")
    public ResponseEntity<?> getFullInventory() {
        return ResponseEntity.ok(bookRepository.findAll());
    }

    // Delete any User by amdin.
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    	if(!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body("User not found");
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
    
    @PutMapping("/inventory/bulk-update")
    public ResponseEntity<ApiResponse<String>> bulkUpdate(@RequestBody List<Book> updatedBooks) {
        // Call the service layer to handle the logic
    	bookService.updateBulkInventory(updatedBooks);
        
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Inventory updated successfully!", null)
        );
    }
    
    @GetMapping("/all-orders")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }
    
    @PutMapping("/order/{orderId}/status")
    public ResponseEntity<ApiResponse<Object>> updateOrderStatus(
        @PathVariable Long orderId, 
        @RequestBody java.util.Map<String, String> request) {
        
        return orderRepository.findById(orderId).map(order -> {
            String newStatus = request.get("status");
            order.setOrderStatus(newStatus);
            if ("DELIVERED".equalsIgnoreCase(newStatus)) {
                order.setPaymentStatus("PAID");
            } else if ("CANCELLED".equalsIgnoreCase(newStatus)) {
                order.setPaymentStatus("VOID");
            }
            
            orderRepository.save(order);
            return ResponseEntity.ok(new ApiResponse<>(true, "Logistics updated", null));
            
        }).orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "Order not found", null)));
    }
    
    
}
