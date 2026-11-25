package pe.senati.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.senati.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>
{
    Optional<Usuario> findByCodigo(String codigo);

    Optional<Usuario> findByCorreo(String correo);
}
