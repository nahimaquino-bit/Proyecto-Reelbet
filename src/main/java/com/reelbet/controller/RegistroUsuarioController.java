package com.reelbet.controller;

import com.reelbet.model.Usuario;
import com.reelbet.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar formulario de registro
    @GetMapping("/registroUsuarios")
    public String mostrarFormularioRegistro(Model model, Authentication authentication) {
        // Si ya está autenticado y es un cliente, redirigir a completar sus datos
        if (authentication != null && authentication.isAuthenticated()) {
            boolean esCliente = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));
            if (esCliente) {
                return "redirect:/registros";
            }
        }

        model.addAttribute("usuarioForm", new UsuarioForm());
        return "registroUsuarios";
    }

    // Procesar registro de usuario (rol fijo USER)
    @PostMapping("/registroUsuarios")
    public String registrarUsuario(@RequestParam String username,
                                   @RequestParam String password,
                                   Model model) {

        // Validación de campos vacíos
        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "El usuario y la contraseña son obligatorios.");
            return "registroUsuarios";
        }

        // Validar si el usuario ya existe
        if (usuarioService.existePorUsername(username)) {
            model.addAttribute("error", "El nombre de usuario ya está registrado.");
            return "registroUsuarios";
        }

        // Crear usuario con rol USER fijo
        try {
            usuarioService.registrarUsuario(username, password);
            return "redirect:/login?registroPendiente=true";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el usuario: " + e.getMessage());
            return "registroUsuarios";
        }
    }

    // Clase interna para manejar datos del formulario (DTO)
    public static class UsuarioForm {
        private String username;
        private String password;
        private String confirmPassword;

        // Getters y Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    }
}
