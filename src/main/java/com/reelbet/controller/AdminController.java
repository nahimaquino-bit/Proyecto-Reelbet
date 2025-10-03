package com.reelbet.controller;

import com.reelbet.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/crearAdmin")
    public String mostrarFormularioCrearAdmin(Model model) {
        model.addAttribute("usuarioForm", new UsuarioForm());
        return "crearAdmin"; // Vista en templates/crearAdmin.html
    }

    @PostMapping("/crearAdmin")
    public String crearAdmin(@ModelAttribute UsuarioForm usuarioForm, Model model) {
        try {
            if (usuarioService.existePorUsername(usuarioForm.getUsername())) {
                model.addAttribute("error", "El nombre de usuario ya está registrado.");
                return "crearAdmin";
            }
            if (!usuarioForm.getPassword().equals(usuarioForm.getConfirmPassword())) {
                model.addAttribute("error", "Las contraseñas no coinciden.");
                return "crearAdmin";
            }
            usuarioService.registrarAdmin(usuarioForm.getUsername(), usuarioForm.getPassword());

            model.addAttribute("mensaje", "Administrador creado con exito.");
            model.addAttribute("usuarioForm", new UsuarioForm()); // limpiar formulario
            return "crearAdmin";

        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el administrador: " + e.getMessage());
            return "crearAdmin";
        }
    }

    public static class UsuarioForm {
        private String username;
        private String password;
        private String confirmPassword;

        // Getters y setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    }
}
