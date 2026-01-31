package com.socialmedia.social_app.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(
        name = "follows",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"follower_id", "following_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who follows
    @ManyToOne(optional = false)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    // Who is being followed
    @ManyToOne(optional = false)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;
}
