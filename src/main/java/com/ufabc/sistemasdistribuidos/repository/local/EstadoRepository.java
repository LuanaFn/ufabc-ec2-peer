package com.ufabc.sistemasdistribuidos.repository.local;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ufabc.sistemasdistribuidos.dto.local.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
	@Query("select f from Estado f where f.host = :host and f.port = :port")
	List<Estado> find(@Param("host") String host, @Param("port") int port);
}
