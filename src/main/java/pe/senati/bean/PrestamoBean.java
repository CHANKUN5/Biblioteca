package pe.senati.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import pe.senati.config.Roles;
import pe.senati.config.SpringContext;
import pe.senati.entity.Libro;
import pe.senati.entity.Prestamo;
import pe.senati.entity.Usuario;
import pe.senati.service.LibroService;
import pe.senati.service.PrestamoService;
import pe.senati.service.UsuarioService;

@Named("prestamoBean")
@SessionScoped
public class PrestamoBean implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Integer usuarioId;
    private Integer libroId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private String mensaje;
    private boolean ok;

    @PostConstruct
    public void iniciar()
    {
        fechaPrestamo = LocalDate.now();
        fechaDevolucion = LocalDate.now().plusDays(7);
    }

    public void limpiarMensaje()
    {
        mensaje = null;
        ok = false;
    }

    public boolean isBibliotecario()
    {
        return Roles.esBibliotecario();
    }

    public String registrar()
    {
        ok = false;
        if (!Roles.esBibliotecario())
        {
            mensaje = "Solo el bibliotecario registra préstamos.";
            return null;
        }
        if (fechaPrestamo == null || fechaDevolucion == null)
        {
            mensaje = "Indica la fecha de préstamo y la fecha de devolución.";
            return null;
        }
        if (fechaDevolucion.isBefore(fechaPrestamo))
        {
            mensaje = "La fecha de devolución no puede ser anterior al préstamo.";
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setUsuario_id(usuarioId);
        Libro libro = new Libro();
        libro.setLibro_id(libroId);
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaDevolucion(fechaDevolucion);
        try
        {
            SpringContext.get(PrestamoService.class).insert(prestamo);
            mensaje = "Préstamo registrado.";
            ok = true;
            usuarioId = null;
            libroId = null;
            return "prestamo?faces-redirect=true";
        }
        catch (IllegalStateException ex)
        {
            mensaje = ex.getMessage();
            return null;
        }
    }

    public String devolver(Integer prestamoId)
    {
        if (!Roles.esBibliotecario())
        {
            mensaje = "Solo el bibliotecario registra la devolución.";
            ok = false;
            return null;
        }
        SpringContext.get(PrestamoService.class).devolver(prestamoId);
        mensaje = "Devolución registrada.";
        ok = true;
        return "prestamo?faces-redirect=true";
    }

    public Collection<Prestamo> getPrestamos()
    {
        Collection<Prestamo> prestamos = SpringContext.get(PrestamoService.class).findAll();
        if (Roles.esBibliotecario())
        {
            return prestamos;
        }
        String uid = Roles.uid();
        return prestamos.stream()
                .filter(prestamo -> prestamo.getUsuario() != null && uid.equals(prestamo.getUsuario().getCodigo()))
                .toList();
    }

    public Collection<Usuario> getUsuarios()
    {
        return SpringContext.get(UsuarioService.class).findAll().stream()
                .filter(usuario -> Boolean.TRUE.equals(usuario.getActivo()))
                .toList();
    }

    public Collection<Libro> getLibros()
    {
        return SpringContext.get(LibroService.class).findAll();
    }

    public Integer getUsuarioId()
    {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId)
    {
        this.usuarioId = usuarioId;
    }

    public Integer getLibroId()
    {
        return libroId;
    }

    public void setLibroId(Integer libroId)
    {
        this.libroId = libroId;
    }

    public LocalDate getFechaPrestamo()
    {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo)
    {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion()
    {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion)
    {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getMensaje()
    {
        return mensaje;
    }

    public boolean isOk()
    {
        return ok;
    }
}
