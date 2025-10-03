package com.reelbet.controller;

import com.reelbet.model.Registro;
import com.reelbet.repository.RegistroRepository;
import com.reelbet.validation.RegistroValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.validation.Valid;

@Controller
public class RegistroController {

    @Autowired
    private RegistroRepository registroRepo;

    @Autowired
    private RegistroValidator registroValidator;

    @GetMapping("/registros")
    public String mostrarFormulario(Model model,
                                    Authentication authentication,
                                    @RequestParam(value = "username", required = false) String username) {
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
        }

        if (username == null || username.isEmpty()) {
            return "redirect:/login";
        }

        Registro registroExistente = registroRepo.findByUsername(username);

        if (registroExistente != null) {
            model.addAttribute("mensaje", "Ya tienes tus datos registrados.");
            model.addAttribute("registro", registroExistente);
        } else {
            Registro nuevoRegistro = new Registro();
            nuevoRegistro.setUsername(username);
            model.addAttribute("registro", nuevoRegistro);
        }

        return "registros";
    }

    @PostMapping("/registros")
    public String guardarRegistro(@Valid @ModelAttribute Registro registro,
                                  BindingResult result,
                                  Authentication authentication,
                                  Model model) {

        boolean autenticado = authentication != null && authentication.isAuthenticated();
        String username = autenticado ? authentication.getName() : registro.getUsername();

        if (username == null || username.isEmpty()) {
            return "redirect:/login";
        }

        // Validar usando RegistroValidator
        registroValidator.validate(registro, result);

        if (result.hasErrors()) {
            model.addAttribute("registro", registro);
            return "registros";
        }

        Registro registroExistente = registroRepo.findByUsername(username);

        if (registroExistente != null) {
            registroExistente.setCedula(registro.getCedula());
            registroExistente.setNombre(registro.getNombre());
            registroExistente.setApellido(registro.getApellido());
            registroExistente.setTelefono(registro.getTelefono());
            registroExistente.setDireccion(registro.getDireccion());
            registroExistente.setEmail(registro.getEmail());

            registroRepo.save(registroExistente);
            model.addAttribute("mensaje", "Datos actualizados correctamente.");

            if (!autenticado) {
                return "redirect:/login?mensaje=datosActualizados";
            }

            model.addAttribute("registro", registroExistente);
            return "registros";
        } else {
            registro.setUsername(username);
            registroRepo.save(registro);

            if (!autenticado) {
                return "redirect:/login?mensaje=datosGuardados";
            }

            model.addAttribute("mensaje", "Datos registrados con exito.");
            model.addAttribute("registro", registro);
            return "registros";
        }
    }
}
