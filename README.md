# Online Medical Health Tracker (OMHT) - Backend API

API RESTful desarrollada para el sistema Online Medical Health Tracker (OMHT), un proyecto académico para la Universidad del Magdalena. Este backend gestiona la lógica de negocio, persistencia de datos y seguridad para una plataforma integral de gestión de consultorios médicos, pacientes, doctores y citas.

## Tecnologías Utilizadas

* **Java** * **Spring Boot** (Web, Data JPA, Validation)
* **Spring Security & JWT** (Autenticación y Autorización)
* **PostgreSQL** (Base de datos relacional)
* **Maven** (Gestión de dependencias)
* **Docker** (Contenedorización de la base de datos)
* **Lombok** (Reducción de código boilerplate)

## Arquitectura y Patrones

El proyecto sigue una arquitectura en capas (Controller, Service, Repository) e implementa los siguientes patrones y buenas prácticas:
* **DTO (Data Transfer Object):** Uso de `records` de Java para aislar las entidades de la base de datos de las respuestas HTTP.
* **Mappers:** Transformación limpia entre Entidades y DTOs.
* **Proyecciones (JPA Projections):** Optimización de consultas nativas para el módulo de reportes y estadísticas.
* **Manejo Global de Excepciones:** Respuestas estandarizadas para errores 400, 401, 403 y 404 mediante un `GlobalExceptionHandler` y `@ControllerAdvice`.

## Estructura de Módulos Principales

1. **Security (`/api/auth`):** Login, generación y validación de tokens JWT.
2. **Pacientes (`/api/patients`):** Registro y gestión de historial de pacientes.
3. **Doctores (`/api/doctors` & `/api/doctor-schedules`):** Gestión de personal médico, perfiles profesionales, especialidades y horarios de disponibilidad.
4. **Citas (`/api/appointments` & `/api/availability`):** Motor de agendamiento que verifica en tiempo real la disponibilidad de los doctores según sus horarios y citas previas.
5. **Consultorios (`/api/offices`):** Administración de los espacios físicos.
6. **Reportes (`/api/reports`):** Endpoints analíticos para medir la productividad médica, ocupación de oficinas y tasas de cancelación.

## Requisitos Previos

* Java Development Kit (JDK) 17 o superior
* Maven 3.8+
* PostgreSQL 15+ o Docker Desktop instalado
