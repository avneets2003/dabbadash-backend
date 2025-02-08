package dabbadash.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tiffin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tiffinID;

    @Column(nullable = false)
    private String tiffinName;

    @Column(nullable = false)
    private double tiffinPrice;

    @Column(nullable = false)
    private String tiffinDescription;

    @Column //  it is nullable as we are not sure if all service providers would uplaod or not
    private String tiffinImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TiffinType tiffinType;

    public enum TiffinType {
        BREAKFAST,
        LUNCH,
        DINNER,
        PUNJABI,
        CHINESE,
        SOUTH_INDIAN,
        ITALIAN,
        CONTINENTAL,
        SNACKS,
        BEVERAGES,
        OTHERS
    }

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private User restaurant;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
