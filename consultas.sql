-- Base: db_biblioteca_web
-- Las tablas aparecen al levantar la aplicacion (ddl-auto=update).

-- Libros mas prestados en el ultimo mes.
-- Es la misma idea de la consulta JPQL de la pregunta 4.
SELECT l.titulo, COUNT(p.prestamo_id) AS veces
FROM prestamos p
JOIN libros l ON l.libro_id = p.libro_id
WHERE p.fecha_prestamo >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)
GROUP BY l.libro_id, l.titulo
ORDER BY veces DESC;

-- Usuarios con mora: no devolvieron y la fecha ya paso.
SELECT u.codigo, u.nombre, l.titulo, p.fecha_devolucion
FROM prestamos p
JOIN usuarios u ON u.usuario_id = p.usuario_id
JOIN libros l ON l.libro_id = p.libro_id
WHERE p.fecha_entrega IS NULL
  AND p.fecha_devolucion < CURDATE();

-- Cuantos libros sigue debiendo un usuario. Sirve para el tope de 3.
SELECT COUNT(*) AS sin_devolver
FROM prestamos
WHERE usuario_id = 1
  AND fecha_entrega IS NULL;
