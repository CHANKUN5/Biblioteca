package pe.senati.service;

import java.util.Collection;
import java.util.List;

import pe.senati.entity.Libro;

public interface LibroService
{
    public abstract void insert(Libro libro);
    public abstract void update(Libro libro);
    public abstract void delete(Integer libro_id);
    public abstract Libro findById(Integer libro_id);
    public abstract Collection<Libro> findAll();
    public abstract List<Libro> buscarPorTitulo(String texto);
    public abstract Integer ejemplaresDisponibles(Integer libro_id);
}
