package com.andabazaar.repository.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "cities", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class City extends BaseEntity {
    public City(Long id, String name) {
        super(id, name);
    }
}
