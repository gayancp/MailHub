# Backend – Campaign Home Page API

Build the Spring Boot backend that powers the home page: listing all email campaigns as tiles with their sending-completion percentage.

## User Review Required

> [!IMPORTANT]
> **MySQL Configuration**: You'll need a local MySQL server running. The app will auto-create the schema via JPA (`spring.jpa.hibernate.ddl-auto=update`). Please confirm the following defaults are acceptable, or provide your own:
> - DB name: `mailhub`
> - Username: `root`
> - Password: *(empty — or provide yours)*
> - Port: `3306`

> [!NOTE]
> Since this is for **sole/local usage**, there is no authentication layer. CORS is configured to allow the React frontend on `localhost:5173` (Vite default).

## Proposed Changes

### 1. Maven Dependencies (pom.xml)

#### [MODIFY] [pom.xml](file:///c:/Users/Gayan chinthaka/Documents/springboot projects/MailHub/bakend/mailhub/pom.xml)
Add the following dependencies:
- `spring-boot-starter-data-jpa` — JPA/Hibernate ORM
- `mysql-connector-j` — MySQL JDBC driver
- `spring-boot-starter-validation` — Bean validation (`@NotBlank`, etc.)
- `lombok` — Boilerplate reduction (optional, via `compileOnly` + annotation processor)

---

### 2. Application Configuration

#### [MODIFY] [application.properties](file:///c:/Users/Gayan chinthaka/Documents/springboot projects/MailHub/bakend/mailhub/src/main/resources/application.properties)
Configure MySQL datasource, JPA/Hibernate settings, and server port (`8080`).

---

### 3. Domain Model — `Campaign` Entity

#### [NEW] Campaign.java
`com.example.gayanchinthaka.mailhub.backend.model.Campaign`

| Field | Type | Description |
|---|---|---|
| `id` | `Long` (PK, auto) | Primary key |
| `name` | `String` | Campaign name |
| `description` | `String` | Short description |
| `status` | `Enum(DRAFT, RUNNING, PAUSED, COMPLETED)` | Current state |
| `totalEmails` | `int` | Total emails in the list |
| `sentEmails` | `int` | Number sent so far |
| `deliveredEmails` | `int` | Number confirmed delivered |
| `clickedEmails` | `int` | Number with link clicks |
| `templateContent` | `@Lob String` | Email template body |
| `isHtml` | `boolean` | HTML vs plain-text toggle |
| `sendIntervalMs` | `long` | Interval between sends (ms) |
| `createdAt` | `LocalDateTime` | Creation timestamp |
| `updatedAt` | `LocalDateTime` | Last update timestamp |

The **completion percentage** is a derived field: `(sentEmails / totalEmails) * 100`.

---

### 4. Repository Layer

#### [NEW] CampaignRepository.java
`com.example.gayanchinthaka.mailhub.backend.repository.CampaignRepository`

- Extends `JpaRepository<Campaign, Long>`
- Custom query: `findByStatus(CampaignStatus status)` — for filtering

---

### 5. DTO Layer

#### [NEW] CampaignSummaryDTO.java
`com.example.gayanchinthaka.mailhub.backend.dto.CampaignSummaryDTO`

A lightweight projection for the home page tiles:
```
id, name, description, status, totalEmails, sentEmails, completionPercentage
```

#### [NEW] CampaignCreateDTO.java
`com.example.gayanchinthaka.mailhub.backend.dto.CampaignCreateDTO`

For creating a new campaign:
```
name, description
```

#### [NEW] CampaignDetailDTO.java
`com.example.gayanchinthaka.mailhub.backend.dto.CampaignDetailDTO`

Full campaign details (used by the campaign dashboard later):
```
all Campaign fields + completionPercentage
```

---

### 6. Service Layer

#### [NEW] CampaignService.java
`com.example.gayanchinthaka.mailhub.backend.service.CampaignService`

| Method | Description |
|---|---|
| `getAllCampaigns()` | Returns `List<CampaignSummaryDTO>` for home page tiles |
| `getCampaignsByStatus(status)` | Filtered list by status |
| `getCampaignById(id)` | Returns `CampaignDetailDTO` |
| `createCampaign(dto)` | Creates a new campaign in DRAFT status |
| `updateCampaign(id, dto)` | Updates campaign fields |
| `deleteCampaign(id)` | Deletes a campaign |

---

### 7. REST Controller

#### [NEW] CampaignController.java
`com.example.gayanchinthaka.mailhub.backend.controller.CampaignController`

| Endpoint | Method | Description |
|---|---|---|
| `GET /api/campaigns` | GET | List all campaigns (home page tiles) |
| `GET /api/campaigns?status=RUNNING` | GET | Filter by status |
| `GET /api/campaigns/{id}` | GET | Get full campaign details |
| `POST /api/campaigns` | POST | Create a new campaign |
| `PUT /api/campaigns/{id}` | PUT | Update an existing campaign |
| `DELETE /api/campaigns/{id}` | DELETE | Delete a campaign |

---

### 8. CORS & Exception Handling

#### [NEW] WebConfig.java
`com.example.gayanchinthaka.mailhub.backend.config.WebConfig`

- CORS: allow `http://localhost:5173` (React dev server)
- Allow all methods & headers for local development

#### [NEW] GlobalExceptionHandler.java
`com.example.gayanchinthaka.mailhub.backend.exception.GlobalExceptionHandler`

- `@RestControllerAdvice` to handle `EntityNotFoundException`, validation errors, etc.
- Returns consistent JSON error responses

#### [NEW] ResourceNotFoundException.java
`com.example.gayanchinthaka.mailhub.backend.exception.ResourceNotFoundException`

- Custom runtime exception for 404 scenarios

---

## File Summary

| # | File | Action |
|---|---|---|
| 1 | `pom.xml` | MODIFY |
| 2 | `application.properties` | MODIFY |
| 3 | `Campaign.java` (model) | NEW |
| 4 | `CampaignStatus.java` (enum) | NEW |
| 5 | `CampaignRepository.java` | NEW |
| 6 | `CampaignSummaryDTO.java` | NEW |
| 7 | `CampaignCreateDTO.java` | NEW |
| 8 | `CampaignDetailDTO.java` | NEW |
| 9 | `CampaignService.java` | NEW |
| 10 | `CampaignController.java` | NEW |
| 11 | `WebConfig.java` | NEW |
| 12 | `GlobalExceptionHandler.java` | NEW |
| 13 | `ResourceNotFoundException.java` | NEW |

## Verification Plan

### Automated Tests
```bash
./mvnw spring-boot:run
```
Then test with curl:
```bash
# Create a campaign
curl -X POST http://localhost:8080/api/campaigns -H "Content-Type: application/json" -d '{"name":"Summer Sale","description":"Q3 promo blast"}'

# List all campaigns
curl http://localhost:8080/api/campaigns

# Get single campaign
curl http://localhost:8080/api/campaigns/1
```

### Manual Verification
- Verify MySQL `mailhub` database has `campaigns` table auto-created
- Verify JSON response includes `completionPercentage` field
- Verify CORS headers are present for cross-origin requests from React
