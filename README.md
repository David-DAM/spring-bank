# Spring Bank

Sistema bancario modular desarrollado con **Spring Boot 4.0.3** y **Java 25**, implementando:

- Arquitectura hexagonal (Ports & Adapters)
- Contabilidad de doble entrada (double-entry ledger)
- Procesamiento de transacciones mediante XML (JAXB)

---

# Características principales

- **Arquitectura Hexagonal (Ports & Adapters)**  
  Separación clara entre dominio, aplicación e infraestructura

- **Contabilidad de doble entrada**  
  Sistema de ledger que garantiza integridad financiera

- **API REST con XML**  
  Endpoints que consumen y producen XML

- **Validación con XML Schema (XSD)**  
  Contratos estrictos para requests/responses

- **Patrón Outbox**  
  Gestión de eventos transaccionales

- **Idempotencia**  
  Prevención de transacciones duplicadas

- **Testing completo**  
  Unit, integración, contrato y carga

- **Base de datos H2**  
  Para desarrollo y testing rápido

---

# Requisitos

- Java 25
- Maven 3.6+
- Spring Boot 4.0.3

---

# Tecnologías utilizadas

- Jakarta EE (Persistence, XML Binding)
- Spring Data JPA
- Spring Web MVC
- Lombok
- MapStruct
- H2 Database
- Cucumber (BDD testing)
- Gatling (performance and load testing)

---

# Estructura del proyecto

```
src/main/java/com/davinchicoder/springbank/
├── account/ # Gestión de cuentas
├── customer/ # Gestión de clientes
├── transaction/ # Procesamiento de transacciones
│ ├── application/ # Casos de uso
│ ├── domain/ # Lógica de negocio
│ └── infrastructure # Controllers y repositorios
├── ledger/ # Sistema de doble entrada
├── outbox/ # Eventos (Outbox Pattern)
└── common/ # Utilidades compartidas
```

---

# Instalación y ejecución

## Clonar repositorio

```bash
git clone <repository-url>
cd spring-bank
```

Compilar

```bash
./mvnw clean install
```

Ejecutar aplicación

```bash
./mvnw spring-boot:run
```

Disponible en: http://localhost:8080

## Testing

### Tests unitarios e integración

```bash
./mvnw test
```

### Tests de contrato (XSD)

Validan que el XML cumple con los schemas definidos.

```bash
./mvnw test -Dtest=TransactionXsdValidationTest
```

### Tests de carga (Gatling)

```bash
./mvnw gatling:test
```

Script completo de carga

```bash
./run-loadtest.sh
```

Este script:

- Inicia la aplicación
- Espera a que esté disponible (health check)
- Ejecuta Gatling
- Detiene la aplicación

## API

Crear transacción

POST /api/v1/transaction
Content-Type: application/xml

Request

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

Response

```xml

<TransactionResponse xmlns="http://bank.com/transaction">
    <id>550e8400-e29b-41d4-a716-446655440000</id>
    <status>COMPLETED</status>
    <createdAt>2026-03-18T10:45:30Z</createdAt>
</TransactionResponse>
```

## Seguridad y confiabilidad

### Idempotencia

Evita duplicados por ID

### Transaccionalidad (@Transactional)

Garantiza ACID

### Validación de balance

Débito = Crédito
Retry automático
Reintentos en fallos transitorios
Monitoreo

### Spring Boot Actuator habilitado:

- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actuator/metrics

## Patrones de diseño

### Double-Entry Ledger

Cada transacción genera:

DEBIT → resta en cuenta origen
CREDIT → suma en cuenta destino

- Garantiza integridad
- Permite auditoría completa

### Outbox Pattern

Los eventos se almacenan en la misma transacción que el negocio.

- Evita pérdida de eventos
- Garantiza consistencia eventual
