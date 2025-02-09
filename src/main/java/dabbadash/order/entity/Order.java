package dabbadash.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "restaurantId", nullable = false)
    private User restaurant;

    @ManyToOne
    @JoinColumn(name = "deliveryAgentId")
    private User deliveryAgent;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    private String paymentStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
