package com.github.torleifg.freg.event.adapter.out.db;

import lombok.Value;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Table("sequence")
public class SequenceEntity {
    Long id;
}