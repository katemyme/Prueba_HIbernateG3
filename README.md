# Control de Equipos de Laboratorio

Aplicación de escritorio en **JavaFX** que usa **Hibernate (JPA)** para guardar los datos en **PostgreSQL**. Sirve para registrar, editar, eliminar, buscar y filtrar los equipos de un laboratorio. Cada equipo pertenece a un tipo de equipo, por ejemplo Computadora, Proyector, Redes o Periférico.

| | |
|---|---|
| **Estudiante** | Victor Mateo Alcocer Lopez |
| **Docente** | Freddy Luis Lopez Barrios |
| **Pruebas** | [Matriz de pruebas P1–P8](DOCS/Pruebas.MD) |

---

## 1. Requisitos de ejecución

| Requisito | Versión usada | Nota |
|---|---|---|
| JDK | 21 | El `pom.xml` compila para Java 21 |
| PostgreSQL | 18.4 | Servidor local en `localhost:5432`, usuario `postgres` |
| Maven | 3.8.5 (Maven Wrapper incluido) | No hace falta instalarlo: se usa `mvnw.cmd` / `mvnw` |
| IntelliJ IDEA | Opcional | Para abrir y ejecutar el proyecto desde el IDE |

La primera ejecución necesita conexión a internet, porque Maven descarga las dependencias:

| Dependencia | Versión |
|---|---|
| `org.openjfx:javafx-controls` y `javafx-fxml` | 21.0.6 |
| `org.hibernate.orm:hibernate-core` | 7.0.4.Final |
| `org.postgresql:postgresql` (driver JDBC) | 42.7.13 |

---

## 2. Configuración local de PostgreSQL

### 2.1 Crear la base de datos con el script

El script [`SQL/bdlaboratorioorm.sql`](SQL/bdlaboratorioorm.sql) crea todo lo necesario:

- la base `bdlaboratorioorm`;
- el esquema `catalogos`;
- las tablas `tipo_equipo` y `equipo`, con sus restricciones;
- los datos iniciales: 4 tipos y 10 equipos.

`CREATE DATABASE` y el resto del script se ejecutan en conexiones distintas: primero se crea la base y después hay que conectarse a ella. Puedes hacerlo de dos formas.

**Opción A: pgAdmin**

1. Abre el *Query Tool* sobre la base `postgres` y ejecuta:
   ```sql
   CREATE DATABASE bdlaboratorioorm;
   ```
2. Actualiza el árbol, abre el *Query Tool* sobre **`bdlaboratorioorm`**, pega el script desde `CREATE SCHEMA catalogos;` hasta el final y ejecútalo.

**Opción B: psql** (desde la carpeta del proyecto)

```
psql -U postgres -d postgres -c "CREATE DATABASE bdlaboratorioorm;"
psql -U postgres -d bdlaboratorioorm -f SQL/bdlaboratorioorm.sql
```

- En el segundo comando la primera línea muestra `ERROR: la base de datos «bdlaboratorioorm» ya existe`. Es normal, porque la base se creó en el primer comando; el resto del script se ejecuta igual.
- Si todo salió bien, después del error verás `CREATE SCHEMA`, `CREATE TABLE`, `CREATE TABLE`, `INSERT 0 4`, `INSERT 0 10` y las consultas de verificación.
- Si `psql` no se reconoce como comando, usa la ruta completa, por ejemplo `"C:\Program Files\PostgreSQL\18\bin\psql.exe"`.

Para volver a los datos iniciales, ejecuta `DROP DATABASE bdlaboratorioorm;` desde la base `postgres` y repite estos pasos.

### 2.2 Datos de conexión

La conexión está en [`src/main/resources/META-INF/persistence.xml`](src/main/resources/META-INF/persistence.xml):

| Propiedad | Valor |
|---|---|
| URL | `jdbc:postgresql://localhost:5432/bdlaboratorioorm` |
| Usuario | `postgres` |
| Contraseña | Variable de entorno **`DB_PASSWORD`** |
| `hibernate.hbm2ddl.auto` | `validate` |

- **Otro servidor, puerto o usuario:** si tu PostgreSQL usa otros datos, cambia la URL o el usuario en ese archivo.
- **Contraseña:** no se guarda en el proyecto. `JPAUtil` la lee de la variable de entorno `DB_PASSWORD` y la pasa al crear el `EntityManagerFactory`. Si la variable no está definida, la aplicación muestra *"La variable de entorno DB_PASSWORD está vacía"*.
- **Validación del esquema:** con `validate`, Hibernate comprueba al iniciar que las tablas coincidan con las entidades y no modifica el esquema.

---

## 3. Pasos para iniciar la aplicación

1. Crea la base de datos (sección 2.1).
2. Define la variable de entorno `DB_PASSWORD` con la contraseña del usuario `postgres`.
3. Ejecuta la aplicación con una de las dos opciones siguientes.

**Opción A: IntelliJ IDEA**

1. Abre la carpeta del proyecto y espera a que IntelliJ cargue Maven. Si el código aparece en rojo, pulsa **Load Maven Changes** (`Ctrl+Shift+O`).
2. Ve a **Run → Edit Configurations… → + → Application** y configura:
   - **Main class:** `ni.edu.uam.prueba_hibernateg3.Launcher`
   - **Environment variables:** `DB_PASSWORD=tu_contraseña`
3. Pulsa **Run**.

**Opción B: terminal** (desde la carpeta del proyecto)

PowerShell:
```powershell
$env:DB_PASSWORD = "tu_contraseña"
.\mvnw.cmd clean javafx:run
```

CMD:
```
set DB_PASSWORD=tu_contraseña
mvnw.cmd clean javafx:run
```

Se abre la ventana **Control de Equipos de Laboratorio** con la lista de equipos cargada desde PostgreSQL.

**Uso de la pantalla**

| Acción | Cómo se hace |
|---|---|
| Registrar | Completa el formulario y pulsa **Guardar** |
| Editar | Selecciona una fila de la tabla, cambia los datos y pulsa **Actualizar** |
| Eliminar | Selecciona una fila, pulsa **Eliminar** y confirma |
| Nuevo registro después de seleccionar | Pulsa **Limpiar** para vaciar el formulario |
| Buscar y filtrar | Escribe parte del nombre, elige un tipo (o ambos) y pulsa **Buscar / Aplicar filtros**; **Quitar filtros** vuelve a mostrar todo |

---

## 4. Estructura del proyecto

```
Prueba_HIbernateG3
├── pom.xml                         Dependencias y plugin javafx-maven-plugin
├── SQL/bdlaboratorioorm.sql        Script de la base de datos
├── DOCS/Pruebas.MD                 Matriz de pruebas P1–P8 con resultados y evidencias
├── DOCS/capturas/                  Capturas de la aplicación usadas como evidencia
└── src/main
    ├── java
    │   ├── module-info.java        Módulo ni.edu.uam.Prueba_HibernateG
    │   └── ni/edu/uam/prueba_hibernateg3
    │       ├── Launcher.java               Punto de entrada (main)
    │       ├── LaboratorioApplication.java Carga el FXML y abre la ventana
    │       ├── Controller/EquipoController.java  Eventos de la pantalla y validaciones
    │       ├── Dao/EquipoDao.java          CRUD y consultas de equipos
    │       ├── Dao/TipoEquipoDao.java      Consulta de tipos de equipo
    │       ├── Database/JPAUtil.java       Crea el EntityManagerFactory y los EntityManager
    │       └── Model/Equipo.java, TipoEquipo.java   Entidades JPA
    └── resources
        ├── META-INF/persistence.xml        Unidad de persistencia "bdlaboratorioorm"
        └── ni/edu/uam/prueba_hibernateg3/Laboratorio-view.fxml   Interfaz
```

El controller solo maneja la pantalla. Todo lo que usa `EntityManager` y transacciones está en los DAO.

---

## 5. Entidades

### `TipoEquipo` → tabla `catalogos.tipo_equipo`

| Atributo | Columna | Reglas |
|---|---|---|
| `id` (`Long`) | `id_tipo` | Clave primaria, `@GeneratedValue(strategy = IDENTITY)` |
| `nombre` | `nombre` | Obligatorio, único, máximo 100 caracteres |
| `descripcion` | `descripcion` | Opcional, máximo 255 caracteres |
| `equipos` (`List<Equipo>`) | — | Lado inverso de la asociación (`@OneToMany`) |

`toString()` devuelve el nombre, para que los `ComboBox` y la columna **Tipo** de la tabla muestren el nombre del tipo.

### `Equipo` → tabla `catalogos.equipo`

| Atributo | Columna | Reglas |
|---|---|---|
| `id` (`Long`) | `id_equipo` | Clave primaria, `IDENTITY` |
| `codigo` | `codigo` | Obligatorio, único (`equipo_codigo_key`), máximo 100 caracteres |
| `nombre` | `nombre` | Obligatorio, único (`equipo_nombre_key`), máximo 100 caracteres |
| `marca` | `marca` | Opcional |
| `estado` | `estado` | Obligatorio; solo `Disponible`, `En mantenimiento` o `Fuera de servicio` (`chk_equipo_estado`) |
| `cantidad` (`int`) | `cantidad` | Obligatorio, mayor o igual a 0 (`chk_equipo_cantidad`) |
| `tipoEquipo` (`TipoEquipo`) | `id_tipo` | Clave foránea hacia `tipo_equipo` (`fk_equipo_tipo`) |

El formulario aplica las mismas reglas antes de guardar: código y nombre obligatorios, cantidad entera y no negativa, y tipo y estado seleccionados. Si aun así PostgreSQL rechaza un dato, la transacción hace `rollback()` y se muestra un Alert con el motivo; por ejemplo, *"Ya existe un equipo con el código …"*.

---

## 6. Asociación `Equipo` ↔ `TipoEquipo`

Es una asociación **muchos a uno**: muchos equipos pertenecen a un tipo y cada equipo tiene exactamente un tipo.

```java
// Equipo.java: lado propietario (la tabla equipo guarda la clave foránea id_tipo)
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "id_tipo", nullable = false)
private TipoEquipo tipoEquipo;

// TipoEquipo.java: lado inverso (mappedBy = nombre del atributo en Equipo)
@OneToMany(mappedBy = "tipoEquipo", fetch = FetchType.LAZY)
private List<Equipo> equipos = new ArrayList<>();
```

- **Lado propietario:** `Equipo`, porque su tabla tiene la columna `id_tipo`. Hibernate escribe la clave foránea a partir de este lado.
- **`optional = false` y `nullable = false`:** todo equipo debe tener un tipo, igual que `id_tipo NOT NULL` en la tabla.
- **`FetchType.LAZY`:** el tipo no se carga hasta que se usa. Las consultas del listado usan `JOIN FETCH e.tipoEquipo`, que trae el tipo en la misma consulta, para que la tabla pueda mostrarlo después de cerrar el `EntityManager`.
- **Sin `cascade`:** borrar un equipo no borra su tipo. En PostgreSQL la clave foránea tiene `ON DELETE RESTRICT`, así que no se puede borrar un tipo que todavía tenga equipos.
- **Tipo administrado:** antes de `persist()` o `merge()`, `EquipoDao.obtenerTipoEquipo()` busca el tipo con `em.find(TipoEquipo.class, id)`. Así el equipo queda asociado a una entidad administrada, y si el tipo no existe se lanza *"Error al obtener el tipo de equipo"*.

---

## 7. Consultas y operaciones JPA

### Consultas (JPQL)

| Método | Consulta | Para qué |
|---|---|---|
| `TipoEquipoDao.listar()` | `SELECT t FROM TipoEquipo t ORDER BY t.nombre` | Llenar los combos de tipo |
| `EquipoDao.listar()` | `SELECT e FROM Equipo e JOIN FETCH e.tipoEquipo ORDER BY e.id` | Listar los equipos con su tipo |
| `EquipoDao.buscar(nombre, tipo)` | `SELECT e FROM Equipo e JOIN FETCH e.tipoEquipo t WHERE 1 = 1` + `AND LOWER(e.nombre) LIKE :nombre` + `AND t.id = :tipoId` + `ORDER BY e.id` | Buscar por parte del nombre (sin distinguir mayúsculas) y/o filtrar por tipo |

En `buscar`, cada condición se agrega solo si ese filtro tiene valor, así que se pueden combinar. Los valores escritos por el usuario siempre se pasan con `setParameter(...)` y nunca se pegan al texto de la consulta.

### Operaciones de escritura (`EquipoDao`)

Cada operación abre su propio `EntityManager` con `JPAUtil.getEntityManager()` y lo cierra en `finally`.

| Operación | Método JPA | Detalle |
|---|---|---|
| Guardar | `persist(equipo)` | Inserta el equipo nuevo; PostgreSQL genera el id |
| Actualizar | `merge(equipo)` | Copia los datos sobre el registro existente con el mismo id |
| Eliminar | `find(...)` + `remove(equipo)` | `remove()` solo funciona con una entidad administrada, por eso primero se busca |

Todas las escrituras siguen el mismo patrón de transacción:

```java
tx.begin();                // inicia la transacción
// ... persist / merge / remove ...
tx.commit();               // confirma los cambios en PostgreSQL
// si ocurre un error:
if (tx.isActive()) tx.rollback();   // deshace todo, sin cambios parciales
```
