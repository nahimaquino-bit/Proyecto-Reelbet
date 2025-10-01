package com.reelbet.model;

import javax.persistence.*;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "cedula", length = 20)
    private String cedula;

    @Column(name = "cevicheBlanco", length = 15)
    private String cevicheBlanco;

    @Column(name = "cantCevBlanco")
    private Integer cantCevBlanco = 0;

    @Column(name = "cevicheRojo", length = 15)
    private String cevicheRojo;

    @Column(name = "cantCevRojo")
    private Integer cantCevRojo = 0;

    @Column(name = "cevicheCamaron", length = 15)
    private String cevicheCamaron;

    @Column(name = "cantCevCamaron")
    private Integer cantCevCamaron = 0;

    @Column(name = "encebollado", length = 15)
    private String encebollado;

    @Column(name = "cantEncebollado")
    private Integer cantEncebollado = 0;

    @Column(name = "bolloPescado", length = 15)
    private String bolloPescado;

    @Column(name = "cantBolloPescado")
    private Integer cantBolloPescado = 0;

    @Column(name = "bolloPollo", length = 15)
    private String bolloPollo;

    @Column(name = "cantBolloPollo")
    private Integer cantBolloPollo = 0;

    @Column(name = "bolloCamaron", length = 15)
    private String bolloCamaron;

    @Column(name = "cantBolloCamaron")
    private Integer cantBolloCamaron = 0;

    @Column(name = "chifle", length = 15)
    private String chifle;

    @Column(name = "cantChifle")
    private Integer cantChifle = 0;

    @Column(name = "tostado", length = 15)
    private String tostado;

    @Column(name = "fecha")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    public Pedido() {}

    // Getters y setters para todos los campos

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCevicheBlanco() {
        return cevicheBlanco;
    }

    public void setCevicheBlanco(String cevicheBlanco) {
        this.cevicheBlanco = cevicheBlanco;
    }

    public Integer getCantCevBlanco() {
        return cantCevBlanco;
    }

    public void setCantCevBlanco(Integer cantCevBlanco) {
        this.cantCevBlanco = cantCevBlanco;
    }

    public String getCevicheRojo() {
        return cevicheRojo;
    }

    public void setCevicheRojo(String cevicheRojo) {
        this.cevicheRojo = cevicheRojo;
    }

    public Integer getCantCevRojo() {
        return cantCevRojo;
    }

    public void setCantCevRojo(Integer cantCevRojo) {
        this.cantCevRojo = cantCevRojo;
    }

    public String getCevicheCamaron() {
        return cevicheCamaron;
    }

    public void setCevicheCamaron(String cevicheCamaron) {
        this.cevicheCamaron = cevicheCamaron;
    }

    public Integer getCantCevCamaron() {
        return cantCevCamaron;
    }

    public void setCantCevCamaron(Integer cantCevCamaron) {
        this.cantCevCamaron = cantCevCamaron;
    }

    public String getEncebollado() {
        return encebollado;
    }

    public void setEncebollado(String encebollado) {
        this.encebollado = encebollado;
    }

    public Integer getCantEncebollado() {
        return cantEncebollado;
    }

    public void setCantEncebollado(Integer cantEncebollado) {
        this.cantEncebollado = cantEncebollado;
    }

    public String getBolloPescado() {
        return bolloPescado;
    }

    public void setBolloPescado(String bolloPescado) {
        this.bolloPescado = bolloPescado;
    }

    public Integer getCantBolloPescado() {
        return cantBolloPescado;
    }

    public void setCantBolloPescado(Integer cantBolloPescado) {
        this.cantBolloPescado = cantBolloPescado;
    }

    public String getBolloPollo() {
        return bolloPollo;
    }

    public void setBolloPollo(String bolloPollo) {
        this.bolloPollo = bolloPollo;
    }

    public Integer getCantBolloPollo() {
        return cantBolloPollo;
    }

    public void setCantBolloPollo(Integer cantBolloPollo) {
        this.cantBolloPollo = cantBolloPollo;
    }

    public String getBolloCamaron() {
        return bolloCamaron;
    }

    public void setBolloCamaron(String bolloCamaron) {
        this.bolloCamaron = bolloCamaron;
    }

    public Integer getCantBolloCamaron() {
        return cantBolloCamaron;
    }

    public void setCantBolloCamaron(Integer cantBolloCamaron) {
        this.cantBolloCamaron = cantBolloCamaron;
    }

    public String getChifle() {
        return chifle;
    }

    public void setChifle(String chifle) {
        this.chifle = chifle;
    }

    public Integer getCantChifle() {return cantChifle;}

    public void setCantChifle(Integer cantChifle) {this.cantChifle = cantChifle;}

    public String getTostado() {
        return tostado;
    }

    public void setTostado(String tostado) {
        this.tostado = tostado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
