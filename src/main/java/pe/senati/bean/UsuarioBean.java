package pe.senati.bean;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import pe.senati.config.Roles;
import pe.senati.entity.Usuario;
import pe.senati.service.UsuarioService;

@Component("usuarioBean")
@RequestScope
public class UsuarioBean
{
    @Autowired
    private UsuarioService usuarioService;

    private String codigo;
    private String nombre;
    private String correo;
    private Boolean activo = true;
    private String rol = Roles.ESTUDIANTE;
    private String mensaje;
    private boolean ok;

    public void registrar()
    {
        ok = false;
        if (!Roles.esBibliotecario())
        {
            mensaje = "Solo el bibliotecario registra usuarios.";
            return;
        }
        String rolElegido = Roles.BIBLIOTECARIO.equals(rol) ? Roles.BIBLIOTECARIO : Roles.ESTUDIANTE;
        Usuario usuario = new Usuario();
        usuario.setCodigo(codigo == null ? null : codigo.trim());
        usuario.setNombre(nombre == null ? null : nombre.trim());
        usuario.setCorreo(correo == null || correo.isBlank() ? null : correo.trim());
        usuario.setActivo(Boolean.TRUE.equals(activo));
        usuario.setRol(rolElegido);
        try
        {
            usuarioService.insert(usuario);
            mensaje = "Usuario registrado.";
            ok = true;
            codigo = null;
            nombre = null;
            correo = null;
            activo = true;
            rol = Roles.ESTUDIANTE;
        }
        catch (DataIntegrityViolationException ex)
        {
            mensaje = "Ese código o ese correo ya está registrado.";
        }
    }

    public Collection<Usuario> getUsuarios()
    {
        return usuarioService.findAll();
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getCorreo()
    {
        return correo;
    }

    public void setCorreo(String correo)
    {
        this.correo = correo;
    }

    public Boolean getActivo()
    {
        return activo;
    }

    public void setActivo(Boolean activo)
    {
        this.activo = activo;
    }

    public String getRol()
    {
        return rol;
    }

    public void setRol(String rol)
    {
        this.rol = rol;
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
