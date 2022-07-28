package com.github.torleifg.freg.person.adapter.out.db;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("person")
public class PersonEntity {
    @Id
    String id;
    String name;
}