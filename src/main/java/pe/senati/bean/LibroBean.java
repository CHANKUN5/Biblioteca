package pe.senati.bean;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import pe.senati.config.Roles;
import pe.senati.entity.Libro;
import pe.senati.service.LibroService;

@Component("libroBean")
@RequestScope
public class LibroBean
{
    @Autowired
    private LibroService libroService;

    private String isbn;
    private String titulo;
    private String autor;
    private Integer ejemplares;
    private String mensaje;
    private boolean ok;

    public boolean isBibliotecario()
    {
        return Roles.esBibliotecario();
    }

    public void registrar()
    {
        ok = false;
        if (!Roles.esBibliotecario())
        {
            mensaje = "Solo el bibliotecario registra libros.";
            return;
        }
        if (ejemplares == null || ejemplares < 1)
        {
            mensaje = "La cantidad de ejemplares tiene que ser al menos 1.";
            return;
        }
        Libro libro = new Libro();
        libro.setIsbn(isbn == null ? null : isbn.replace("-", "").replace(" ", ""));
        libro.setTitulo(titulo == null ? null : titulo.trim());
        libro.setAutor(autor == null ? null : autor.trim());
        libro.setEjemplares(ejemplares);
        try
        {
            libroService.insert(libro);
            mensaje = "Libro registrado.";
            ok = true;
            isbn = null;
            titulo = null;
            autor = null;
            ejemplares = null;
        }
        catch (DataIntegrityViolationException ex)
        {
            mensaje = "Ese ISBN ya está registrado.";
        }
    }

    public Collection<Libro> getLibros()
    {
        return libroService.findAll();
    }

    public String getIsbn()
    {
        return isbn;
    }

    public void setIsbn(String isbn)
    {
        this.isbn = isbn;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public String getAutor()
    {
        return autor;
    }

    public void setAutor(String autor)
    {
        this.autor = autor;
    }

    public Integer getEjemplares()
    {
        return ejemplares;
    }

    public void setEjemplares(Integer ejemplares)
    {
        this.ejemplares = ejemplares;
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
