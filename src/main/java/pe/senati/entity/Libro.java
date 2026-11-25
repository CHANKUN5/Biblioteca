package pe.senati.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name="libros")
public class Libro
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer libro_id;

    @Column(unique=true, nullable=false, length=13)
    private String isbn;

    @Column(nullable=false)
    private String titulo;

    @Column(nullable=false)
    private String autor;

    @Column(nullable=false)
    private Integer ejemplares;

    // 1:N. La llave libro_id vive en Prestamo, no aqui.
    @JsonIgnore
    @OneToMany(mappedBy="libro")
    private Collection<Prestamo> itemsPrestamo = new ArrayList<>();

    public Libro() {}

    public Libro(Integer libro_id, String isbn, String titulo, String autor, Integer ejemplares) {
        this.libro_id = libro_id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.ejemplares = ejemplares;
    }

    public Integer getLibro_id() {
        return libro_id;
    }

    public void setLibro_id(Integer libro_id) {
        this.libro_id = libro_id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(Integer ejemplares) {
        this.ejemplares = ejemplares;
    }

    public Collection<Prestamo> getItemsPrestamo() {
        return itemsPrestamo;
    }

    public void setItemsPrestamo(Collection<Prestamo> itemsPrestamo) {
        this.itemsPrestamo = itemsPrestamo;
    }
}
