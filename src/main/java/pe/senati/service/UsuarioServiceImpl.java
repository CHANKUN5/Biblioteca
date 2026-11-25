package pe.senati.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.senati.entity.Usuario;
import pe.senati.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService
{
    @Autowired
    private UsuarioRepository repository;

    @Override
    @Transactional
    public void insert(Usuario usuario)
    {
        repository.save(usuario);
    }

    @Override
    @Transactional
    public void update(Usuario usuario)
    {
        repository.save(usuario);
    }

    @Override
    @Transactional
    public void delete(Integer usuario_id)
    {
        repository.deleteById(usuario_id);
    }

    @Override
    @Transactional(readOnly=true)
    public Usuario findById(Integer usuario_id)
    {
        return repository.findById(usuario_id).orElse(null);
    }

    @Override
    @Transactional(readOnly=true)
    public Usuario findByCodigo(String codigo)
    {
        return repository.findByCodigo(codigo).orElse(null);
    }

    @Override
    @Transactional(readOnly=true)
    public Collection<Usuario> findAll()
    {
        return repository.findAll();
    }
}
