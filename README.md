# 📚 API REST de Libros - Spring Boot

API RESTful completa para la gestión de libros, desarrollada con Spring Boot y Maven.

## 🛠️ Tecnologías

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## 📖 Funcionalidades

- ✅ CRUD completo de libros
- 🔍 Búsqueda por título y autor
- 📄 Paginación de resultados
- 👤 Gestión de usuarios
- 🔐 Autenticación JWT
- 📊 Dashboard de administración

## 🔗 Endpoints principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/libros` | Listar todos los libros |
| GET | `/api/libros/{id}` | Obtener libro por ID |
| POST | `/api/libros` | Crear nuevo libro |
| PUT | `/api/libros/{id}` | Actualizar libro |
| DELETE | `/api/libros/{id}` | Eliminar libro |
| GET | `/api/libros/buscar?q=texto` | Búsqueda avanzada |

## 🚀 Ejecutar

```bash
# Con Maven
./mvnw spring-boot:run

# Con Docker
docker build -t api-libros .
docker run -p 8080:8080 api-libros
```

## 📦 Requisitos

- JDK 17+
- MySQL 8.0+
- Maven 3.8+

---

⭐ Si te fue útil, dale una estrella al proyecto!