package com.reelbet.controller;

import com.reelbet.model.Pedido;
import com.reelbet.model.Registro;
import com.reelbet.model.Stock;
import com.reelbet.model.Usuario;
import com.reelbet.repository.PedidoRepository;
import com.reelbet.repository.RegistroRepository;
import com.reelbet.repository.StockRepository;
import com.reelbet.service.UsuarioService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PedidoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private StockRepository stockRepo;

    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private RegistroRepository registroRepo;

    @GetMapping("/pedidos")
    public String mostrarFormulario(Model model, Authentication auth) {
        Pedido pedido = new Pedido();

        // Inicializar cantidades a 0 para evitar nulos
        pedido.setCantCevBlanco(0);
        pedido.setCantCevRojo(0);
        pedido.setCantCevCamaron(0);
        pedido.setCantEncebollado(0);
        pedido.setCantBolloPescado(0);
        pedido.setCantBolloPollo(0);
        pedido.setCantBolloCamaron(0);

        model.addAttribute("pedido", pedido);

        // Obtener datos de registro del usuario autenticado
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            Registro registro = registroRepo.findByUsername(username);  // O el método correcto que tienes
            if (registro != null) {
                model.addAttribute("cliente", registro);
            } else {
                model.addAttribute("cliente", new Registro());
            }
        } else {
            model.addAttribute("cliente", new Registro());
        }

        return "pedidos";
    }


    @PostMapping("/buscarCliente")
    public String buscarCliente(@ModelAttribute Pedido pedido, Model model) {
        String cedula = pedido.getCedula();

        Registro cliente = registroRepo.findByCedula(cedula);

        if (cliente != null) {
            pedido.setNombre(cliente.getNombre());
            model.addAttribute("cliente", cliente);
        } else {
            model.addAttribute("error", "Cliente no encontrado.");
            model.addAttribute("cliente", new Registro());
        }

        model.addAttribute("pedido", pedido);
        return "pedidos";
    }

    @PostMapping("/pedidos")
    public String guardarPedido(@ModelAttribute Pedido pedido, Model model, RedirectAttributes redirectAttributes, Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Registro registro = registroRepo.findByUsername(username);
        if (registro == null) {
            prepararVistaConError(pedido, "Debe completar sus datos personales antes de hacer un pedido.", model);
            model.addAttribute("cliente", new Registro());
            return "pedidos";
        }

        // Asignar usuario y datos personales al pedido
        pedido.setUsuario(usuario);
        pedido.setNombre(registro.getNombre());
        pedido.setCedula(registro.getCedula());
        // Si quieres, asigna otros datos del registro que el pedido requiera

        // Validaciones y lógica original para cantidades y stock
        if (pedido.getFecha() == null) {
            prepararVistaConError(pedido, "La fecha del pedido es obligatoria.", model);
            model.addAttribute("cliente", registro);
            return "pedidos";
        }

        boolean alMenosUnPlatoSeleccionado = false;

        if (verificarSeleccion(pedido.getCevicheBlanco(), pedido.getCantCevBlanco())) alMenosUnPlatoSeleccionado = true;
        if (verificarSeleccion(pedido.getCevicheRojo(), pedido.getCantCevRojo())) alMenosUnPlatoSeleccionado = true;
        if (verificarSeleccion(pedido.getCevicheCamaron(), pedido.getCantCevCamaron())) alMenosUnPlatoSeleccionado = true;
        if (verificarSeleccion(pedido.getEncebollado(), pedido.getCantEncebollado())) alMenosUnPlatoSeleccionado = true;
        if (verificarSeleccion(pedido.getBolloPescado(), pedido.getCantBolloPescado())) alMenosUnPlatoSeleccionado = true;
        if (verificarSeleccion(pedido.getBolloPollo(), pedido.getCantBolloPollo())) alMenosUnPlatoSeleccionado = true;
        if (verificarSeleccion(pedido.getBolloCamaron(), pedido.getCantBolloCamaron())) alMenosUnPlatoSeleccionado = true;
        if (verificarSeleccion(pedido.getChifle(), pedido.getCantChifle())) alMenosUnPlatoSeleccionado = true;

        if (!alMenosUnPlatoSeleccionado) {
            prepararVistaConError(pedido, "Debe seleccionar al menos un plato y su cantidad.", model);
            model.addAttribute("cliente", registro);
            return "pedidos";
        }

        if (!verificarStock("Ceviche Blanco", pedido.getCevicheBlanco(), pedido.getCantCevBlanco(), model, pedido)) {
            model.addAttribute("cliente", registro);
            return "pedidos";
        }
        if (!verificarStock("Ceviche Rojo", pedido.getCevicheRojo(), pedido.getCantCevRojo(), model, pedido)) {
            model.addAttribute("cliente", registro);
            return "pedidos";
        }
        if (!verificarStock("Ceviche Camaron", pedido.getCevicheCamaron(), pedido.getCantCevCamaron(), model, pedido)) {
            model.addAttribute("cliente", registro);
            return "pedidos";
        }
        if (!verificarStock("Encebollado", pedido.getEncebollado(), pedido.getCantEncebollado(), model, pedido)) {
            model.addAttribute("cliente", registro);
            return "pedidos";
        }
        if (!verificarStock("Bollo Pescado", pedido.getBolloPescado(), pedido.getCantBolloPescado(), model, pedido)) {
            model.addAttribute("cliente", registro);
            return "pedidos";
        }
        if (!verificarStock("Bollo Pollo", pedido.getBolloPollo(), pedido.getCantBolloPollo(), model, pedido)) {
            model.addAttribute("cliente", registro);
            return "pedidos";
        }
        if (!verificarStock("Bollo Camaron", pedido.getBolloCamaron(), pedido.getCantBolloCamaron(), model, pedido)) {
            model.addAttribute("cliente", registro);
            return "pedidos";
        }
        if (!verificarStock("Chifle", pedido.getBolloCamaron(), pedido.getCantBolloCamaron(), model, pedido)) {
            model.addAttribute("cliente", registro);
            return "pedidos";
        }

        ajustarCantidadesNoSeleccionadas(pedido);
        pedidoRepo.save(pedido);

        redirectAttributes.addFlashAttribute("mensaje", "Pedido realizado con exito. Su código es: " + pedido.getIdPedido());

        return "redirect:/recibo/" + pedido.getIdPedido();
    }

    @GetMapping("/recibo/pdf/{id}")
    public void descargarReciboPDF(@PathVariable Integer id, HttpServletResponse response) throws Exception {
        Pedido pedido = pedidoRepo.findById(id).orElse(null);
        if (pedido == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Pedido no encontrado");
            return;
        }

        double total = calcularTotalPedido(pedido);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=recibo_" + id + ".pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Logo
        try {
            String logoPath = "src/main/resources/static/images/logoOriginal.png";
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(120, 120);
            logo.setAlignment(Image.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            // Logo opcional
        }

        // Título
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(6, 39, 78)); // #06274e
        Paragraph titulo = new Paragraph("RECIBO DE PEDIDO", titleFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20f);
        document.add(titulo);

        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
        document.add(new Paragraph("ID Pedido: " + pedido.getIdPedido(), normalFont));
        document.add(new Paragraph("Cliente: " + pedido.getNombre(), normalFont));
        document.add(new Paragraph("Cédula: " + pedido.getCedula(), normalFont));
        document.add(new Paragraph("Fecha: " + pedido.getFecha(), normalFont));
        document.add(new Paragraph(" "));

        // Tabla con productos
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15f);
        table.setSpacingAfter(15f);
        table.setWidths(new int[]{5, 2, 3});

        BaseColor azulOscuro = new BaseColor(6, 39, 78);
        BaseColor grisClaro = new BaseColor(230, 230, 230);

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 11);

        PdfPCell header1 = new PdfPCell(new Phrase("Producto", headerFont));
        header1.setBackgroundColor(azulOscuro);
        header1.setHorizontalAlignment(Element.ALIGN_CENTER);
        header1.setPadding(8f);

        PdfPCell header2 = new PdfPCell(new Phrase("Cantidad", headerFont));
        header2.setBackgroundColor(azulOscuro);
        header2.setHorizontalAlignment(Element.ALIGN_CENTER);
        header2.setPadding(8f);

        PdfPCell header3 = new PdfPCell(new Phrase("Subtotal", headerFont));
        header3.setBackgroundColor(azulOscuro);
        header3.setHorizontalAlignment(Element.ALIGN_CENTER);
        header3.setPadding(8f);

        table.addCell(header1);
        table.addCell(header2);
        table.addCell(header3);

        agregarFilaSiAplica(table, "Ceviche Blanco", pedido.getCantCevBlanco(), obtenerPrecioProducto("Ceviche Blanco"), cellFont, grisClaro);
        agregarFilaSiAplica(table, "Ceviche Rojo", pedido.getCantCevRojo(), obtenerPrecioProducto("Ceviche Rojo"), cellFont, BaseColor.WHITE);
        agregarFilaSiAplica(table, "Ceviche Camarón", pedido.getCantCevCamaron(), obtenerPrecioProducto("Ceviche Camaron"), cellFont, grisClaro);
        agregarFilaSiAplica(table, "Encebollado", pedido.getCantEncebollado(), obtenerPrecioProducto("Encebollado"), cellFont, BaseColor.WHITE);
        agregarFilaSiAplica(table, "Bollo de Pescado", pedido.getCantBolloPescado(), obtenerPrecioProducto("Bollo Pescado"), cellFont, grisClaro);
        agregarFilaSiAplica(table, "Bollo de Pollo", pedido.getCantBolloPollo(), obtenerPrecioProducto("Bollo Pollo"), cellFont, BaseColor.WHITE);
        agregarFilaSiAplica(table, "Bollo de Camarón", pedido.getCantBolloCamaron(), obtenerPrecioProducto("Bollo Camaron"), cellFont, grisClaro);
        agregarFilaSiAplica(table, "Chifle", pedido.getCantChifle(), obtenerPrecioProducto("Chifle"), cellFont, BaseColor.WHITE);

        document.add(table);

        // Total
        Font totalFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph totalText = new Paragraph("Total a pagar: $" + String.format("%.2f", total), totalFont);
        totalText.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalText);

        // Firma
        Font firmaFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC, BaseColor.DARK_GRAY);
        Paragraph firma = new Paragraph("\n\nFirma: ______________________", firmaFont);
        firma.setSpacingBefore(40f);
        document.add(firma);

        // Código QR HACER CUANDO ESTE EN LINEA EL PROYECTO
       // BarcodeQRCode qrCode = new BarcodeQRCode("http://localhost:8080/recibo/" + pedido.getIdPedido(), 150, 150, null);
       // Image qrImage = qrCode.getImage();
      //  qrImage.setAlignment(Image.ALIGN_CENTER);
       // qrImage.scaleToFit(120, 120);
      //  qrImage.setSpacingBefore(20f);
      //  document.add(qrImage);

        document.close();
    }



    private void agregarFilaSiAplica(PdfPTable table, String nombre, int cantidad, double precio, Font font, BaseColor background) {
        if (cantidad > 0) {
            PdfPCell prod = new PdfPCell(new Phrase(nombre, font));
            PdfPCell cant = new PdfPCell(new Phrase(String.valueOf(cantidad), font));
            PdfPCell sub = new PdfPCell(new Phrase("$" + String.format("%.2f", cantidad * precio), font));

            for (PdfPCell cell : new PdfPCell[]{prod, cant, sub}) {
                cell.setBackgroundColor(background);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(6f);
            }

            table.addCell(prod);
            table.addCell(cant);
            table.addCell(sub);
        }
    }




    private boolean verificarStock(String producto, String opcion, Integer cantidad, Model model, Pedido pedido) {
        if ("SI".equalsIgnoreCase(opcion) && cantidad != null && cantidad > 0) {
            Stock stock = stockRepo.findByNombreProductoIgnoreCase(producto);
            if (stock == null || stock.getCantidad() < cantidad) {
                prepararVistaConError(pedido, "No hay suficiente stock de " + producto + ".", model);
                return false;
            }
            stock.setCantidad(stock.getCantidad() - cantidad);
            stockRepo.save(stock);
        }
        return true;
    }

    private void prepararVistaConError(Pedido pedido, String mensajeError, Model model) {
        model.addAttribute("error", mensajeError);
        model.addAttribute("pedido", pedido);
        Registro cliente = registroRepo.findByCedula(pedido.getCedula());
        model.addAttribute("cliente", cliente != null ? cliente : new Registro());
    }

    private boolean verificarSeleccion(String opcion, Integer cantidad) {
        return "SI".equalsIgnoreCase(opcion) && cantidad != null && cantidad > 0;
    }

    private void ajustarCantidadesNoSeleccionadas(Pedido pedido) {
        if (!"SI".equalsIgnoreCase(pedido.getCevicheBlanco())) pedido.setCantCevBlanco(0);
        if (!"SI".equalsIgnoreCase(pedido.getCevicheRojo())) pedido.setCantCevRojo(0);
        if (!"SI".equalsIgnoreCase(pedido.getCevicheCamaron())) pedido.setCantCevCamaron(0);
        if (!"SI".equalsIgnoreCase(pedido.getEncebollado())) pedido.setCantEncebollado(0);
        if (!"SI".equalsIgnoreCase(pedido.getBolloPescado())) pedido.setCantBolloPescado(0);
        if (!"SI".equalsIgnoreCase(pedido.getBolloPollo())) pedido.setCantBolloPollo(0);
        if (!"SI".equalsIgnoreCase(pedido.getBolloCamaron())) pedido.setCantBolloCamaron(0);
    }

    @GetMapping("/recibo/{id}")
    public String verRecibo(@PathVariable Integer id, Model model) {
        Pedido pedido = pedidoRepo.findById(id).orElse(null);
        if (pedido == null) {
            model.addAttribute("error", "Pedido no encontrado");
            return "buscar_pedido"; // o una vista de error que tengas
        }

        double total = calcularTotalPedido(pedido);

        model.addAttribute("pedido", pedido);
        model.addAttribute("total", total);

        return "recibo_pedido";
    }

    private Double calcularTotalPedido(Pedido pedido) {
        double total = 0.0;

        if ("SI".equalsIgnoreCase(pedido.getCevicheBlanco()))
            total += obtenerPrecioProducto("Ceviche Blanco") * pedido.getCantCevBlanco();

        if ("SI".equalsIgnoreCase(pedido.getCevicheRojo()))
            total += obtenerPrecioProducto("Ceviche Rojo") * pedido.getCantCevRojo();

        if ("SI".equalsIgnoreCase(pedido.getCevicheCamaron()))
            total += obtenerPrecioProducto("Ceviche Camaron") * pedido.getCantCevCamaron();

        if ("SI".equalsIgnoreCase(pedido.getEncebollado()))
            total += obtenerPrecioProducto("Encebollado") * pedido.getCantEncebollado();

        if ("SI".equalsIgnoreCase(pedido.getBolloPescado()))
            total += obtenerPrecioProducto("Bollo Pescado") * pedido.getCantBolloPescado();

        if ("SI".equalsIgnoreCase(pedido.getBolloPollo()))
            total += obtenerPrecioProducto("Bollo Pollo") * pedido.getCantBolloPollo();

        if ("SI".equalsIgnoreCase(pedido.getBolloCamaron()))
            total += obtenerPrecioProducto("Bollo Camaron") * pedido.getCantBolloCamaron();

        if ("SI".equalsIgnoreCase(pedido.getChifle()))
            total += obtenerPrecioProducto("Chifle") * pedido.getCantChifle();

        return total;
    }

    private Double obtenerPrecioProducto(String nombreProducto) {
        Stock stock = stockRepo.findByNombreProductoIgnoreCase(nombreProducto);
        return (stock != null) ? stock.getPrecio() : 0.0;
    }

    @GetMapping("/historial")
    public String mostrarHistorial(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Pedido> pedidos = pedidoRepo.findByUsuario(usuario);
        Map<Integer, Double> totales = new HashMap<>();

        for (Pedido p : pedidos) {
            totales.put(p.getIdPedido(), calcularTotalPedido(p));
        }

        if (pedidos.isEmpty()) {
            model.addAttribute("mensaje", "No se encontraron pedidos para el usuario.");
        }

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("totales", totales);

        return "historial_pedidos"; // misma vista para mostrar
    }

    // Ya no es necesario el POST /historial porque la búsqueda se hace con el usuario autenticado.
// Opcionalmente, puedes eliminarlo o redireccionar al GET /historial.
    @PostMapping("/historial")
    public String postHistorial() {
        return "redirect:/historial";
    }

    @GetMapping("/pedidosrealizados")
    public String verPedidosRealizados(Model model) {
        List<Pedido> listaPedidos = pedidoRepo.findAll();
        model.addAttribute("pedidos", listaPedidos);
        return "pedidosrealizados";  // sin extensión .html y en minúsculas
    }




}


