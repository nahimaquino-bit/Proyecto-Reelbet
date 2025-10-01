package com.reelbet.repository;

import com.reelbet.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // Buscar stock por nombre del producto
    Stock findByNombreProductoIgnoreCase(String nombreProducto);
    List<Stock> findByCantidadLessThan(int cantidadThreshold);

}
