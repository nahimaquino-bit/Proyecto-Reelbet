package com.reelbet.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status  = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object exObj   = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Throwable ex   = exObj instanceof Throwable ? (Throwable) exObj : null;

        model.addAttribute("status", status);
        model.addAttribute("error", ex == null ? "N/A" : ex.getMessage());
        model.addAttribute("trace", ex == null ? "–" : Arrays.toString(ex.getStackTrace()));
        return "error";
    }
}
