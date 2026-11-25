# Biblioteca universitaria

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![JSF](https://img.shields.io/badge/JSF-Mojarra-E34F26)](https://github.com/eclipse-ee4j/mojarra)
[![Hibernate](https://img.shields.io/badge/Hibernate-JPA-59666C?logo=hibernate&logoColor=white)](https://hibernate.org/)
[![MySQL](https://img.shields.io/badge/MySQL-local-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Render-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Vercel](https://img.shields.io/badge/Vercel-en%20l%C3%ADnea-000000?logo=vercel&logoColor=white)](https://biblioteca-rouge-eight.vercel.app/senati/login.xhtml)
[![Docker](https://img.shields.io/badge/Docker-contenedor-2496ED?logo=docker&logoColor=white)](https://docs.docker.com/)

Préstamos de la biblioteca de la Universidad Tecnológica XYZ. Caso de Gestores de Administración Web.

Sigue siendo **Spring Boot**. Vercel no cambia el framework: construye el mismo jar y lo ejecuta dentro de un contenedor.

**Enlace principal:** https://biblioteca-rouge-eight.vercel.app/senati/login.xhtml

## Qué ya corre

| Pantalla | Ruta |
| --- | --- |
| Ingreso LDAP | `/senati/login.xhtml` |
| Inicio | `/senati/inicio.xhtml` |
| Libros | `/senati/libros.xhtml` |
| Usuarios | `/senati/usuarios.xhtml` |
| Préstamos | `/senati/prestamo.xhtml` |
| Reportes | `/senati/reportes.xhtml` |

Consulta REST, sin sesión y sin datos del alumno:

```text
GET /senati/api/libros/1/disponibilidad
```

Responde si el libro tiene ejemplares. No devuelve nombre ni código.

Un usuario tiene muchos préstamos y un libro también. Las llaves `usuario_id` y `libro_id` están en `prestamos`. No hay tabla intermedia.

## Cómo levantarlo en la PC

Hace falta JDK 21 y MySQL en el puerto 3306. La base es `db_biblioteca_web`. No uses `db_biblioteca` de la práctica anterior.

```text
mvnw spring-boot:run
```

Abre `http://localhost:8096/senati/login.xhtml`.

Si no defines variables, la URL queda en `localhost` y el usuario en `root`. La clave no va en el repositorio: ponla en `MYSQL_PASSWORD` o en `config/application.properties`, que está fuera de git.

| Variable | Para qué |
| --- | --- |
| `MYSQL_URL` | JDBC. En la PC es MySQL. En Vercel es PostgreSQL |
| `MYSQL_USER` | Usuario de esa base |
| `MYSQL_PASSWORD` | Clave de esa base |
| `DDL_AUTO` | En la PC, `update`. En producción, `none` |
| `SHOW_SQL` | En la PC, `true`. En producción, `false` |

El controlador JDBC lo elige Spring a partir de la URL. No está fijo en MySQL.

Usuarios del LDAP de prueba:

| Usuario | Clave | Qué ve |
| --- | --- | --- |
| `biblioteca` | `123456` | Libros, usuarios, préstamos y reportes |
| `Estudiante1` | `123456` | El catálogo y solo sus préstamos |
| `Estudiante2` | `123456` | El LDAP acepta la clave y la pantalla lo rechaza porque está inactivo |

## Reglas que ya quedan en el código

- El ISBN tiene 13 dígitos. Lo revisa `IsbnValidator` en la pantalla de libros.
- Un usuario no lleva un cuarto libro si tiene 3 con `fechaEntrega` vacía. Lo revisa el validador de la pantalla y otra vez el servicio, antes de grabar.
- `fechaEntrega` vacía significa que el libro sigue prestado.
- La búsqueda por título de `Libro` entra a la caché de segundo nivel (Ehcache). `Prestamo` no se cachea.
- `PrestamoBean` es `@Named("prestamoBean")` y `@SessionScoped`. El servicio se pide con `SpringContext.get`.
- `SpringContext` lleva `@Lazy(false)`. Si no, el arranque perezoso deja el contexto en null y préstamos responde 500.

## Despliegue

La página pública es esta:

https://biblioteca-rouge-eight.vercel.app/senati/login.xhtml

Vercel no ejecuta un jar suelto. El archivo `Dockerfile.vercel` arma la imagen con Maven y Temurin 21, y el contenedor escucha la variable `PORT`. En la PC, si `PORT` no existe, sigue el 8096. El contexto es `/senati`.

La base de producción no es la MySQL de la computadora. Es PostgreSQL de Render, plan Free. Hibernate creó `libros`, `usuarios` y `prestamos` la primera vez. Después, en Production, `DDL_AUTO` quedó en `none` para que el arranque no vuelva a alterar tablas. Los datos de la PC no se copiaron solos.

Las variables de Production, sin poner la clave en git:

| Variable | Valor |
| --- | --- |
| `MYSQL_URL` | `jdbc:postgresql://HOST:5432/BASE?sslmode=require` |
| `MYSQL_USER` | Usuario de Render |
| `MYSQL_PASSWORD` | Clave de Render |
| `DDL_AUTO` | `none` |
| `SHOW_SQL` | `false` |

`HOST` y `BASE` salen de la External Database URL de Render. La URL interna de Render no sirve desde Vercel. El nombre de las variables sigue siendo `MYSQL_*` porque así las lee `application.properties`, aunque el motor sea PostgreSQL.

El LDAP de prueba arranca dentro del mismo contenedor. La primera visita puede tardar cerca de medio minuto: Vercel despierta el contenedor y Spring Boot abre el puerto.

La sesión del préstamo vive en la instancia que atendió el login. Conviene no repartir esa sesión entre varias instancias.

Con la CLI ya autenticada, el proyecto enlazado y las variables cargadas:

```text
vercel link
vercel env add MYSQL_URL production
vercel env add MYSQL_USER production
vercel env add MYSQL_PASSWORD production
vercel env add DDL_AUTO production
vercel env add SHOW_SQL production
vercel deploy --prod
```

El proyecto de Vercel tiene que quedar como contenedor (`Dockerfile.vercel`), no como sitio estático. Si el framework es otro, el despliegue termina en segundos y no construye la imagen.
