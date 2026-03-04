package in.bookstore.main.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	private Double totalPrice;

	private String orderStatus;

	private String paymentStatus;
	
	private LocalDateTime orderDate;
	
	@Column(nullable = false)
	private Double taxAmount;

	@Column(nullable = false)
	private Double shippingCharges;

	@Column(nullable = false)
	private Double netAmount;
	
	@Column(unique = true)
	private String invoiceNumber;
	
	private String sellerName = "IN.BOOKSTORE PVT LTD";
	private String sellerTaxId = "27AAACV1234L1Z5";
	private String sellerAddress = "123 Tech Park, Hyderabad, India";

	private String billingAddress;
	private String shippingAddress;
	private String placeOfSupply;
	
	
	
	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getSellerTaxId() {
		return sellerTaxId;
	}

	public void setSellerTaxId(String sellerTaxId) {
		this.sellerTaxId = sellerTaxId;
	}

	public String getSellerAddress() {
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getPlaceOfSupply() {
		return placeOfSupply;
	}

	public void setPlaceOfSupply(String placeOfSupply) {
		this.placeOfSupply = placeOfSupply;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getShippingCharges() {
		return shippingCharges;
	}

	public void setShippingCharges(Double shippingCharges) {
		this.shippingCharges = shippingCharges;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	@PrePersist
	protected void onCreate() {
		this.orderDate = LocalDateTime.now();
	}

	@OneToMany(mappedBy="order",cascade=CascadeType.ALL)
	private List<OrderItem> items;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
}
