package com.andabazaar.repository.entity;

import com.andabazaar.enums.RoleType;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(
    name = "roles",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_role_name",
            columnNames = "name")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        unique = true,
        length = 30
    )
    private RoleType name;

    @Column(length = 255)
    private String description;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
