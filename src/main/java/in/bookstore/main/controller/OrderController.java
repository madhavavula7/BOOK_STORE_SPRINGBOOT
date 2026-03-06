package in.bookstore.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin(origins = "*")
public class OrderController {

	@Autowired
	private OrderService service;

	private String getEmail(){
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getName();
	}

	// USER PLACE ORDER
	@PostMapping
	public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
			@RequestBody @Valid OrderRequest req){

		return ResponseEntity.ok(
				new ApiResponse<OrderResponse>(true, "Order placed",
						service.placeOrder(req, getEmail()))
				);
	}

	// USER ORDERS
	@GetMapping("/my")
	public ResponseEntity<ApiResponse<List<OrderResponse>>> myOrders(){
		return ResponseEntity.ok(
				new ApiResponse<List<OrderResponse>>(true, "My Orders",
						service.getMyOrders(getEmail()))
				);
	}

	// ADMIN → ALL ORDERS
	@GetMapping
	public ResponseEntity<ApiResponse<List<OrderResponse>>> all(){

		return ResponseEntity.ok(
				new ApiResponse<List<OrderResponse>>(true, "All orders",
						service.getAll())
				);
	}

	// GET ONE
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long id){

		return ResponseEntity.ok(
				new ApiResponse<OrderResponse>(true, "Order",
						service.getById(id))
				);
	}

	// UPDATE STATUS
	@PutMapping("/{id}/status")
	public ResponseEntity<ApiResponse<OrderResponse>> status(@PathVariable Long id, @RequestParam String status){
		return ResponseEntity.ok(
				new ApiResponse<OrderResponse>(true, "Status updated",
						service.updateStatus(id, status))
				);
	}


}