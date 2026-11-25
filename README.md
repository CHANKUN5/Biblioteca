# Biblioteca universitaria

Préstamos de la biblioteca de la Universidad Tecnológica XYZ. Caso de Gestores de Administración Web.

**Spring Boot 4.1 · Java 21 · MySQL · JSF (Mojarra) · JPA/Hibernate · LDAP · Ehcache**

Un usuario tiene muchos préstamos y un libro también. Las llaves `usuario_id` y `libro_id` están en `prestamos`. No hay tabla intermedia.

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

## Cómo levantarlo

Hace falta JDK 21 y MySQL en el puerto 3306. La base es `db_biblioteca_web`. No uses `db_biblioteca` de la práctica anterior.

```text
mvnw spring-boot:run
```

Abre `http://localhost:8096/senati/login.xhtml`.

La URL y el usuario tienen valor de aula si no defines la variable (`localhost` y `root`). La clave no va en el repositorio: defínela en `MYSQL_PASSWORD` antes de arrancar.

| Variable | Para qué |
| --- | --- |
| `MYSQL_URL` | JDBC. En Vercel apunta a un MySQL externo |
| `MYSQL_USER` | Usuario de esa base |
| `MYSQL_PASSWORD` | Clave de esa base |

Usuarios del LDAP de prueba:

| Usuario | Clave | Qué ve |
| --- | --- | --- |
| `biblioteca` | `123456` | Libros, usuarios, préstamos y reportes |
| `20210001` | `123456` | El catálogo y solo sus préstamos |
| `20210002` | `123456` | El LDAP acepta la clave y la pantalla lo rechaza porque está inactivo |

## Reglas que ya quedan en el código

- El ISBN tiene 13 dígitos. Lo revisa `IsbnValidator` en la pantalla de libros.
- Un usuario no lleva un cuarto libro si tiene 3 con `fechaEntrega` vacía. Lo revisa el validador de la pantalla y otra vez el servicio, antes de grabar.
- `fechaEntrega` vacía significa que el libro sigue prestado.
- La búsqueda por título de `Libro` entra a la caché de segundo nivel (Ehcache). `Prestamo` no se cachea.
- `PrestamoBean` es `@Named("prestamoBean")` y `@SessionScoped`. El servicio se pide con `SpringContext.get`.

## Despliegue en Vercel

Vercel corre un servidor HTTP cuando el repositorio trae un archivo llamado `Dockerfile.vercel`. Construye la imagen y la ejecuta en Fluid, con escalado automático. Spring Boot entra por ese archivo. No hace falta otro `vercel.json`.

La aplicación escucha la variable `PORT`. En la máquina, si `PORT` no existe, usa el 8096.

En el proyecto de Vercel hay que cargar estas variables. Los libros y los préstamos no pueden quedar dentro del contenedor: si la instancia se apaga, esa base se perdería.

| Variable | Valor |
| --- | --- |
| `MYSQL_URL` | JDBC de un MySQL externo |
| `MYSQL_USER` | Usuario de esa base |
| `MYSQL_PASSWORD` | Clave de esa base |

El LDAP de prueba arranca dentro del mismo contenedor.

La sesión del préstamo vive en la instancia que atendió el login. Conviene no repartir esa sesión entre varias instancias.

GraalVM puede bajar el tiempo de arranque. Este proyecto sale con el JDK 21 de clase.

Con la CLI ya autenticada y las tres variables cargadas:

```text
vercel link
vercel env add MYSQL_URL
vercel env add MYSQL_USER
vercel env add MYSQL_PASSWORD
vercel deploy
```

La pantalla de ingreso queda en `/senati/login.xhtml`.
