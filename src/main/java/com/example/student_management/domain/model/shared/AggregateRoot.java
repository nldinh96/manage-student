package com.example.student_management.domain.model.shared;

import jakarta.persistence.MappedSuperclass;

/**
 * Base class for all Aggregate Roots
 */
@MappedSuperclass
public abstract class AggregateRoot {
    // Common behavior for all aggregates can be added here
}
