package pe.senati.entity;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="usuarios")
public class Usuario
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer usuario_id;

    @Column(unique=true, nullable=false)
    private String codigo;

    @Column(nullable=false)
    private String nombre;

    @Column(unique=true)
    private String correo;

    @Column(nullable=false)
    private Boolean activo = true;

    // BIBLIOTECARIO administra las pantallas. ESTUDIANTE solo consulta las suyas.
    @Column(length=20)
    private String rol = "ESTUDIANTE";

    // 1:N. La llave usuario_id vive en Prestamo.
    @JsonIgnore
    @OneToMany(mappedBy="usuario")
    private Collection<Prestamo> itemsPrestamo = new ArrayList<>();

    public Usuario() {}

    public Usuario(Integer usuario_id, String codigo, String nombre, String correo, Boolean activo) {
        this.usuario_id = usuario_id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.correo = correo;
        this.activo = activo;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getRol() {
        return rol == null || rol.isBlank() ? "ESTUDIANTE" : rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Collection<Prestamo> getItemsPrestamo() {
        return itemsPrestamo;
    }

    public void setItemsPrestamo(Collection<Prestamo> itemsPrestamo) {
        this.itemsPrestamo = itemsPrestamo;
    }
}
