package pe.senati.service;

import java.util.Collection;

import pe.senati.entity.Usuario;

public interface UsuarioService
{
    public abstract void insert(Usuario usuario);
    public abstract void update(Usuario usuario);
    public abstract void delete(Integer usuario_id);
    public abstract Usuario findById(Integer usuario_id);
    public abstract Usuario findByCodigo(String codigo);
    public abstract Collection<Usuario> findAll();
}
