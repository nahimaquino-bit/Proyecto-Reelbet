package com.reelbet.controller;

import com.reelbet.model.Pedido;
import com.reelbet.model.Stock;
import com.reelbet.model.Usuario;
import com.reelbet.repository.PedidoRepository;
import com.reelbet.repository.RegistroRepository;
import com.reelbet.repository.StockRepository;
import com.reelbet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RegistroRepository registroRepo;


    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/home/user")
    public String homeUser(Authentication authentication, Model model) {
        String username = authentication.getName(); // Obtener username autenticado
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario == null) {
            return "redirect:/login"; // Si no existe, redirigir a login
        }

        // Si es admin, redirigir a home_admin
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/home/admin";
        }
        if (registroRepo.findByUsername(username) == null) {
            return "redirect:/registros"; // Redirigir si aún no ha llenado sus datos
        }

        // Para usuarios comunes, cargar usuario y pedidos en el modelo
        model.addAttribute("usuario", usuario);

        List<Pedido> pedidos = pedidoRepository.findByUsuario(usuario);
        model.addAttribute("pedidos", pedidos);

        Map<Integer, Double> totales = new HashMap<>();
        for (Pedido p : pedidos) {
            totales.put(p.getIdPedido(), calcularTotal(p));
        }
        model.addAttribute("totales", totales);


        // Mostrar la vista home_user.html
        return "home_user";
    }

    @GetMapping("/home/admin")
    public String homeAdmin(Model model) {
        // Obtener lista de productos con stock menor que 15
        List<Stock> lowStockList = stockRepository.findByCantidadLessThan(15);

        // Agregar al modelo para la vista
        model.addAttribute("lowStockList", lowStockList);

        return "home_admin";
    }


    private Double calcularTotal(Pedido pedido) {
        double total = 0.0;

        if ("SI".equalsIgnoreCase(pedido.getCevicheBlanco()))
            total += obtenerPrecio("Ceviche Blanco") * pedido.getCantCevBlanco();
        if ("SI".equalsIgnoreCase(pedido.getCevicheRojo()))
            total += obtenerPrecio("Ceviche Rojo") * pedido.getCantCevRojo();
        if ("SI".equalsIgnoreCase(pedido.getCevicheCamaron()))
            total += obtenerPrecio("Ceviche Camaron") * pedido.getCantCevCamaron();
        if ("SI".equalsIgnoreCase(pedido.getEncebollado()))
            total += obtenerPrecio("Encebollado") * pedido.getCantEncebollado();
        if ("SI".equalsIgnoreCase(pedido.getBolloPescado()))
            total += obtenerPrecio("Bollo Pescado") * pedido.getCantBolloPescado();
        if ("SI".equalsIgnoreCase(pedido.getBolloPollo()))
            total += obtenerPrecio("Bollo Pollo") * pedido.getCantBolloPollo();
        if ("SI".equalsIgnoreCase(pedido.getBolloCamaron()))
            total += obtenerPrecio("Bollo Camaron") * pedido.getCantBolloCamaron();

        return total;
    }

    private double obtenerPrecio(String nombre) {
        Stock stock = stockRepository.findByNombreProductoIgnoreCase(nombre);
        return stock != null ? stock.getPrecio() : 0.0;
    }


}
