# Spring Bank

Sistema bancario modular de alto rendimiento desarrollado con **Spring Boot 4.0.3** y **Java 25**, enfocado en la
integridad financiera y la escalabilidad mediante Virtual Threads.

---

## 🚀 Características principales

- **Arquitectura Hexagonal (Ports & Adapters)**: Separación estricta entre dominio, aplicación e infraestructura para
  facilitar el mantenimiento y testing.
- **Contabilidad de Doble Entrada (Double-Entry Ledger)**: Sistema de registro que garantiza que cada débito tenga un
  crédito correspondiente, asegurando la integridad del balance.
- **Virtual Threads (Project Loom)**: Aprovechamiento de hilos virtuales para un manejo eficiente de miles de conexiones
  simultáneas.
- **Procesamiento XML (JAXB & XSD)**: API REST basada en contratos XML estrictos definidos mediante XML Schema.
- **Patrón Outbox**: Garantiza la consistencia eventual entre el dominio y los sistemas externos (eventos).
- **Idempotencia**: Prevención de transacciones duplicadas mediante identificadores únicos.
- **Observabilidad**: Integración con OpenTelemetry para trazabilidad (pre-configurado).

---

## 🛠️ Tecnologías utilizadas

- **Core**: Java 25, Spring Boot 4.0.3
- **Persistencia**: Spring Data JPA, PostgreSQL 18.3, Hibernate
- **Mapeo y Utilidades**: Lombok, MapStruct
- **Comunicación**: Jakarta XML Binding (JAXB)
- **Testing**:
    - **Unitarios/Integración**: JUnit 5, Spring Boot Test
    - **BDD**: Cucumber (Acceptance Testing)
    - **Carga**: Gatling 3.15.0
- **Infraestructura**: Docker & Docker Compose

---

## 📂 Estructura del proyecto

El proyecto sigue una organización por dominios, implementando arquitectura hexagonal dentro de cada uno:

```text
src/main/java/com/davinchicoder/springbank/
├── account/         # Gestión de cuentas bancarias
├── customer/        # Gestión de clientes y perfiles
├── transaction/     # Procesamiento de transacciones (core)
│   ├── application/ # Casos de uso y orquestación
│   ├── domain/      # Lógica de negocio y entidades
│   └── infrastructure/ # API REST (Controllers) y Adaptadores de persistencia
├── ledger/          # Sistema de contabilidad (libro mayor)
├── outbox/          # Infraestructura para el patrón Outbox
├── audit/           # Trazabilidad y auditoría de acciones
├── common/          # Clases base, excepciones y utilidades globales
└── SpringBankApplication.java
```

---

## 🚦 Requisitos

- **Java 25**
- **Maven 3.9+**
- **Docker & Docker Compose**

---

## ⚙️ Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone <repository-url>
cd spring-bank
```

### 2. Levantar la infraestructura (PostgreSQL)

```bash
docker-compose up -d
```

### 3. Compilar el proyecto

```bash
./mvnw clean install
```

### 4. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

---

## 🧪 Testing

### Tests unitarios e integración

```bash
./mvnw test
```

### Tests de aceptación (Cucumber)

```bash
./mvnw verify -DskipUnitTests
```

### Tests de contrato (XSD)

Valida que las peticiones y respuestas cumplan con los esquemas definidos en `src/main/resources/xsd/`.

```bash
./mvnw test -Dtest=TransactionXsdValidationTest
```

### Tests de carga (Gatling)

```bash
./mvnw gatling:test
```

O mediante el script de automatización:

```bash
chmod +x run-loadtest.sh
./run-loadtest.sh
```

---

## 📖 API Reference

### Crear transacción

**POST** `/api/v1/transaction`  
**Content-Type**: `application/xml`

**Request:**

```xml
<Transaction xmlns="http://bank.com/transaction">
    <id>550e8400-e29b-41d4-a716-446655440000</id>
    <fromAccount>ES1234567890</fromAccount>
    <toAccount>ES0987654321</toAccount>
    <amount>100.00</amount>
    <type>DEBIT</type>
    <createdAt>2026-03-18T10:45:30</createdAt>
</Transaction>
```

---

## 📈 Monitoreo y Salud

- **Health Check**: `http://localhost:8080/actuator/health`
- **Métricas**: `http://localhost:8080/actuator/metrics`
