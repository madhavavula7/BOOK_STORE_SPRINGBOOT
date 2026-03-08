package in.bookstore.main.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
    private OrderRepository orderRepo;
	@Autowired
    private BookRepository bookRepo;
	@Autowired
    private UserRepository userRepo;   


    @Override
    @Transactional // Ensures stock and order details are rolled back if something fails
    public OrderResponse placeOrder(OrderRequest req, String email) {
        // Fetch User
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
     // --- FROM THE USER (Request) ---
        order.setShippingAddress(req.getShippingAddress());
        order.setBillingAddress(req.getBillingAddress());
        order.setState(req.getState());
        order.setPincode(req.getPincode());
        order.setPlaceOfSupply(req.getState()); 

        // --- FROM THE ADMIN (Hardcoded/System) ---
        order.setSellerName("BOOKSTORE PVT LTD");
        order.setSellerTaxId("27AAACV1234L1Z5"); // Your GSTIN/VAT
        order.setSellerAddress("123 Tech Park, Hyderabad, India");
        order.setOrderStatus("CASH ON DELIVERY");
        order.setPaymentStatus("UNPAID");
        order.setOrderDate(java.time.LocalDateTime.now()); // Set the timestamp

        List<OrderItem> items = new ArrayList<>();
        double subTotal = 0;

        // Process Items and Deduct Stock
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
            item.setPrice(book.getPrice()); // Capture unit price at time of purchase
            item.setOrder(order);
            
            subTotal += (book.getPrice() * i.getQuantity());
            items.add(item);
        }

        // Invoice Calculations
        double taxRate = 0.18; // 18% GST/VAT
        double shipping = subTotal > 500 ? 0.0 : 40.0; // Free shipping over ₹500
        double taxAmount = subTotal * taxRate;
        double finalTotal = subTotal + taxAmount + shipping;

        // Set Financial Details to Order Entity
        order.setItems(items);
        order.setNetAmount(subTotal);
        order.setTaxAmount(taxAmount);
        order.setShippingCharges(shipping);
        order.setTotalPrice(finalTotal);

        // Save initially to generate the Database ID
        Order savedOrder = orderRepo.save(order);

        // Generate and update the Unique Invoice Number
        // Format: INV-2026-00001
        String invoiceNo = String.format("INV-%d-%05d", 
                            java.time.LocalDate.now().getYear(), 
                            savedOrder.getId());
        savedOrder.setInvoiceNumber(invoiceNo);

        // Final save and map to DTO
        return map(orderRepo.save(savedOrder));
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
        return orderRepo.findByUserEmailOrderByIdDesc(email).stream().map(this::map).toList();
    }

    @Override
    public OrderResponse updateStatus(Long id, String status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        return map(orderRepo.save(order));
    }

    private OrderResponse map(Order order) {
        OrderResponse res = new OrderResponse();
        res.setOrderId(order.getId());
        res.setInvoiceNumber(order.getInvoiceNumber()); // Map the INV string
        res.setCustomer(order.getUser().getName());
        res.setEmail(order.getUser().getEmail());
        
        // Map the addresses saved during placeOrder
        res.setShippingAddress(order.getShippingAddress());
        res.setBillingAddress(order.getBillingAddress());
        res.setState(order.getState());
        res.setPincode(order.getPincode());
        
        res.setNetAmount(order.getNetAmount());
        res.setTaxAmount(order.getTaxAmount());
        res.setShippingCharges(order.getShippingCharges());
        res.setTotalPrice(order.getTotalPrice());
        
        res.setOrderStatus(order.getOrderStatus());
        res.setOrderDate(order.getOrderDate());
        
        if (order.getItems() != null) {
            res.setItems(order.getItems().stream().map(item -> {
                OrderResponse.ItemDetail detail = new OrderResponse.ItemDetail();
                detail.setBookId(item.getBook().getId());
                detail.setTitle(item.getBook().getTitle());
                detail.setImageUrl(item.getBook().getImageUrl());
                detail.setPrice(item.getPrice());
                detail.setQuantity(item.getQuantity());
                detail.setGenre(item.getBook().getGenre());
                detail.setIsbn(item.getBook().getIsbn());
                detail.setDescription(item.getBook().getDescription());
                detail.setAuthor(item.getBook().getAuthor());
                return detail;
            }).toList());
        }
        return res;
    }
}
