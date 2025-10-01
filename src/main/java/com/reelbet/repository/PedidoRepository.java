package com.reelbet.repository;

import com.reelbet.model.Pedido;
import com.reelbet.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    String idPedido(Integer idPedido);
    List<Pedido> findByCedula(String cedula);
    List<Pedido> findByUsuario(Usuario usuario);


    // Puedes agregar consultas personalizadas si las necesitas
}
