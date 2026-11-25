package pe.senati.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="prestamos")
public class Prestamo
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer prestamo_id;

    @DateTimeFormat(pattern="yyyy-MM-dd", iso=ISO.DATE)
    @Column(nullable=false)
    private LocalDate fechaPrestamo;

    @DateTimeFormat(pattern="yyyy-MM-dd", iso=ISO.DATE)
    @Column(nullable=false)
    private LocalDate fechaDevolucion;

    // Null significa que el libro sigue prestado.
    @DateTimeFormat(pattern="yyyy-MM-dd", iso=ISO.DATE)
    private LocalDate fechaEntrega;

    @ManyToOne
    @JoinColumn(name="libro_id", nullable=false)
    private Libro libro;

    @ManyToOne
    @JoinColumn(name="usuario_id", nullable=false)
    private Usuario usuario;

    public Prestamo() {}

    public Integer getPrestamo_id() {
        return prestamo_id;
    }

    public void setPrestamo_id(Integer prestamo_id) {
        this.prestamo_id = prestamo_id;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
