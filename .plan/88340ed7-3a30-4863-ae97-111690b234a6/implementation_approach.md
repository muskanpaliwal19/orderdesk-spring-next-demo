# Implementation Approach

## Health Check via Spring Boot Actuator

### Approach
Use **Spring Boot Actuator** to provide the health check endpoint, mapped to `/api/health` to match the legacy route and the `preview.manifest.json` health check path.

### Dependencies
Add `spring-boot-starter-actuator` to `pom.xml`:
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Configuration (`application.yml`)
```yaml
management:
  endpoints:
    web:
      base-path: /api
      exposure:
        include: health
  endpoint:
    health:
      show-details: always
```

- **`base-path: /api`** — places the health endpoint at `/api/health`, matching both the legacy route and the preview manifest's `healthCheck.path`.
- **`exposure.include: health`** — only the health endpoint is exposed; no other actuator endpoints are accessible.
- **`show-details: always`** — returns component-level status (database connectivity, disk space) so operators can diagnose issues beyond "app is up."

### What It Provides
The auto-configured **DataSource health indicator** (activated by Spring Data JPA on the classpath) runs a lightweight validation query against PostgreSQL on every health check. No custom code needed.

**Response at `GET /api/health`:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": { ... }
    }
  }
}
```

### Differences from Legacy
| Aspect | Legacy | Target |
|--------|--------|--------|
| Route | `GET /api/health` | `GET /api/health` (same) |
| Response shape | `{ ok: true, app, database, migrationSchema }` | `{ status: "UP", components: { db, diskSpace } }` |
| DB check | None (static response) | Real PostgreSQL connectivity check |
| Format | Custom JSON | Standard Actuator format |

The acceptance criteria requires "a successful response confirming the application is operational" — the Actuator `{"status":"UP"}` response satisfies this, and the real DB check is strictly more useful than the legacy static response.

### No Custom Controller Needed
Since Actuator handles everything, no `HealthController` class is created. This avoids route conflicts and keeps the implementation zero-code beyond dependency + configuration.

### Security Note
Per the locked Security decision, Spring Security will be present. The `/api/health` endpoint should be permitted without authentication (standard for health checks used by load balancers and deployment probes). Add to the security configuration:
```java
.requestMatchers("/api/health").permitAll()
```
