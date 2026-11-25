package pe.senati.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.senati.entity.Libro;
import pe.senati.entity.Prestamo;
import pe.senati.entity.Usuario;
import pe.senati.repository.LibroRepository;
import pe.senati.repository.PrestamoRepository;
import pe.senati.repository.UsuarioRepository;

@Service
public class PrestamoServiceImpl implements PrestamoService
{
    @Autowired
    private PrestamoRepository repository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void insert(Prestamo prestamo)
    {
        if (prestamo.getUsuario() == null || prestamo.getUsuario().getUsuario_id() == null)
        {
            throw new IllegalStateException("El préstamo necesita un usuario.");
        }
        if (prestamo.getLibro() == null || prestamo.getLibro().getLibro_id() == null)
        {
            throw new IllegalStateException("El préstamo necesita un libro.");
        }

        long sinDevolver = repository.contarSinDevolver(prestamo.getUsuario().getUsuario_id());
        if (sinDevolver >= 3)
        {
            throw new IllegalStateException("No puede llevar otro libro: tiene 3 sin devolver.");
        }

        Integer disponibles = libroRepository.ejemplaresDisponibles(prestamo.getLibro().getLibro_id());
        if (disponibles == null || disponibles <= 0)
        {
            throw new IllegalStateException("Ese libro no tiene ejemplares disponibles.");
        }

        Usuario usuario = usuarioRepository.findById(prestamo.getUsuario().getUsuario_id()).orElse(null);
        Libro libro = libroRepository.findById(prestamo.getLibro().getLibro_id()).orElse(null);
        if (usuario == null || libro == null)
        {
            throw new IllegalStateException("El usuario o el libro no existe.");
        }
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        repository.save(prestamo);
    }

    @Override
    @Transactional
    public void update(Prestamo prestamo)
    {
        repository.save(prestamo);
    }

    @Override
    @Transactional
    public void devolver(Integer prestamo_id)
    {
        Prestamo prestamo = repository.findById(prestamo_id).orElse(null);
        if (prestamo == null || prestamo.getFechaEntrega() != null)
        {
            return;
        }
        prestamo.setFechaEntrega(LocalDate.now());
        repository.save(prestamo);
    }

    @Override
    @Transactional
    public void delete(Integer prestamo_id)
    {
        repository.deleteById(prestamo_id);
    }

    @Override
    @Transactional(readOnly=true)
    public Prestamo findById(Integer prestamo_id)
    {
        return repository.findById(prestamo_id).orElse(null);
    }

    @Override
    @Transactional(readOnly=true)
    public Collection<Prestamo> findAll()
    {
        return repository.findAllConDetalle();
    }

    @Override
    @Transactional(readOnly=true)
    public List<Object[]> librosMasPrestadosDelUltimoMes()
    {
        LocalDate desde = LocalDate.now().minusMonths(1);
        return repository.librosMasPrestadosDesde(desde);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Object[]> usuariosConMora()
    {
        return repository.usuariosConMora(LocalDate.now());
    }

    @Override
    @Transactional(readOnly=true)
    public long contarSinDevolver(Integer usuario_id)
    {
        return repository.contarSinDevolver(usuario_id);
    }
}
