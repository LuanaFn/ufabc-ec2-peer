package com.ufabc.sistemasdistribuidos.repository.local;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufabc.sistemasdistribuidos.dto.local.FileDTO;

public interface FileRepository extends JpaRepository<FileDTO, Long> {

}
