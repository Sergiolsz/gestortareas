# Task Manager

Task Manager es una aplicación de gestión de tareas desarrollada en **Spring Boot** con **JDK 17** y base de datos **MongoDB**. 
La aplicación permite la creación y consulta de tareas, siguiendo una arquitectura hexagonal.

## Requisitos Previos

Componentes necesarios:

- **JDK 17**: La aplicación requiere JDK 17.
- **Maven 3.8+**: Utilizado para la construcción y gestión de dependencias.
- **MongoDB**: Asegúrate de que MongoDB esté en ejecución en `localhost:27017`.
- **Git**: Para clonar el repositorio.

## Configuración de la Aplicación

### Clonación del Repositorio

Clona el repositorio desde GitHub y navega al directorio del proyecto:

```bash
git clone https://github.com/tu-usuario/manager-tasks.git
cd manager-tasks
```

## Configuración de MongoDB
MongoDB debe estar ejecutándose en localhost:27017. 
Si es necesario, se deberá ajustar la configuración en el archivo application.yml de la aplicación.

## Archivo application.yml

La configuración principal de la aplicación se encuentra en src/main/resources/application.yml:

### Propiedades

Configuración general de Spring Boot
- spring.application.name=gestor-tareas

Configuración de la base de datos MongoDB
- spring.data.mongodb.uri=mongodb://localhost:27017/manager-tasks

Configuración del servidor web
- spring.web.servlet.context-path=/api
- server.port=8080

Configuración de logging
- logging.level.root=INFO
- logging.level.org.springframework.web=DEBUG

Se puede modificar estas propiedades para personalizar la configuración de la aplicación.

## Construcción y Ejecución

### Construcción del Proyecto
Para construir el proyecto, navega al directorio raíz del proyecto y ejecuta:

```bash
mvn clean install
```
Este comando descargará todas las dependencias necesarias, compilará el código fuente y empaquetará la aplicación en un archivo JAR.

### Ejecución de la Aplicación
Una vez construido, ejecuta la aplicación con el siguiente comando:

```bash
java -jar target/manager-tasks-0.0.1-SNAPSHOT.jar
```
La aplicación estará disponible en http://localhost:8080/api.

### Verificación
Puedes acceder a la documentación de la API generada por Swagger en:

http://localhost:8080/api/swagger-ui.html

## Funcionalidades Principales

La aplicación expone los siguientes endpoints:

POST /tasks: Crea una nueva tarea.
GET /tasks: Obtiene la lista de todas las tareas.
GET /tasks/{id}: Obtiene una tarea por su ID.

## Testing
Para ejecutar las pruebas unitarias, utiliza el comando:

```bash
mvn test
```

## Contacto y Soporte

Si durante el proceso se pudiera generar alguna pregunta o problema, contacta con el equipo de desarrollo a **sergiolsz82@gmail.com**