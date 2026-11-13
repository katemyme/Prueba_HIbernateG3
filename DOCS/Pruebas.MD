# Matriz de pruebas — Control de Equipos de Laboratorio

| Dato | Valor |
|---|---|
| Estudiante | Victor Mateo Alcocer Lopez |
| Docente | Freddy Luis Lopez Barrios |
| Fecha de ejecución | 12/11/2026 |
| Sistema | Windows 11, JDK 21, JavaFX 21.0.6, Hibernate ORM 7.0.4.Final, PostgreSQL 18.4 |
| Base de datos | `bdlaboratorioorm`, esquema `catalogos` (creada con `SQL/bdlaboratorioorm.sql`) |

**Cómo se obtuvieron las evidencias**

- Todas las capturas son de la aplicación real en ejecución, conectada a `bdlaboratorioorm`. Los datos se ingresaron en los controles de la ventana (campos, combos y botones) mediante un programa de prueba; el código del proyecto no se modificó.
- Después de guardar, actualizar, eliminar, buscar o filtrar, la tabla se vuelve a cargar con una consulta a PostgreSQL. Por eso lo que se ve en la tabla es lo que está guardado en la base.
- Punto de partida: los 10 equipos de prueba del script SQL.

![Aplicación iniciada](capturas/01_aplicacion_iniciada.png)

---

## 1. Resumen P1–P8

| ID | Prueba | Entrada | Resultado esperado | Resultado obtenido | Evidencia | Estado |
|---|---|---|---|---|---|---|
| P1 | Registrar un equipo válido | Código `LAP-003`, Nombre `Laptop IdeaPad Slim 3`, Marca `Lenovo`, Cantidad `8`, Tipo `Computadora`, Estado `Disponible` → **Guardar** | Registro guardado y visible | El equipo aparece en la tabla con ID 12 y sigue ahí después de reiniciar (P8) | [P1_1](capturas/P1_1_formulario_con_datos_validos.png), [P1_2](capturas/P1_2_equipo_registrado_y_visible.png) | ✅ Aprobada |
| P2 | Intentar código duplicado | Código `LAP-001` (ya existe), Nombre `Laptop ThinkPad E14 Gen 5`, Cantidad `2`, Tipo `Computadora`, Estado `Disponible` → **Guardar** | Operación rechazada; sin duplicados | Alert *"No se pudo guardar el equipo: Ya existe un equipo con el código "LAP-001"."*; la ventana sigue abierta; al buscar "ThinkPad" en la base aparece un solo `LAP-001` | [P2_1](capturas/P2_1_formulario_con_codigo_duplicado.png), [P2_2](capturas/P2_2_alerta_codigo_duplicado.png), [P2_3](capturas/P2_3_aplicacion_sigue_abierta_sin_cambios.png), [P2_4](capturas/P2_4_busqueda_un_solo_LAP-001.png) | ✅ Aprobada |
| P3 | Editar nombre, estado o tipo | Seleccionar `LAP-003`; Nombre → `Laptop IdeaPad Slim 3 15IAH8`; Estado → `En mantenimiento` → **Actualizar** | Cambios persistidos | La tabla muestra el nuevo nombre y el nuevo estado, y se mantienen después de reiniciar (P8) | [P3_1](capturas/P3_1_equipo_seleccionado.png), [P3_2](capturas/P3_2_formulario_con_cambios.png), [P3_3](capturas/P3_3_cambios_guardados.png) | ✅ Aprobada |
| P4 | Eliminar un equipo con confirmación | Registrar `PER-003` (Mouse óptico USB M90, Logitech, 2, Periférico, Fuera de servicio); seleccionarlo → **Eliminar** → **Aceptar** | El registro desaparece del listado y de PostgreSQL | Se pide confirmación; al aceptar, `PER-003` desaparece del listado y tampoco aparece después de reiniciar (P8) | [P4_1](capturas/P4_1_equipo_a_eliminar_seleccionado.png), [P4_2](capturas/P4_2_confirmacion_de_eliminacion.png), [P4_3](capturas/P4_3_equipo_ya_no_aparece_en_listado.png) | ✅ Aprobada |
| P5 | Buscar por nombre parcial | Buscar nombre: `laptop` → **Buscar** | Solo se muestran coincidencias | Se muestran solo `LAP-001`, `LAP-002` y `LAP-003` (3 de 11); la búsqueda no distingue mayúsculas | [P5](capturas/P5_busqueda_parcial_laptop.png) | ✅ Aprobada |
| P6 | Filtrar por tipo y combinar con búsqueda | a) Tipo `Redes`; b) `switch` + Tipo `Redes`; c) `laptop` + Tipo `Computadora` | Resultados cumplen ambas condiciones | a) 3 equipos de Redes; b) solo `RED-002 Switch Catalyst 2960`; c) solo las 3 laptops (`PC-001` es Computadora pero no contiene "laptop") | [P6_1](capturas/P6_1_filtro_por_tipo_redes.png), [P6_2](capturas/P6_2_filtro_redes_y_busqueda_switch.png), [P6_3](capturas/P6_3_filtro_computadora_y_busqueda_laptop.png), [P6_4](capturas/P6_4_quitar_filtros_muestra_todo.png) | ✅ Aprobada |
| P7 | Ingresar cantidad negativa | Cantidad `-5` (resto de datos válidos) → **Guardar** | Validación rechaza el dato | Alert *"La cantidad no puede ser negativa."*; no se guarda nada | [P7](capturas/P7_cantidad_negativa.png) | ✅ Aprobada |
| P8 | Reiniciar aplicación | Cerrar la aplicación (fin del proceso) y volver a iniciarla | Persisten los datos previamente confirmados | Al reiniciar se cargan desde PostgreSQL los 11 equipos: `LAP-003` con los cambios de P3 y sin `PER-003`, que se eliminó en P4 | [P8_0](capturas/P8_0_aplicacion_antes_de_cerrar.png), [P8_1](capturas/P8_1_aplicacion_reiniciada.png), [P8_2](capturas/P8_2_datos_confirmados_persisten.png) | ✅ Aprobada |

---

## 2. Validaciones obligatorias

| Validación | Entrada | Resultado obtenido | Evidencia |
|---|---|---|---|
| Código o nombre vacíos | Código y nombre en blanco → **Guardar** | Alert *"El código y el nombre son obligatorios."* | [V1](capturas/V1_codigo_y_nombre_vacios.png) |
| Cantidad no numérica | Cantidad `abc` → **Guardar** | Alert *"La cantidad debe ser un número entero."* | [V2](capturas/V2_cantidad_no_numerica.png) |
| Cantidad negativa | Cantidad `-5` → **Guardar** | Alert *"La cantidad no puede ser negativa."* | [P7](capturas/P7_cantidad_negativa.png) |
| Ausencia de tipo | Sin tipo seleccionado → **Guardar** | Alert *"Debe seleccionar un tipo de equipo."* | [V3](capturas/V3_sin_tipo.png) |
| Código duplicado | Código `LAP-001` → **Guardar** | Alert *"No se pudo guardar el equipo: Ya existe un equipo con el código "LAP-001"."* | [P2_2](capturas/P2_2_alerta_codigo_duplicado.png) |
| Estado no permitido | Sin estado seleccionado → **Guardar** | Alert *"Debe seleccionar un estado."* | [V4](capturas/V4_sin_estado.png) |

**Estado no permitido:**

- **En la interfaz:** el combo de estado solo ofrece `Disponible`, `En mantenimiento` y `Fuera de servicio`, así que desde la ventana no se puede ingresar otro valor, y dejarlo vacío se rechaza (V4).
- **En la base de datos:** la tabla tiene la restricción `chk_equipo_estado`. Se comprobó en `psql` que un `INSERT` con el estado `'Prestado'` es rechazado con *"el nuevo registro para la relación «equipo» viola la restricción «check» «chk_equipo_estado»"*. Se puede repetir así:

  ```sql
  INSERT INTO catalogos.equipo (codigo, nombre, marca, estado, cantidad, id_tipo)
  VALUES ('TST-999', 'Equipo de prueba', 'Generica', 'Prestado', 1, 1);
  ```

**Mensajes de éxito y Alert ante errores:** la aplicación no muestra mensajes de éxito. Una operación correcta se ve reflejada en la tabla, que se recarga desde PostgreSQL. Cuando algo falla, solo se muestra el Alert de advertencia o error, y al cerrarlo la ventana sigue abierta con los datos sin cambios ([P2_3](capturas/P2_3_aplicacion_sigue_abierta_sin_cambios.png)).

---

## 3. Operación que falla por integridad y uso de `rollback()`

**Entrada ([R1](capturas/R1_edicion_con_codigo_duplicado_y_cantidad_99.png)):** se seleccionó `LAP-003`, que tenía cantidad 8. En la misma edición se cambió la cantidad a `99` y el código a `LAP-001`, que ya existe. Luego se presionó **Actualizar**.

**Resultado ([R2](capturas/R2_alerta_actualizacion_rechazada.png)):** PostgreSQL rechazó el `UPDATE` por la restricción `UNIQUE` `equipo_codigo_key`. La aplicación mostró *"No se pudo actualizar el equipo: Ya existe un equipo con el código "LAP-001"."* y la ventana siguió abierta.

**Comprobación:** justo después se consultó la base en `psql`.

```sql
SELECT id_equipo, codigo, nombre, cantidad, estado FROM catalogos.equipo WHERE nombre = 'Laptop IdeaPad Slim 3';
--  12 | LAP-003 | Laptop IdeaPad Slim 3 | 8 | Disponible
SELECT codigo, COUNT(*) AS veces FROM catalogos.equipo WHERE codigo = 'LAP-001' GROUP BY codigo;
--  LAP-001 | 1
```

`LAP-003` siguió con su código y con cantidad 8, y `LAP-001` siguió existiendo una sola vez. No se guardó ninguna parte de la edición: ni el código nuevo ni la cantidad 99. En las capturas siguientes ([P3_1](capturas/P3_1_equipo_seleccionado.png), [P5](capturas/P5_busqueda_parcial_laptop.png)) también se ven `LAP-001` y `LAP-003` como equipos distintos.

Este es el método `actualizar` de `EquipoDao`; los comentarios se agregaron aquí para explicarlo:

```java
public void actualizar(Equipo equipo) {
    EntityManager em = JPAUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
        tx.begin();                       // inicia la transacción en PostgreSQL
        TipoEquipo tipoEquipo = obtenerTipoEquipo(em, equipo);
        equipo.setTipoEquipo(tipoEquipo);
        em.merge(equipo);                 // copia los datos sobre el registro con el mismo id
        tx.commit();                      // aquí se envía el UPDATE; PostgreSQL lo rechaza
    } catch (RuntimeException ex) {
        if (tx.isActive()) {
            tx.rollback();                // deshace todo lo hecho dentro de la transacción
        }
        throw ex;                         // el controller muestra el Alert de error
    } finally {
        em.close();
    }
}
```

- **Al guardar (P2):** el `INSERT` se ejecuta en `persist()`, porque el id es `IDENTITY`. Al fallar, la transacción sigue activa y el `catch` llama a `tx.rollback()`.
- **Al actualizar (este caso):** el `UPDATE` se envía en `commit()`. Cuando PostgreSQL lo rechaza, Hibernate deshace la transacción y lanza `RollbackException`. El `if (tx.isActive())` evita llamar a `rollback()` dos veces.
- **En los dos casos:** la base queda exactamente como antes de `begin()`, sin cambios parciales.
- **Los id no se recuperan:** `rollback()` deshace los datos, pero no el contador de la columna `IDENTITY`. Por eso los id 11 y 13 se consumieron en `INSERT` rechazados (el estado `'Prestado'` y el `LAP-001` duplicado de P2), y `LAP-003` tiene el id 12 y `PER-003` tuvo el 14. Es el comportamiento normal de PostgreSQL y no deja registros a medias.

---

## 4. Detalle de cada prueba

### Validaciones

| Código y nombre vacíos | Cantidad no numérica |
|---|---|
| ![V1](capturas/V1_codigo_y_nombre_vacios.png) | ![V2](capturas/V2_cantidad_no_numerica.png) |
| **Sin tipo** | **Sin estado** |
| ![V3](capturas/V3_sin_tipo.png) | ![V4](capturas/V4_sin_estado.png) |

### P1 — Registrar un equipo válido

![P1_1 Formulario con datos válidos](capturas/P1_1_formulario_con_datos_validos.png)
![P1_2 Equipo registrado y visible en la tabla](capturas/P1_2_equipo_registrado_y_visible.png)

### P2 — Intentar código duplicado

![P2_1 Formulario con código duplicado](capturas/P2_1_formulario_con_codigo_duplicado.png)
![P2_2 Alert de código duplicado](capturas/P2_2_alerta_codigo_duplicado.png)
![P2_3 La aplicación sigue abierta y sin cambios](capturas/P2_3_aplicacion_sigue_abierta_sin_cambios.png)
![P2_4 Búsqueda: un solo LAP-001](capturas/P2_4_busqueda_un_solo_LAP-001.png)

### Rollback

![R1 Edición con código duplicado y cantidad 99](capturas/R1_edicion_con_codigo_duplicado_y_cantidad_99.png)
![R2 Alert de actualización rechazada](capturas/R2_alerta_actualizacion_rechazada.png)

### P3 — Editar nombre y estado

![P3_1 Equipo seleccionado](capturas/P3_1_equipo_seleccionado.png)
![P3_2 Formulario con los cambios](capturas/P3_2_formulario_con_cambios.png)
![P3_3 Cambios guardados en la tabla](capturas/P3_3_cambios_guardados.png)

### P4 — Eliminar con confirmación

![P4_1 Equipo a eliminar seleccionado](capturas/P4_1_equipo_a_eliminar_seleccionado.png)
![P4_2 Confirmación de eliminación](capturas/P4_2_confirmacion_de_eliminacion.png)
![P4_3 El equipo ya no aparece en el listado](capturas/P4_3_equipo_ya_no_aparece_en_listado.png)

### P5 — Buscar por nombre parcial

![P5 Búsqueda "laptop"](capturas/P5_busqueda_parcial_laptop.png)

### P6 — Filtrar por tipo y combinar con búsqueda

![P6_1 Filtro por tipo Redes](capturas/P6_1_filtro_por_tipo_redes.png)
![P6_2 Tipo Redes + búsqueda "switch"](capturas/P6_2_filtro_redes_y_busqueda_switch.png)
![P6_3 Tipo Computadora + búsqueda "laptop"](capturas/P6_3_filtro_computadora_y_busqueda_laptop.png)
![P6_4 Quitar filtros](capturas/P6_4_quitar_filtros_muestra_todo.png)

### P7 — Cantidad negativa

![P7 Cantidad negativa rechazada](capturas/P7_cantidad_negativa.png)

### P8 — Reiniciar la aplicación

![P8_0 Antes de cerrar la aplicación](capturas/P8_0_aplicacion_antes_de_cerrar.png)
![P8_1 Aplicación reiniciada](capturas/P8_1_aplicacion_reiniciada.png)
![P8_2 Los datos confirmados persisten](capturas/P8_2_datos_confirmados_persisten.png)
