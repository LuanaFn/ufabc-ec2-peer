package com.example.repository.local;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dto.local.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

}
