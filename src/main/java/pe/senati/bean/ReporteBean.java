package pe.senati.bean;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import pe.senati.service.PrestamoService;

@Component("reporteBean")
@RequestScope
public class ReporteBean
{
    @Autowired
    private PrestamoService prestamoService;

    public List<Object[]> getMasPrestados()
    {
        return prestamoService.librosMasPrestadosDelUltimoMes();
    }

    public List<Object[]> getMora()
    {
        return prestamoService.usuariosConMora();
    }
}
