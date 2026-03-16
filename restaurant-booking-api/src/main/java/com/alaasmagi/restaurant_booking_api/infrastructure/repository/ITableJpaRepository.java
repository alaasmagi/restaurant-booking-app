package com.alaasmagi.restaurant_booking_api.infrastructure.repository;

import com.alaasmagi.restaurant_booking_api.domain.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ITableJpaRepository extends JpaRepository<TableEntity, UUID> {
}

