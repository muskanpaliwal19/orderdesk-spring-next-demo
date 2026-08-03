package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderItemRequest;
import com.example.orderservice.model.AuditLog;
import com.example.orderservice.model.Customer;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.Product;
import com.example.orderservice.repository.AuditLogRepository;
import com.example.orderservice.repository.CustomerRepository;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private OrderService orderService;

    private Product activeProduct1;
    private Product activeProduct2;
    private Product inactiveProduct;
    private Customer customer;

    @BeforeEach
    void setUp() {
        activeProduct1 = new Product();
        activeProduct1.setId(1L);
        activeProduct1.setName("Laptop");
        activeProduct1.setPriceCents(120000);
        activeProduct1.setActive(true);

        activeProduct2 = new Product();
        activeProduct2.setId(2L);
        activeProduct2.setName("Mouse");
        activeProduct2.setPriceCents(2500);
        activeProduct2.setActive(true);

        inactiveProduct = new Product();
        inactiveProduct.setId(3L);
        inactiveProduct.setName("Old Keyboard");
        inactiveProduct.setPriceCents(1500);
        inactiveProduct.setActive(false);

        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
    }

    private CreateOrderRequest createRequest(Long customerId, List<OrderItemRequest> items) {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(customerId);
        request.setItems(items);
        return request;
    }

    private OrderItemRequest createItemRequest(Long productId, int quantity) {
        OrderItemRequest item = new OrderItemRequest();
        item.setProductId(productId);
        item.setQuantity(quantity);
        return item;
    }

    @Test
    void createOrder_Success() {
        long customerId = 1L;
        OrderItemRequest item1 = createItemRequest(1L, 1);
        OrderItemRequest item2 = createItemRequest(2L, 2);
        CreateOrderRequest request = createRequest(customerId, List.of(item1, item2));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdInAndIsActiveTrue(List.of(1L, 2L))).thenReturn(List.of(activeProduct1, activeProduct2));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(100L); // Set ID for audit log test
            return o;
        });

        Order result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals(customer, result.getCustomer());
        assertEquals(2, result.getOrderItems().size());
        assertEquals(125000, result.getTotalAmountCents());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_ShouldSkipInactiveProducts() {
        long customerId = 1L;
        OrderItemRequest item1 = createItemRequest(1L, 1);
        OrderItemRequest item2 = createItemRequest(3L, 1); // Inactive
        CreateOrderRequest request = createRequest(customerId, List.of(item1, item2));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdInAndIsActiveTrue(List.of(1L, 3L))).thenReturn(List.of(activeProduct1));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals(1, result.getOrderItems().size());
        assertEquals(1L, result.getOrderItems().get(0).getProduct().getId());
        assertEquals(120000, result.getTotalAmountCents());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_FailsWhenAllProductsAreInactive() {
        long customerId = 1L;
        OrderItemRequest item1 = createItemRequest(3L, 1);
        CreateOrderRequest request = createRequest(customerId, List.of(item1));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdInAndIsActiveTrue(List.of(3L))).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));
        assertEquals("No valid products found in the order.", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_FailsForEmptyProductList() {
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        CreateOrderRequest request = createRequest(customerId, Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));
        assertEquals("No valid products found in the order.", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_FailsForNullProductList() {
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        CreateOrderRequest request = createRequest(customerId, null);

        assertThrows(NullPointerException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_FailsWhenNoProductsFound() {
        long customerId = 1L;
        OrderItemRequest item1 = createItemRequest(99L, 1);
        CreateOrderRequest request = createRequest(customerId, List.of(item1));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdInAndIsActiveTrue(List.of(99L))).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));
        assertEquals("No valid products found in the order.", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_FailsWhenCustomerNotFound() {
        long customerId = 99L;
        OrderItemRequest item1 = createItemRequest(1L, 1);
        CreateOrderRequest request = createRequest(customerId, List.of(item1));

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(request));
        assertEquals("Customer not found with id: 99", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }
}
