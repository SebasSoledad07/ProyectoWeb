
# Contexto del Proyecto

El sistema es una plataforma de gestión fitness orientada a entrenadores y clientes que permite:

* autenticación y autorización por roles,
* gestión de clientes,
* seguimiento biométrico,
* creación y asignación de rutinas,
* agenda de citas,
* control de disponibilidad,
* notificaciones,
* historial de progreso,
* y administración de ejercicios/gimnasios.

---

# Reglas Generales

## 1. Idioma y convenciones

* Todo el código debe estar completamente en inglés.
* No usar nombres en español en:

    * clases,
    * atributos,
    * métodos,
    * tablas,
    * DTOs,
    * endpoints,
    * variables.

### Convenciones

| Elemento   | Convención       | Ejemplo                   |
| ---------- | ---------------- |---------------------------|
| Package    | lowercase        | `com.ufps.proyectoweb`    |
| Clase      | PascalCase       | `WorkoutTemplateService`  |
| Método     | camelCase        | `assignWorkoutToClient`   |
| Variable   | camelCase        | `bodyMeasurement`         |
| Constantes | UPPER_SNAKE_CASE | `MAX_CLIENTS_PER_TRAINER` |

---

# 2. Arquitectura del proyecto

La arquitectura debe seguir separación clara por capas.

## Controllers

Responsabilidades:

* manejar HTTP,
* recibir DTOs,
* validación superficial,
* devolver responses,
* delegar lógica al Service.

NO debe contener:

* lógica de negocio,
* acceso a base de datos,
* cálculos complejos.

---

## Services

Responsabilidades:

* lógica de negocio,
* reglas funcionales,
* manejo transaccional,
* validaciones críticas,
* orquestación.

Ejemplos:

* validación de cruces de horario,
* cálculo de IMC,
* clonación profunda de rutinas,
* actualización de estados de citas.

---

## Repositories

Responsabilidades:

* acceso a datos con Spring Data JPA,
* consultas específicas,
* paginación,
* filtros.

NO debe contener:

* lógica de negocio.

---

## Mappers

* Usar MapStruct preferiblemente.
* Todo mapeo Entity ↔ DTO debe centralizarse.

---

# 3. DTOs

## Obligatorio

Todos los DTOs deben implementarse usando `record`.

## Ejemplo

```java
package com.ufps.fitmanager.dto;

/**
 * DTO used to return body measurement information.
 *
 * @param id measurement identifier
 * @param weight client weight
 * @param height client height
 * @param bmi calculated BMI
 * @param measurementDate measurement date
 */
public record BodyMeasurementDto(
        Long id,
        Double weight,
        Double height,
        Double bmi,
        LocalDate measurementDate
) {}
```

## Restricciones

* No usar entities en responses.
* No exponer entidades JPA directamente.
* No usar `record` para entidades JPA.

---

# 4. Entidades JPA

## Reglas

* Las entidades deben ser clases normales.
* Deben tener:

    * constructor vacío protegido,
    * relaciones bien definidas,
    * auditoría cuando aplique.

## Relaciones esperadas

### User

Roles:

* TRAINER
* CLIENT

### Trainer

Relación:

* OneToMany → Clients

### WorkoutTemplate

Relación:

* OneToMany → Exercises

### Appointment

Relaciones:

* Trainer
* Client
* Gym

---

# 5. Seguridad

## Spring Security obligatorio

El sistema debe implementar:

* autenticación JWT,
* autorización basada en roles,
* protección de endpoints.

## Roles mínimos

| Rol     | Acceso                                      |
| ------- | ------------------------------------------- |
| TRAINER | administración de clientes, rutinas y citas |
| CLIENT  | visualización de rutinas y progreso         |

---

# 6. Validación

Usar `jakarta.validation`.

## Ejemplo

```java
public record RegisterUserDto(

        @NotBlank
        String firstName,

        @Email
        String email,

        @Size(min = 8)
        String password
) {}
```

## Reglas

* Usar `@Valid` en controllers.
* Revalidar reglas críticas en services.

---

# 7. Manejo de excepciones

## Obligatorio

Usar excepciones específicas.

Ejemplos:

* `AppointmentConflictException`
* `WorkoutTemplateNotFoundException`
* `ClientNotFoundException`

## Global Exception Handler

Usar:

```java
@ControllerAdvice
```

para respuestas consistentes.

---

# 8. Transacciones

Toda lógica crítica debe usar:

```java
@Transactional
```

Ejemplos:

* asignación de rutinas,
* creación de citas,
* registro biométrico.

---

# 9. Logging

Usar SLF4J.

## Niveles

| Nivel | Uso                     |
| ----- | ----------------------- |
| DEBUG | desarrollo              |
| INFO  | operaciones importantes |
| WARN  | inconsistencias         |
| ERROR | errores críticos        |

## Restricciones

NO loggear:

* contraseñas,
* tokens,
* datos sensibles.

---

# 10. Reglas específicas del dominio

## Rutinas

La asignación de rutinas debe realizar una copia profunda.

La plantilla original nunca debe modificarse.

---

## Citas

Debe validarse:

* disponibilidad del entrenador,
* disponibilidad del cliente,
* conflictos de horario.

---

## Biometría

El IMC debe calcularse automáticamente.

IMC = \frac{peso}{altura^2}

---

## Historial

Las mediciones deben mantenerse históricas.

Nunca sobrescribir registros anteriores.

---

# 11. Documentación

## Javadoc obligatorio

Todos los métodos públicos deben documentarse.

## Ejemplo

```java
/**
 * Assigns a workout template to a specific client.
 *
 * @param clientId client identifier
 * @param templateId workout template identifier
 * @return assigned workout DTO
 * @throws ClientNotFoundException when client does not exist
 */
```

---

# 12. Testing

## Unit Testing

Usar:

* JUnit 5,
* Mockito.

## Integration Testing

Usar:

* SpringBootTest,
* Testcontainers.

## Cobertura mínima

* happy path,
* validaciones,
* excepciones,
* conflictos de horarios.

---

# 13. Estructura recomendada

```text
src/
 └── main/
     └── java/
         └── com.ufps.fitmanager/
             ├── controller/
             ├── service/
             ├── repository/
             ├── dto/
             ├── entity/
             ├── mapper/
             ├── exception/
             ├── security/
             ├── config/
             ├── util/
             └── validation/
```

---

# 14. Estructura sugerida por módulos

## auth

* login,
* JWT,
* roles.

## user

* perfiles,
* entrenadores,
* clientes.

## workout

* ejercicios,
* rutinas,
* asignaciones.

## biometric

* IMC,
* medidas,
* historial.

## appointment

* agenda,
* disponibilidad,
* reservas.

## notification

* correos,
* recordatorios.

---

# 15. Base de datos

## Recomendaciones

* Usar PostgreSQL.
* Migraciones con Flyway.
* Naming consistente.

## Tablas

snake_case obligatorio.

Ejemplo:

```sql
workout_templates
body_measurements
appointment_status
```

---

# 16. API REST

## Convenciones

### Endpoints

```http
/api/v1/clients
/api/v1/workouts
/api/v1/appointments
```

## Responses

Usar códigos HTTP correctos.

| Código | Uso              |
| ------ | ---------------- |
| 200    | success          |
| 201    | created          |
| 204    | no content       |
| 400    | validation error |
| 401    | unauthorized     |
| 403    | forbidden        |
| 404    | not found        |
| 409    | conflict         |

---

# 17. Calidad de código

## Herramientas

* Checkstyle,
* Spotless,
* SpotBugs,
* SonarQube.

## CI obligatorio

```bash
mvn clean verify
```

---

# 18. Buenas prácticas modernas

## Preferir

* records,
* streams legibles,
* Optional correctamente,
* programación inmutable.

## Evitar

* lógica gigante en controllers,
* métodos extremadamente largos,
* código duplicado,
* consultas innecesarias.

---

# 19. Checklist para PRs

* [ ] Código completamente en inglés
* [ ] DTOs implementados con `record`
* [ ] Métodos públicos documentados
* [ ] Tests agregados
* [ ] Validaciones implementadas
* [ ] Seguridad aplicada
* [ ] Sin lógica de negocio en controllers
* [ ] Excepciones específicas creadas
* [ ] Mappers implementados correctamente

---

# 20. Aplicación obligatoria

Estas reglas deben seguirse en:

* generación de código,
* refactors,
* endpoints nuevos,
* tests,
* documentación,
* revisiones de PR.

Si alguna regla no puede cumplirse por restricciones técnicas, la excepción debe documentarse explícitamente.
