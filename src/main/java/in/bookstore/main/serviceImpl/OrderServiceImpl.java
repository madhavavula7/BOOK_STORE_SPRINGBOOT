package in.bookstore.main.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.bookstore.main.dto.OrderRequest;
import in.bookstore.main.dto.OrderResponse;
import in.bookstore.main.entity.Book;
import in.bookstore.main.entity.Order;
import in.bookstore.main.entity.OrderItem;
import in.bookstore.main.entity.User;
import in.bookstore.main.repository.BookRepository;
import in.bookstore.main.repository.OrderRepository;
import in.bookstore.main.repository.UserRepository;
import in.bookstore.main.service.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Automatically generates the constructor for final fields
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    
    

    public OrderServiceImpl(OrderRepository orderRepo, BookRepository bookRepo, UserRepository userRepo) {
		super();
		this.orderRepo = orderRepo;
		this.bookRepo = bookRepo;
		this.userRepo = userRepo;
	}

	@Override
    @Transactional // Ensures stock is rolled back if something fails
    public OrderResponse placeOrder(OrderRequest req, String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus("CASH ON DELIVERY");
        order.setPaymentStatus("UNPAID");

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (OrderRequest.Item i : req.getItems()) {
            Book book = bookRepo.findById(i.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found: " + i.getBookId()));

            if (book.getStockQuantity() < i.getQuantity()) {
                throw new RuntimeException("Book out of stock: " + book.getTitle());
            }

            // Deduct Stock
            book.setStockQuantity(book.getStockQuantity() - i.getQuantity());

            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setQuantity(i.getQuantity());
            item.setPrice(book.getPrice()); // Unit price
            item.setOrder(order);

            total += (book.getPrice() * i.getQuantity());
            items.add(item);
        }

        order.setItems(items);
        order.setTotalPrice(total);
        
        return map(orderRepo.save(order));
    }

    @Override
    public List<OrderResponse> getAll() {
        return orderRepo.findAll().stream().map(this::map).toList();
    }

    @Override
    public OrderResponse getById(Long id) {
        return orderRepo.findById(id).map(this::map)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<OrderResponse> getMyOrders(String email) {
        return orderRepo.findByUserEmail(email).stream().map(this::map).toList();
    }

    @Override
    public OrderResponse updateStatus(Long id, String status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        return map(orderRepo.save(order));
    }

    // --- CLEAN MAPPER ---
    private OrderResponse map(Order order) {
        OrderResponse res = new OrderResponse();
        res.setOrderId(order.getId());
        res.setCustomer(order.getUser().getName());
        res.setTotalPrice(order.getTotalPrice());
        res.setOrderStatus(order.getOrderStatus());
        res.setOrderDate(order.getOrderDate());
        // Map the items so the frontend can see book details!
        if (order.getItems() != null) {
            List<OrderResponse.ItemDetail> details = order.getItems().stream().map(item -> {
                OrderResponse.ItemDetail detail = new OrderResponse.ItemDetail();
                detail.setBookId(item.getBook().getId());
                detail.setTitle(item.getBook().getTitle());
                detail.setImageUrl(item.getBook().getImageUrl());
                detail.setPrice(item.getPrice());
                detail.setQuantity(item.getQuantity());
                return detail;
            }).toList();
            res.setItems(details);
        }
        return res;
    }
}
