
package com.example.orders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.example.orders.dto.OrderExportRow;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return orderService.getOrder(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            OrderStatus status = OrderStatus.valueOf(body.get("status").toUpperCase());
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/export")
    public void exportOrders(HttpServletResponse response, @RequestParam(required = false) OrderStatus status) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"orders.csv\"");

        List<OrderExportRow> orders = orderService.findOrdersForExport(status);
        try (PrintWriter writer = response.getWriter()) {
            writer.println("id,customer_email,status,order_date,total_cents");

            for (OrderExportRow order : orders) {
                writer.println(
                        sanitizeForCsv(order.id()) + "," +
                                sanitizeForCsv(order.customerEmail()) + "," +
                                sanitizeForCsv(order.status()) + "," +
                                sanitizeForCsv(order.orderDate()) + "," +
                                sanitizeForCsv(order.totalCents())
                );
            }
        }
    }

    private String sanitizeForCsv(Object data) {
        if (data == null) {
            return "";
        }
        return "\"" + String.valueOf(data).replace("\"", "\"\"") + "\"";
    }
}
