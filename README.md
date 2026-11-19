# Reboot Tech - Backend API

Backend de la aplicación web Reboot Tech, desarrollado con Java y Spring Boot. Proporciona una API REST para gestionar trabajadores, clientes y reparaciones, además de controlar la autenticación y los permisos de acceso según el rol de cada trabajador.

## Funcionalidades principales

- **Autenticación:** permite validar las credenciales de los trabajadores y generar tokens JWT para gestionar sus sesiones.
- **Gestión de trabajadores:** permite registrar, consultar, modificar, desactivar y eliminar cuentas del personal.
- **Gestión de clientes:** permite registrar, buscar y actualizar la información de los clientes.
- **Gestión de reparaciones:** permite registrar solicitudes, consultar pedidos y administrar sus diferentes estados.
- **Atención técnica:** permite asignar reparaciones, registrar diagnósticos, solicitar repuestos y documentar las soluciones realizadas.
- **Confirmación de repuestos:** permite gestionar la confirmación de los clientes y devolver las reparaciones al técnico que realizó el diagnóstico.
- **Consulta pública:** proporciona información básica sobre las reparaciones mediante el código de atención, sin requerir autenticación.
- **Estadísticas:** proporciona información sobre los servicios registrados, completados y en espera.

## Tecnologías y dependencias utilizadas

| Tecnología o biblioteca | Uso en el proyecto |
|---|---|
| **Java 21** | Lenguaje de programación utilizado para implementar la lógica del backend. |
| **Spring Boot 4.1.2** | Framework principal utilizado para configurar y desarrollar la aplicación. |
| **Spring Web MVC** | Permite recibir solicitudes HTTP y crear los endpoints de la API REST. |
| **Spring Data JPA** | Facilita el acceso y la gestión de los datos almacenados mediante repositorios. |
| **Hibernate** | Implementación de JPA utilizada para relacionar las clases Java con las tablas de la base de datos. |
| **HSQLDB** | Sistema de gestión de base de datos utilizado para almacenar la información de trabajadores, clientes y reparaciones. |
| **Spring Security** | Permite establecer las reglas de seguridad y controlar el acceso a los recursos según los roles. |
| **Spring Security Crypto** | Proporciona funciones para cifrar y verificar las contraseñas de los trabajadores. |
| **JJWT** | Biblioteca utilizada para generar, firmar y validar tokens JWT durante la autenticación. |
| **Spring Boot Validation** | Permite validar los datos recibidos en las solicitudes y comprobar que cumplan las condiciones establecidas. |
| **Maven** | Herramienta utilizada para gestionar las dependencias y ejecutar el proyecto. |

## Requisitos previos

Para ejecutar el proyecto se necesita:

- Java Development Kit (JDK) 21.
- Git.
- El frontend de Reboot Tech para utilizar la aplicación completa.

El proyecto incluye Maven Wrapper, por lo que no es necesario instalar Maven por separado.

## Instalación y ejecución

**1. Clonar el repositorio**

```bash
git clone https://github.com/FabrizioFernandez2008/RebootTech-API.git
```

**2. Ingresar a la carpeta del proyecto**

```bash
cd RebootTech-API
```

**3. Configurar la variable de entorno para JWT**

En PowerShell, ejecutar:

```powershell
$env:JWT_SECRET = "TU_CLAVE_SECRETA"
```

Se debe utilizar una clave secreta segura para la firma de los tokens JWT. Esta clave no debe compartirse ni subirse al repositorio.

**4. Iniciar el backend**

En PowerShell, ejecutar:

```powershell
.\mvnw spring-boot:run
```

**5. Acceder a la API**

El backend estará disponible en:

http://localhost:8081

La dirección base de la API es:

http://localhost:8081/api

## Base de datos y datos iniciales

El proyecto utiliza HSQLDB en modo archivo para conservar la información registrada durante su funcionamiento.

La base de datos se almacena automáticamente en la carpeta `data/`, que no se incluye en el repositorio para evitar subir información generada durante el uso de la aplicación.

Además, el proyecto cuenta con una configuración de datos iniciales que registra trabajadores, clientes y reparaciones de prueba al iniciar la aplicación. Esto permite probar las funcionalidades del sistema y verificar la conexión con la base de datos.

## Conexión con el frontend

El backend proporciona los servicios y endpoints que utiliza el frontend de Reboot Tech mediante solicitudes HTTP.

El frontend se encuentra en el repositorio [RebootTech](https://github.com/FabrizioFernandez2008/RebootTech).

Para utilizar la aplicación completa, primero debe iniciarse el backend y posteriormente el frontend, disponible en:

http://localhost:4200
