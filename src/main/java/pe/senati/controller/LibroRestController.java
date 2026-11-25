package pe.senati.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.senati.service.LibroService;

@RestController
@RequestMapping("/api/libros")
public class LibroRestController
{
    @Autowired
    private LibroService libroService;

    @GetMapping("/{id}/disponibilidad")
    public Map<String, Object> disponibilidad(@PathVariable Integer id)
    {
        Integer disponibles = libroService.ejemplaresDisponibles(id);
        int cantidad = disponibles == null ? 0 : disponibles;
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("libro_id", id);
        cuerpo.put("ejemplaresDisponibles", cantidad);
        cuerpo.put("disponible", cantidad > 0);
        return cuerpo;
    }
}
