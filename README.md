# Ticket Booking System (Spring Boot + MySQL + Thymeleaf)

CRUD app: book, list, edit and delete tickets.

## 1. Prerequisites (Windows)
- JDK 17+ : `java -version`
- Maven 3.9+ : `mvn -version` (or use the Maven built into IntelliJ/Eclipse/VS Code Java extensions)
- MySQL 8 running (Windows: Services -> MySQL80 -> Start, or `net start MySQL80` in an admin terminal)

## 2. Create the database
Option A (Workbench): open and run `sql/setup.sql`.
Option B (CLI): `mysql -u root -p < sql\setup.sql`
Option C: do nothing; the JDBC URL contains `createDatabaseIfNotExist=true`, so MySQL creates `ticket_booking_db` on first start.

Table `tickets` is created by Hibernate (`spring.jpa.hibernate.ddl-auto=update`). That is convenient for local development; use `validate` plus SQL scripts in production.

## 3. Configure credentials
`application.properties` reads environment variables (defaults: user `root`, empty password).

PowerShell:
```
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
```
CMD: `set DB_PASSWORD=your_mysql_password`
In IntelliJ: Run > Edit Configurations > Environment variables.

## 4. Build and run
```
mvn clean test      # tests use in-memory H2, no MySQL needed
mvn spring-boot:run
```
or `mvn clean package` then `java -jar target\ticket-booking-system-1.0.0.jar`.

Open http://localhost:8080/ticket and http://localhost:8080/tickets.

## 5. Sample data (optional)
Disabled by default. Enable: `$env:SAMPLE_DATA="true"` before running.
Rows are inserted only if the table is empty, so restarts never duplicate them. Unset it (or set `false`) to disable.

## 6. Manual CRUD test checklist
1. Open `/ticket`, submit valid data -> redirected to `/tickets` with a green success message.
2. Add 2-3 tickets; IDs increase and the list is sorted ascending.
3. Submit empty fields, bad email (`abc`), bad phone (`12`) -> errors shown, nothing saved.
4. Click Edit -> fields prefilled; change a value, Update -> success message, same ID.
5. Click Delete -> confirm dialog; Cancel keeps it, OK deletes only that row.
6. Delete all -> "No tickets available. Book your first ticket."
7. Stop and restart the app -> data is still there (MySQL).
8. Visit `/tickets/edit/9999` and `/tickets/edit/abc` -> redirected with an error message.
9. Resize the browser/phone view -> table scrolls horizontally.

## 7. Troubleshooting
| Problem | Fix |
|---|---|
| `Access denied for user 'root'` | Set `DB_USERNAME`/`DB_PASSWORD` correctly. |
| `Communications link failure` | MySQL not running or wrong host/port. |
| Port 8080 in use | Run with `--server.port=8081`. |
| `release version 17 not supported` | Your JDK is older than 17; install JDK 17+ and set JAVA_HOME. |
| `mvn` not recognized | Add Maven `bin` to PATH or use your IDE's Maven. |
| Unknown database | Run `sql/setup.sql` or check the URL. |

## Structure
`controller` (routes) -> `service` (logic) -> `repository` (Spring Data JPA) -> `entity` (Ticket). Views: `templates/*.html`, styles: `static/css/style.css`.
