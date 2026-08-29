package com.andabazaar.repository.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "countries", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Country extends BaseEntity {
    public Country(Long id, String name) {
        super(id, name);
    }
}
