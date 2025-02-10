package dabbadash.order.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "DeliveryStatus")
public class DeliveryStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String deliveryStatus;

    @ManyToOne
    @JoinColumn(name = "deliveryAgentId")
    private User deliveryAgent;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
