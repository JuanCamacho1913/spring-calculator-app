![Descripción alternativa](src/main/resources/static/calculator-image.png)

# Documentación del Proyecto Calculadora

## Descripción
**Este proyecto es una API REST de operaciones matemáticas desarrollada con Spring Boot. Permite realizar cálculos, almacenar el historial de operaciones realizadas, consultar dicho historial con filtros por tipo de operación y rango de fechas, así como eliminar operaciones específicas.**

**Incluye integración con Swagger para facilitar la documentación y pruebas interactivas de los endpoints.**


## Características

1. **Autenticación con Spring Security**: La API implementa un medio y esquema de autenticación utilizando Spring Security. Esto permite identificar qué usuario está realizando búsquedas de información.

2. **Autorización con Json Web Token**: La API implementa un método de seguridad que permite validar y controlar el acceso a mi API REST.

3. **Arquitectura en capas**: Este proyecto implementa una arquitectura en capas claramente definida, siguiendo los principios de separación de responsabilidades y alta cohesión. Cada capa tiene una función específica y se comunica con las demás de manera controlada.

4. **Almacenamiento en Base de Datos**: Se implementa un esquema de base de datos, preferiblemente MySQL, para almacenar toda la información necesaria. Esto incluye datos relacionados con operaciones aritméticas y usuarios.

5. **Documentación con Swagger**: Este proyecto utiliza Swagger para la generación automática de documentación de la API REST. Swagger permite explorar y probar los endpoints de forma interactiva desde una interfaz web intuitiva.

Puedes acceder a la documentación en:
```properties
http://localhost:8080/swagger-ui/index.html
```

## Configuración
## Instrucciones de Instalación

### Requisitos Previos

- Java JDK 17
- Maven
- MySQL 8.0+
- Git

### Clonar el repositorio
- git clone https://github.com/JuanCamacho1913/spring-calculator-app.git

### Configurar la base de datos
- Crear una base de datos MySQL llamada `calculatorDatabase`
  
Se debe de configurar en el archivo application.properties las propiedades correspondientes para MySQL:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/calculatorDatabase
```

## Paquetes del Proyecto
El proyecto está organizado en los siguientes paquetes:

- `com.calculator.advice`: Gestiona de forma centralizada los errores más comunes que pueden ocurrir durante el uso de la API. Esta capa captura y devuelve respuestas claras y estructuradas ante excepciones como: Errores de validación, Errores aritméticos y errores de puntero nulo.
- `com.example.calculator.config`: Configura la seguridad de una aplicación Spring Boot con JWT (JSON Web Token). Sus características principales son: Implementa autenticación stateless con tokens JWT, Define puntos de acceso público como Endpoints de registro y login (/api/auth/login y /api/auth/register), Protege todos los demás endpoints con autenticación, Configura el validador de tokens JWT, Utiliza BCrypt para el cifrado de contraseñas.
- `com.example.calculator.exception`: Captura excepciones personalizadas.
- `com.example.calculator.mapper`: Contiene mapeadores utilizando MapStruct para la conversión eficiente entre objetos de diferentes capas de la aplicación.
- `com.example.calculator.persistence`: Contiene entidades y repositorios.
- `com.example.calculator.presentation`: Contiene controladores que actúan como punto de entrada a la aplicación y Dto para transferir información.
- `com.example.calculator.service`: Contiene la lógica de negocio de la aplicación estructurada en dos componentes principales. Interface, Implementaciones.
- `com.example.calculator.utils`: Contiene herramientas como JwtUtils encapsula toda la lógica relacionada con la gestión de tokens JWT, incluyendo generación, validación y extracción de información de tokens y OperationTypeEnum define los tipos de operaciones disponibles en el sistema, proporcionando un conjunto controlado de constantes.

## Controlladores

- La aplicación implementa una estructura modular de rutas `AuthController`, `CalculatorOperationController` delegan la lógica de negocio a las capas inferiores. Cada controlador se especializa en un recurso o funcionalidad específica de la aplicación.

### Rutas:

**URL:**
```properties
http://localhost:8080
```
**CalculatorOperationController:**

- **GET**
- `/api/history`: Lista paginada de 10 en 10 de operaciones del usuario.
- `/api/history?operationType=operationType`: Buscar por tipo de operación.
- `/api/history?startDate=2025-05-05T15:30:00&endDate=2025-05-05T20:30:00`: Buscar por rango de fechas.
- `/api/history/{id}`: Detalle de una operación específica por su ID.


- **POST**
- `/api/calculate`: Agrega una nueva operación.


- **DELETE**
- `/api/delete/history/{id}`: Eliminar registro de operación.

**AuthController:**

- **POST**
- `/api/auth/register`: Registro de usuarios.
- `/api/auth/login`: Autenticación y generación de token.

## Manejo de Excepciones
El proyecto incluye un sistema de manejo de excepciones que garantiza una respuesta adecuada a diferentes tipos de errores, El manejo de excepciones se realiza a través de las clases `CalculatorControllerAdvice`, `ArithmeticOperationException`, `ElementNotFoundException`.


## Pasos Para descargar un fichero
1. En GitHub.com, navega a la página principal del repositorio.
2. Navegas hasta el fichero y damos clic para ingresar al fichero.
2. En la parte superior del fichero, haz clic en **Raw**.
3. Te llevará al contenido del fichero, haz clic derecho y guardar como.

## Resumen
Este documento proporciona una visión general de la estructura del proyecto, sus componentes claves y configuraciones.