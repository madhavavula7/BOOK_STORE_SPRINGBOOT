package in.bookstore.main.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.bookstore.main.dto.ApiResponse;
import in.bookstore.main.dto.OrderRequest;
import in.bookstore.main.dto.OrderResponse;
import in.bookstore.main.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

	private final OrderService service;

	public OrderController(OrderService service) {
		this.service = service;
	}

	private String getEmail(){
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getName();
	}

	// PLACE ORDER
	@PostMapping
	public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
			@RequestBody @Valid OrderRequest req){

		return ResponseEntity.ok(
				new ApiResponse<>(true,"Order placed",
						service.placeOrder(req,getEmail()))
				);
	}

	// ADMIN → ALL ORDERS
	@GetMapping
	public ResponseEntity<ApiResponse<List<OrderResponse>>> all(){

		return ResponseEntity.ok(
				new ApiResponse<>(true,"All orders",
						service.getAll())
				);
	}

	// GET ONE
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id){

		return ResponseEntity.ok(
				new ApiResponse<>(true,"Order",
						service.getById(id))
				);
	}

	// UPDATE STATUS
	@PutMapping("/{id}/status")
	public ResponseEntity<ApiResponse<OrderResponse>> status(@PathVariable Long id,@RequestParam String status){
		return ResponseEntity.ok(
				new ApiResponse<>(true,"Status updated",
						service.updateStatus(id,status))
				);
	}

	// USER ORDERS
	@GetMapping("/my")
	public ResponseEntity<ApiResponse<List<OrderResponse>>> myOrders(){
		return ResponseEntity.ok(
				new ApiResponse<>(true,"My Orders",
						service.getMyOrders(getEmail()))
				);
	}
}