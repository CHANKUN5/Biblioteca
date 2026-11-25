package pe.senati.config;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import pe.senati.entity.Libro;
import pe.senati.entity.Prestamo;
import pe.senati.entity.Usuario;
import pe.senati.repository.LibroRepository;
import pe.senati.repository.PrestamoRepository;
import pe.senati.repository.UsuarioRepository;

@Component
public class DatosIniciales implements ApplicationRunner
{
    private static final Logger log = LoggerFactory.getLogger(DatosIniciales.class);

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Override
    public void run(ApplicationArguments args)
    {
        try
        {
            asegurarUsuario("Estudiante1", "Kassandra Castro Huauya", "kassandra@universidad.edu", true);
            asegurarUsuario("Estudiante2", "Diego Saravia Duran", "diego@universidad.edu", false);
            asegurarUsuario("20210003", "Ana Quispe Flores", "ana@universidad.edu", true);
            asegurarUsuario("20210004", "Luis Mendoza Rios", "luis@universidad.edu", true);
            asegurarUsuario("20210005", "Maria Delgado Paz", "maria@universidad.edu", true);

            asegurarLibro("9780451524935", "1984", "George Orwell", 4);
            asegurarLibro("9780307474728", "Cien años de soledad", "Gabriel García Márquez", 2);
            asegurarLibro("9788420471839", "El principito", "Antoine de Saint-Exupéry", 1);
            asegurarLibro("9780140449136", "Crimen y castigo", "Fiódor Dostoievski", 3);
            asegurarLibro("9788437604947", "Rayuela", "Julio Cortazar", 3);

            asegurarPrestamos();
        }
        catch (RuntimeException ex)
        {
            log.warn("No se pudieron completar los datos iniciales. La aplicacion sigue en linea.", ex);
        }
    }

    private void asegurarUsuario(String codigo, String nombre, String correo, boolean activo)
    {
        Usuario usuario = usuarioRepository.findByCodigo(codigo).orElse(null);
        if (usuario == null)
        {
            usuario = usuarioRepository.findByCorreo(correo).orElse(null);
        }
        if (usuario == null)
        {
            usuario = new Usuario();
            usuario.setCodigo(codigo);
            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            usuario.setActivo(activo);
            usuario.setRol(Roles.ESTUDIANTE);
            usuarioRepository.save(usuario);
            return;
        }
        usuario.setCodigo(codigo);
        usuario.setNombre(nombre);
        usuario.setActivo(activo);
        if (usuario.getRol() == null || usuario.getRol().isBlank())
        {
            usuario.setRol(Roles.ESTUDIANTE);
        }
        usuarioRepository.save(usuario);
    }

    private void asegurarLibro(String isbn, String titulo, String autor, int ejemplares)
    {
        if (libroRepository.findByIsbn(isbn).isPresent())
        {
            return;
        }
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEjemplares(ejemplares);
        libroRepository.save(libro);
    }

    private void asegurarPrestamos()
    {
        String[] codigos = { "20210003", "20210004", "20210005", "Estudiante1" };
        int guardia = 0;
        while (prestamoRepository.count() < 5 && guardia < 12)
        {
            guardia++;
            if (!crearUnPrestamo(codigos))
            {
                return;
            }
        }
    }

    private boolean crearUnPrestamo(String[] codigos)
    {
        for (String codigo : codigos)
        {
            Usuario usuario = usuarioRepository.findByCodigo(codigo).orElse(null);
            if (usuario == null || !Boolean.TRUE.equals(usuario.getActivo()))
            {
                continue;
            }
            if (prestamoRepository.contarSinDevolver(usuario.getUsuario_id()) >= 3)
            {
                continue;
            }
            for (Libro libro : libroRepository.findAll())
            {
                Integer disponibles = libroRepository.ejemplaresDisponibles(libro.getLibro_id());
                if (disponibles == null || disponibles <= 0)
                {
                    continue;
                }
                Prestamo prestamo = new Prestamo();
                prestamo.setUsuario(usuario);
                prestamo.setLibro(libro);
                prestamo.setFechaPrestamo(LocalDate.now().minusDays(10));
                prestamo.setFechaDevolucion(LocalDate.now().plusDays(7));
                prestamoRepository.save(prestamo);
                return true;
            }
        }
        return false;
    }
}
