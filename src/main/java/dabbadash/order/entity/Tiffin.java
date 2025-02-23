package dabbadash.order.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
// import org.antlr.v4.runtime.misc.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Tiffins")
public class Tiffin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String tiffinName;

    @Column(nullable = false)
    @Min(value = 1, message = "Tiffin price must be greater than 0") // Ensures valid price
    private double tiffinPrice;

    @Column
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
