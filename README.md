# Biblioteca universitaria

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![JSF](https://img.shields.io/badge/JSF-Mojarra-E34F26)](https://github.com/eclipse-ee4j/mojarra)
[![Hibernate](https://img.shields.io/badge/Hibernate-JPA-59666C?logo=hibernate&logoColor=white)](https://hibernate.org/)
[![MySQL](https://img.shields.io/badge/MySQL-local-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Render-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Vercel](https://img.shields.io/badge/Vercel-en%20l%C3%ADnea-000000?logo=vercel&logoColor=white)](https://biblioteca-rouge-eight.vercel.app/senati/login.xhtml)

Sigue siendo Spring Boot. El enlace de la aplicación es https://biblioteca-rouge-eight.vercel.app/senati/login.xhtml

## Problema que resuelve

La biblioteca prestaba en papel: no se sabía cuántos ejemplares quedaban, un alumno podía llevar más libros de la cuenta y la mora se calculaba a mano. Este sistema deja el catálogo, los usuarios y los préstamos en tres tablas, valida el ingreso contra el LDAP de la universidad y separa lo que ve el bibliotecario de lo que ve el estudiante.

## Lógica del sistema

Un usuario tiene muchos préstamos y un libro también. No hay tabla intermedia. La relación vive en `prestamos`, con `usuario_id` y `libro_id`.

`fechaEntrega` vacía significa que el libro sigue prestado. Si además `fechaDevolucion` ya pasó, esa fila es mora. El reporte y el PDF `usuarios-con-mora.pdf` salen de la misma consulta.

Antes de guardar, el sistema corta el préstamo en dos sitios: el validador de la pantalla y el servicio. Las dos reglas son las mismas.

- Un usuario no lleva un cuarto libro si ya tiene 3 con `fechaEntrega` vacía.
- El libro tiene que tener ejemplares disponibles. Los prestados no se restan de la columna `ejemplares`: se cuentan los préstamos abiertos.
- El ISBN tiene 13 dígitos. Si no, el libro no se guarda.
- El bibliotecario registra libros, usuarios, préstamos y devoluciones, y descarga los PDF.
- El estudiante ve el catálogo y solo sus préstamos. Si el LDAP acepta la clave pero `activo` es falso, la pantalla lo rechaza.

La búsqueda de libros usa caché de segundo nivel. `Prestamo` no se cachea, porque cambia en cada préstamo y cada devolución.

## Casos de uso

![Casos de uso](docs/diagrama-casos-de-uso.png)

| Actor | Puede |
| --- | --- |
| Bibliotecario | Entrar, registrar libros y usuarios, prestar, devolver, ver reportes y bajar el PDF de mora |
| Estudiante | Entrar, ver el catálogo, ver sus préstamos y consultar la disponibilidad |

`Estudiante2` existe en el LDAP y está inactivo. Sirve para mostrar el rechazo.

## Entidades

![Diagrama de entidades](docs/diagrama-entidades.png)

### Usuario

Tabla `usuarios`. Identifica a quien presta.

| Campo | Para qué |
| --- | --- |
| `usuario_id` | Clave generada |
| `codigo` | Único. Es el mismo usuario del LDAP: `Estudiante1`, `biblioteca` no se guarda aquí |
| `nombre` | Nombre que sale en las tablas y en el PDF |
| `correo` | Único |
| `activo` | Si es falso, no entra aunque el LDAP acepte la clave |
| `rol` | `BIBLIOTECARIO` o `ESTUDIANTE` |

### Libro

Tabla `libros`. Es el catálogo. Lleva `@Cacheable`.

| Campo | Para qué |
| --- | --- |
| `libro_id` | Clave generada |
| `isbn` | Único, 13 dígitos |
| `titulo` | Lo que se busca y se presta |
| `autor` | Autor del libro |
| `ejemplares` | Cuántos hay en total, no cuántos quedan en estante |

### Prestamo

Tabla `prestamos`. Une usuario y libro. Muchos préstamos apuntan a un usuario y muchos apuntan a un libro.

| Campo | Para qué |
| --- | --- |
| `prestamo_id` | Clave generada |
| `fechaPrestamo` | Día en que salió |
| `fechaDevolucion` | Día pactado. Si es anterior a hoy y no hay entrega, hay mora |
| `fechaEntrega` | Día real de la devolución. Vacía mientras sigue prestado |
| `usuario_id` | Llave hacia `usuarios` |
| `libro_id` | Llave hacia `libros` |

## Cómo probarlo

| Usuario | Clave | Qué ve |
| --- | --- | --- |
| `biblioteca` | `123456` | Libros, usuarios, préstamos y reportes |
| `Estudiante1` | `123456` | El catálogo y solo sus préstamos |
| `Estudiante2` | `123456` | El LDAP acepta la clave y la pantalla lo rechaza |

En la PC: JDK 21, MySQL y la base `db_biblioteca_web`.

```text
mvnw spring-boot:run
```

http://localhost:8096/senati/login.xhtml

La clave de MySQL no va en el repositorio. Se define en `MYSQL_PASSWORD`.
