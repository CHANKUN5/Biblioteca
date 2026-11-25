package pe.senati.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import pe.senati.config.Roles;
import pe.senati.service.LibroService;
import pe.senati.service.PrestamoService;
import pe.senati.service.UsuarioService;

@Component("inicioBean")
@RequestScope
public class InicioBean
{
    @Autowired
    private LibroService libroService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PrestamoService prestamoService;

    public int getLibros()
    {
        return libroService.findAll().size();
    }

    public int getUsuarios()
    {
        return usuarioService.findAll().size();
    }

    public int getPrestamos()
    {
        return prestamoService.findAll().size();
    }

    public boolean isBibliotecario()
    {
        return Roles.esBibliotecario();
    }
}
