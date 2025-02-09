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
@Table(name = "Tiffins")
public class Tiffin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tiffinId;

    @Column(nullable = false)
    private String tiffinName;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String tiffinDescription;

    @Column
    private String tiffinImage;

    @Column(nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "restaurantId", nullable = false)
    private User restaurant;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
