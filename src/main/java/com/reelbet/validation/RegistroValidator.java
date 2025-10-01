package com.reelbet.validation;

import com.reelbet.model.Registro;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegistroValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Registro.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Registro registro = (Registro) target;

        if (!CedulaValidator.isValid(registro.getCedula())) {
            errors.rejectValue("cedula", "registro.cedula.invalida", "La cédula no es válida");
        }

        String telefono = registro.getTelefono();
        if (telefono != null && !telefono.isEmpty()) {
            if (!telefono.matches("^(09|07)\\d{8}$")) {
                errors.rejectValue("telefono", "registro.telefono.invalido", "Teléfono inválido (Ej: 0991234567)");
            }
        }

        if (registro.getDireccion() == null || registro.getDireccion().length() < 5) {
            errors.rejectValue("direccion", "registro.direccion.corta", "La dirección es muy corta");
        }

        String email = registro.getEmail();
        if (email != null && !email.isEmpty()) {
            if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
                errors.rejectValue("email", "registro.email.invalido", "Email inválido");
            }
        }
    }
}
