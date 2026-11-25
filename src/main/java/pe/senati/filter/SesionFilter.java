package pe.senati.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import pe.senati.config.Roles;

@Component
public class SesionFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {
        if (esPublico(request))
        {
            filterChain.doFilter(request, response);
            return;
        }
        HttpSession sesion = request.getSession(false);
        if (sesion != null && sesion.getAttribute("uidLdap") != null)
        {
            if (!Roles.BIBLIOTECARIO.equals(sesion.getAttribute(Roles.CLAVE)) && esDeBibliotecario(request.getRequestURI()))
            {
                response.sendRedirect(request.getContextPath() + "/inicio.xhtml");
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/login.xhtml");
    }

    private boolean esDeBibliotecario(String uri)
    {
        return uri.contains("usuarios.xhtml") || uri.contains("/reportes");
    }

    private boolean esPublico(HttpServletRequest request)
    {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        if (uri.equals(ctx) || uri.equals(ctx + "/") || uri.endsWith("/index.html"))
        {
            return true;
        }
        if (uri.contains("login.xhtml") || uri.contains("/api/"))
        {
            return true;
        }
        if (uri.contains("jakarta.faces.resource") || uri.contains("javax.faces.resource"))
        {
            return true;
        }
        return uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png")
                || uri.endsWith(".ico") || uri.endsWith(".svg");
    }
}
