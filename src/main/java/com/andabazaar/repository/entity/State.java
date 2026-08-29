package com.andabazaar.repository.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "states", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class State extends BaseEntity {
    public State(Long id, String name) {
        super(id, name);
    }
}
