package pe.senati.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pe.senati.config.Roles;
import pe.senati.service.PrestamoService;
import pe.senati.service.ReportePdf;

@Controller
public class ReporteController
{
    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/reportes/mas-prestados.pdf")
    public void masPrestados(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        if (!esBibliotecario(request, response))
        {
            return;
        }
        List<String[]> filas = new ArrayList<>();
        for (Object[] fila : prestamoService.librosMasPrestadosDelUltimoMes())
        {
            filas.add(new String[] { texto(fila, 0), texto(fila, 1) });
        }
        escribir(response, "libros-mas-prestados.pdf", "Libros más prestados en el último mes",
                new String[] { "Título", "Préstamos" }, filas);
    }

    @GetMapping("/reportes/mora.pdf")
    public void mora(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        if (!esBibliotecario(request, response))
        {
            return;
        }
        List<String[]> filas = new ArrayList<>();
        for (Object[] fila : prestamoService.usuariosConMora())
        {
            filas.add(new String[] { texto(fila, 0), texto(fila, 1), texto(fila, 2), texto(fila, 3) });
        }
        escribir(response, "usuarios-con-mora.pdf", "Usuarios con mora",
                new String[] { "Código", "Nombre", "Libro", "Fecha de devolución" }, filas);
    }

    @GetMapping("/salir")
    public void salir(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        if (request.getSession(false) != null)
        {
            request.getSession(false).invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login.xhtml");
    }

    private boolean esBibliotecario(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        HttpSession sesion = request.getSession(false);
        if (sesion != null && Roles.BIBLIOTECARIO.equals(sesion.getAttribute(Roles.CLAVE)))
        {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/inicio.xhtml");
        return false;
    }

    private void escribir(HttpServletResponse response, String archivo, String titulo, String[] columnas, List<String[]> filas)
            throws Exception
    {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + archivo);
        ReportePdf.escribir(response.getOutputStream(), titulo, columnas, filas);
    }

    private String texto(Object[] fila, int indice)
    {
        Object valor = fila[indice];
        return valor == null ? "" : valor.toString();
    }
}
