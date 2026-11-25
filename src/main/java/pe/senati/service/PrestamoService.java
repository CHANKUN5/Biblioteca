package pe.senati.service;

import java.util.Collection;
import java.util.List;

import pe.senati.entity.Prestamo;

public interface PrestamoService
{
    public abstract void insert(Prestamo prestamo);
    public abstract void update(Prestamo prestamo);
    public abstract void devolver(Integer prestamo_id);
    public abstract void delete(Integer prestamo_id);
    public abstract Prestamo findById(Integer prestamo_id);
    public abstract Collection<Prestamo> findAll();

    public abstract List<Object[]> librosMasPrestadosDelUltimoMes();
    public abstract List<Object[]> usuariosConMora();
    public abstract long contarSinDevolver(Integer usuario_id);
}
