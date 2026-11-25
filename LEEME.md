# AppBibliotecaWeb

Sistema del caso de Gestores de Administración Web. Cada función tiene su pantalla. Las capas son entidad, repositorio, servicio, bean y xhtml. Sigue en Spring Boot.

En línea: https://biblioteca-rouge-eight.vercel.app/senati/login.xhtml

## Cómo correrlo

1. MySQL encendido. La URL, el usuario y la clave salen de `MYSQL_URL`, `MYSQL_USER` y `MYSQL_PASSWORD`.
2. Ejecuta `AppBibliotecaWebApplication`.
3. Abre http://localhost:8096/senati/login.xhtml

Usuarios del LDAP:

- `biblioteca` / `123456` entra como bibliotecario. Ve libros, usuarios, préstamos y reportes.
- `Estudiante1` / `123456` entra como estudiante. Ve el catálogo y solo sus préstamos.
- `Estudiante2` / `123456` existe en el LDAP, pero está inactivo y la pantalla lo rechaza.

El rol queda en la sesión. `UsuarioBean` guarda el rol en la tabla. `LibroBean` y `PrestamoBean` cortan el registro si el rol no es bibliotecario.

## Capas

- `entity`: Libro, Usuario, Prestamo. Libro lleva `@Cacheable`.
- `repository`: JPA y las JPQL de reportes, mora, tope de 3 y ejemplares.
- `service`: reglas de préstamo, LDAP y el PDF.
- `bean`: una clase por pantalla. `UsuarioBean` administra usuarios y el rol. `PrestamoBean` usa `@Named` y `@SessionScoped`. `LibroBean` registra el catálogo.
- `validator`: ISBN de 13 dígitos y máximo 3 libros sin devolver.
- `controller`: REST de disponibilidad y descarga de los PDF.
- `filter`: deja pasar el login, el CSS y el REST. El resto pide sesión.

## Pantallas

- `login.xhtml` ingreso LDAP
- `inicio.xhtml` resumen y accesos
- `libros.xhtml` registro y catálogo
- `usuarios.xhtml` registro y listado
- `prestamo.xhtml` prestar y devolver
- `reportes.xhtml` tablas y PDF

REST, sin sesión: http://localhost:8096/senati/api/libros/1/disponibilidad
