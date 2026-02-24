package in.bookstore.main.service;

import java.util.List;

import in.bookstore.main.dto.OrderRequest;
import in.bookstore.main.dto.OrderResponse;

public interface OrderService {

	OrderResponse placeOrder(OrderRequest req, String email);

    List<OrderResponse> getAll();

    OrderResponse getById(Long id);

    OrderResponse updateStatus(Long id,String status);

    List<OrderResponse> getMyOrders(String email);

}