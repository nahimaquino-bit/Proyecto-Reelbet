package com.reelbet.controller;

import com.reelbet.model.Stock;
import com.reelbet.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockRepository stockRepo;


    // Mostrar lista de productos
    @GetMapping
    public String listarStock(Model model) {
        List<Stock> listaStock = stockRepo.findAll();
        List<Stock> lowStockList = stockRepo.findByCantidadLessThan(15);
        model.addAttribute("listaStock", listaStock);
        return "stock_lista";
    }

    // Mostrar formulario para nuevo producto
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Stock());
        return "stock_form";
    }

    // Guardar nuevo producto
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Stock producto, Model model) {

        // Buscar si ya existe un producto con el mismo nombre (ignorar mayúsculas/minúsculas)
        Stock existente = stockRepo.findByNombreProductoIgnoreCase(producto.getNombreProducto());

        if (existente != null) {
            // Actualizar la cantidad sumando lo nuevo
            existente.setCantidad(existente.getCantidad() + producto.getCantidad());

            // Actualizar el precio si deseas (puedes dejarlo igual si no quieres)
            existente.setPrecio(producto.getPrecio());

            // Guardar el actualizado
            stockRepo.save(existente);

        } else {
            // Si no existe, guardar normalmente
            stockRepo.save(producto);
        }

        return "redirect:/stock";
    }

}
