package com.reelbet.service;

import com.reelbet.model.Rol;
import com.reelbet.model.Usuario;
import com.reelbet.repository.RolRepository;
import com.reelbet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Registro público: siempre cliente (ROLE_USER)
    public Usuario registrarUsuario(String username, String password) throws Exception {
        if (usuarioRepository.existsByUsername(username)) {
            throw new Exception("El usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));

        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new Exception("Rol USER no encontrado"));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolUser);
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }

    // 🔐 Crear admin manualmente desde panel (ROLE_ADMIN)
    public Usuario registrarAdmin(String username, String password) throws Exception {
        if (usuarioRepository.existsByUsername(username)) {
            throw new Exception("El usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));

        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                .orElseThrow(() -> new Exception("Rol ADMIN no encontrado"));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolAdmin);
        usuario.setRoles(roles);

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
}
