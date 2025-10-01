package com.reelbet.config;

import com.reelbet.model.Rol;
import com.reelbet.model.Usuario;
import com.reelbet.repository.RolRepository;
import com.reelbet.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initRolesAndAdmin(RolRepository rolRepository,
                                               UsuarioRepository usuarioRepository,
                                               PasswordEncoder passwordEncoder) {
        return args -> {
            // Crear roles si no existen
            Rol rolUser = rolRepository.findByNombre("ROLE_USER").orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_USER")));
            Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN").orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_ADMIN")));

            // Crear usuario admin si no existe
            Optional<Usuario> adminExistente = usuarioRepository.findByUsername("totty");
            if (!adminExistente.isPresent()) {
                Usuario admin = new Usuario();
                admin.setUsername("totty");
                admin.setPassword(passwordEncoder.encode("0812"));
                admin.setRoles(Collections.singleton(rolAdmin));
                usuarioRepository.save(admin);
                System.out.println("Usuario admin creado: totty / 0812");
            } else {
                System.out.println("El usuario admin 'totty' ya existe");
            }
        };
    }
}
