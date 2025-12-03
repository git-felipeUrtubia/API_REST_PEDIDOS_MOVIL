# 🛠️ API_LEVEL_UP_MOVIL – API REST en Spring Boot

API REST creada en **Java Spring Boot** para gestionar un sistema completo de:
- Clientes  
- Productos  
- Pedidos  
- Detalles de pedidos  

Diseñada para integrarse con aplicaciones móviles (como tu app Kotlin Compose) y proporcionar endpoints limpios, tipados con DTOs y totalmente listos para uso en producción o pruebas.

---

## 📂 Estructura del Proyecto

src/main/java/com/empresa/api_level_up_movil/
├── controller/ # Controladores REST
├── dto/
│ ├── request/ # DTO de entrada
│ └── response/ # DTO de salida
├── model/ # Entidades JPA
├── repository/ # Repositorios JPA
├── service/ # Lógica de negocio
└── ApiLevelUpMovilApplication.java


---

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Web**
- **Spring Data JPA**
- **MySQL**
- **Hibernate**
- **DTO (Request/Response Model)**
- **Arquitectura por capas (Controller – Service – Repository)**

---

## 📌 Endpoints Principales

### 👉 **Clientes**
**Base path:** `/api/v1/clientes`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Obtiene todos los clientes |
| GET | `/{id}` | Obtiene cliente por ID |
| POST | `/` | Crea un nuevo cliente |
| PUT | `/{id}` | Actualiza un cliente |
| DELETE | `/{id}` | Elimina un cliente |

---

### 👉 **Productos**
**Base path:** `/api/v1/productos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Obtiene todos los productos |
| GET | `/{id}` | Obtiene producto por ID |
| POST | `/` | Crea un nuevo producto |
| PUT | `/{id}` | Actualiza un producto |
| DELETE | `/{id}` | Elimina un producto |

---

### 👉 **Pedidos**
**Base path:** `/api/v1/pedidos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Lista todos los pedidos |
| GET | `/{id}` | Obtiene un pedido por ID |
| POST | `/` | Registra un nuevo pedido |
| PUT | `/{id}` | Actualiza un pedido |
| DELETE | `/{id}` | Elimina un pedido |

---

### 👉 **Detalle de Pedido**
**Base path:** `/api/v1/detalle-pedido`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Lista los detalles |
| GET | `/{id}` | Obtiene un detalle por ID |
| POST | `/` | Crea un detalle de pedido |
| PUT | `/{id}` | Actualiza un detalle |
| DELETE | `/{id}` | Elimina un detalle |

---

## 🧠 Arquitectura del Proyecto

Esta API sigue una arquitectura clara por capas:

1. **Controller** → recibe solicitudes HTTP  
2. **DTO Request/Response** → evita exponer entidades directamente  
3. **Service** → contiene la lógica de negocio  
4. **Repository** → interactúa con la base de datos  
5. **Model** → entidades JPA que representan tablas en MySQL

---

## 🛢️ Base de Datos (MySQL)

Tablas incluidas:

- Cliente  
- Producto  
- Pedido  
- DetallePedido  

Todas con relaciones correctas mediante `@OneToMany`, `@ManyToOne` y claves foráneas.

---

## 🚀 Cómo Ejecutar la API

### 1️⃣ Clonar repositorio
git clone https://github.com/tu-usuario/API_LEVEL_UP_MOVIL.git
