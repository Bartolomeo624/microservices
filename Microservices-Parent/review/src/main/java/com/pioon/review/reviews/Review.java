package com.pioon.review.reviews;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long userId;
    private long productId;

    private float stars;
    private String text;
    private String userName;
    private String productName;

}
