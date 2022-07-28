package com.github.torleifg.freg.event.adapter.out.db;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("sequence")
public class SequenceEntity {
    @Id
    Long id;
}