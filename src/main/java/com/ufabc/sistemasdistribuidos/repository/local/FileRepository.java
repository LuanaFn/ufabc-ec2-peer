package com.ufabc.sistemasdistribuidos.repository.local;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ufabc.sistemasdistribuidos.dto.local.Estado;
import com.ufabc.sistemasdistribuidos.dto.local.FileDTO;

public interface FileRepository extends JpaRepository<FileDTO, Long> {
	@Query("select f from FileDTO f where f.name = :nome")
	List<FileDTO> find(@Param("nome") String nome);
	
	@Query("select f from FileDTO f where f.name = :nome and f.estado.id = 1")
	List<FileDTO> findMeuArquivo(@Param("nome") String nome);
	
	@Query("select f.estado from FileDTO f where f.name = :nome")
	List<Estado> findQuemTem(@Param("nome") String nome);
}
