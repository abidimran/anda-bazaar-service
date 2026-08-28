package com.andabazaar.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true
    )
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private Boolean subscriptionNotification = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean priceNotification = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean priceAlertNotification = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean newsNotification = true;

    @Column(nullable = false)
    @Builder.Default
    private Boolean generalNotification = true;
}