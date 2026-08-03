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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @InjectMocks
    private OrderService orderService;

    private Product activeProduct1;
    private Product activeProduct2;
    private Product inactiveProduct;
    private Customer customer;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        activeProduct1 = new Product();
        activeProduct1.setId(1L);
        activeProduct1.setName("Laptop");
        activeProduct1.setPrice(new BigDecimal("1200.00"));
        activeProduct1.setActive(true);

        activeProduct2 = new Product();
        activeProduct2.setId(2L);
        activeProduct2.setName("Mouse");
        activeProduct2.setPrice(new BigDecimal("25.00"));
        activeProduct2.setActive(true);

        inactiveProduct = new Product();
        inactiveProduct.setId(3L);
        inactiveProduct.setName("Old Keyboard");
        inactiveProduct.setPrice(new BigDecimal("15.00"));
        inactiveProduct.setActive(false);

        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
    }
    
    private void mockSecurityContext(Long customerId) {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(String.valueOf(customerId));
    }

    private CreateOrderRequest createRequest(List<OrderItemRequest> items) {
        CreateOrderRequest request = new CreateOrderRequest();
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
        // AC1: Successful Order Creation
        long customerId = 1L;
        mockSecurityContext(customerId);
        OrderItemRequest item1 = createItemRequest(1L, 1);
        OrderItemRequest item2 = createItemRequest(2L, 2);
        CreateOrderRequest request = createRequest(List.of(item1, item2));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdInAndIsActiveTrue(List.of(1L, 2L))).thenReturn(List.of(activeProduct1, activeProduct2));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(100L); // Set ID for audit log test
            return o;\n        });

        Order result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals(customer, result.getCustomer());
        assertEquals(2, result.getOrderItems().size());
        // Total price = (1 * 1200.00) + (2 * 25.00) = 1250.00
        assertEquals(0, new BigDecimal("1250.00").compareTo(result.getTotalAmount()));

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(any());
        
        ArgumentCaptor<AuditLog> auditLogCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(auditLogCaptor.capture());
        AuditLog auditLog = auditLogCaptor.getValue();

        assertEquals(result.getId(), auditLog.getEntityId());
        assertEquals("order", auditLog.getEntityName());
        assertEquals("created", auditLog.getAction());
    }

    @Test
    void createOrder_ShouldSkipInactiveProducts() {
        // AC2: Inactive Products Skipped
        long customerId = 1L;
        mockSecurityContext(customerId);
        OrderItemRequest item1 = createItemRequest(1L, 1);
        OrderItemRequest item2 = createItemRequest(3L, 1);
        CreateOrderRequest request = createRequest(List.of(item1, item2));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(.customer));
        when(productRepository.findByIdInAndIsActiveTrue(List.of(1L, 3L))).thenReturn(List.of(activeProduct1));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals(1, result.getOrderItems().size());
        assertEquals(1L, result.getOrderItems().get(0).getProduct().getId());
        assertEquals(0, new BigDecimal("1200.00").compareTo(result.getTotalAmount()));

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(any());
    }

    @Test
    void createOrder_FailsWhenAllProductsAreInactive() {
        // AC3: All Products Inactive
        long customerId = 1L;
        mockSecurityContext(customerId);
        OrderItemRequest item1 = createItemRequest(3L, 1);
        CreateOrderRequest request = createRequest(List.of(item1));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdInAndIsActiveTrue(List.of(3L))).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));

        assertEquals("No active products found to create order.", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_FailsForEmptyProductList() {
        // AC4: Empty Product List
        mockSecurityContext(1L);
        CreateOrderRequest request = createRequest(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));

        assertEquals("Order must contain at least one item.", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_FailsForNullProductList() {
        // AC4: Empty Product List (null case)
        mockSecurityContext(1L);
        CreateOrderRequest request = createRequest(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));

        assertEquals("Order must contain at least one item.", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_FailsWhenNoProductsFound() {
        // AC5: Non-existent Products
        long customerId = 1L;
        mockSecurityContext(customerId);
        OrderItemRequest item1 = createItemRequest(99L, 1);
        CreateOrderRequest request = createRequest(List.of(item1));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(productRepository.findByIdInAndIsActiveTrue(List.of(99L))).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(request));

        assertEquals("No active products found to create order.", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }
    
    @Test
    void createOrder_FailsWhenCustomerNotFound() {
        long customerId = 99L;
        mockSecurityContext(customerId);
        OrderItemRequest item1 = createItemRequest(1L, 1);
        CreateOrderRequest request = createRequest(List.of(item1));

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(request));
        
        assertEquals("Customer not found with id: 99", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }
}
