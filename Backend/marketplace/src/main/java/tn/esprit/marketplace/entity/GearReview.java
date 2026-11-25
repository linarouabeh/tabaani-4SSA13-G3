package tn.esprit.marketplace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class GearReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reviewerId;
    private Integer rating;

    @Column(name = "review_comment")
    private String comment;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "gear_id")
    private GearItem gear;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}