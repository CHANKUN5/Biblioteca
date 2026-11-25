package pe.senati.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.faces.context.FacesContext;
import pe.senati.config.Roles;
import pe.senati.entity.Usuario;
import pe.senati.service.LdapAuthService;
import pe.senati.service.UsuarioService;

@Component("loginBean")
@RequestScope
public class LoginBean
{
    @Autowired
    private LdapAuthService ldapAuthService;

    @Autowired
    private UsuarioService usuarioService;

    private String uid;
    private String clave;
    private String mensaje;

    public void ingresar()
    {
        String usuario = uid == null ? "" : uid.trim();
        if (!ldapAuthService.autenticar(usuario, clave))
        {
            mensaje = "El LDAP de la universidad rechazó el usuario o la clave.";
            return;
        }
        Usuario registrado = usuarioService.findByCodigo(usuario);
        if (registrado != null && !Boolean.TRUE.equals(registrado.getActivo()))
        {
            mensaje = "El LDAP aceptó la clave, pero el estudiante no está activo.";
            return;
        }
        if (registrado == null && !"biblioteca".equals(usuario))
        {
            mensaje = "El LDAP aceptó la clave, pero ese código no está en la biblioteca.";
            return;
        }
        String rol = Roles.ESTUDIANTE;
        if ("biblioteca".equals(usuario) || (registrado != null && Roles.BIBLIOTECARIO.equals(registrado.getRol())))
        {
            rol = Roles.BIBLIOTECARIO;
        }
        try
        {
            FacesContext contexto = FacesContext.getCurrentInstance();
            contexto.getExternalContext().getSessionMap().put("uidLdap", usuario);
            contexto.getExternalContext().getSessionMap().put(Roles.CLAVE, rol);
            contexto.getExternalContext().redirect(contexto.getExternalContext().getRequestContextPath() + "/inicio.xhtml");
            contexto.responseComplete();
        }
        catch (Exception ex)
        {
            mensaje = "No se pudo abrir la sesión.";
        }
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getClave()
    {
        return clave;
    }

    public void setClave(String clave)
    {
        this.clave = clave;
    }

    public String getMensaje()
    {
        return mensaje;
    }
}
