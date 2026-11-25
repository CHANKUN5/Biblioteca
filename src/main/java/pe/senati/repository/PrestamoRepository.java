package pe.senati.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.senati.entity.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer>
{
    // Trae usuario y libro en la misma consulta para pintarlos en la tabla.
    @Query("""
            SELECT p FROM Prestamo p
            JOIN FETCH p.usuario
            JOIN FETCH p.libro
            """)
    List<Prestamo> findAllConDetalle();

    @Query("""
            SELECT l.titulo, COUNT(p)
            FROM Prestamo p JOIN p.libro l
            WHERE p.fechaPrestamo >= :desde
            GROUP BY l.libro_id, l.titulo
            ORDER BY COUNT(p) DESC
            """)
    List<Object[]> librosMasPrestadosDesde(@Param("desde") LocalDate desde);

    @Query("""
            SELECT u.codigo, u.nombre, l.titulo, p.fechaDevolucion
            FROM Prestamo p JOIN p.usuario u JOIN p.libro l
            WHERE p.fechaEntrega IS NULL
              AND p.fechaDevolucion < :hoy
            ORDER BY p.fechaDevolucion
            """)
    List<Object[]> usuariosConMora(@Param("hoy") LocalDate hoy);

    @Query("""
            SELECT COUNT(p) FROM Prestamo p
            WHERE p.usuario.usuario_id = :usuarioId
              AND p.fechaEntrega IS NULL
            """)
    long contarSinDevolver(@Param("usuarioId") Integer usuarioId);
}
