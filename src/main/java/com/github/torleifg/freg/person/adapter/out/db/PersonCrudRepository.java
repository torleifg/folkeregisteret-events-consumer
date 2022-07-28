package com.github.torleifg.freg.person.adapter.out.db;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PersonCrudRepository extends ReactiveCrudRepository<PersonEntity, String> {
}