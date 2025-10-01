package com.reelbet.repository;

import com.reelbet.model.Registro;
import com.reelbet.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroRepository extends JpaRepository<Registro, String> {
    Registro findByCedula(String cedula);
    Registro findByUsuario(Usuario usuario);
    Registro findByUsername(String username);

}
