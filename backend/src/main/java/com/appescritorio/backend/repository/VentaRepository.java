package com.appescritorio.backend.repository;

import com.appescritorio.backend.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
