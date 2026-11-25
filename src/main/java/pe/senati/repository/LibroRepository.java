package pe.senati.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.QueryHint;
import pe.senati.entity.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer>
{
    Optional<Libro> findByIsbn(String isbn);

    @Query("""
            SELECT l FROM Libro l
            WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
            ORDER BY l.titulo
            """)
    @QueryHints(@QueryHint(name="org.hibernate.cacheable", value="true"))
    List<Libro> buscarPorTitulo(@Param("texto") String texto);

    @Query("""
            SELECT l.ejemplares - (
                SELECT COUNT(p) FROM Prestamo p
                WHERE p.libro = l AND p.fechaEntrega IS NULL
            )
            FROM Libro l
            WHERE l.libro_id = :libroId
            """)
    Integer ejemplaresDisponibles(@Param("libroId") Integer libroId);
}
