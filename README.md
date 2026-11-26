# First Java CRUD REST

Bhishma Pratap Singh Parihar · Vistula University · Student ID **73195**
Team Project – IT

This Spring Boot application manages customers and extends the original product CRUD project. Swagger UI sends requests to the same controllers and H2 database as other HTTP clients. The in-memory database starts empty and resets when the application stops.

## Run locally

Install a JDK (Java 17 or 21). From the repository root, run:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell use `.\mvnw.cmd spring-boot:run`. The first build downloads Maven and project dependencies. Open [Swagger UI](http://localhost:8080/swagger-ui/index.html) to try the endpoints. Run `./mvnw test` or `.\mvnw.cmd test` to execute the automated tests.

## Customer API

| Method | Path | Success |
| --- | --- | --- |
| GET | `/api/customers` | 200 with customers in ID order |
| GET | `/api/customers?name=John` | 200 with case-insensitive substring matches in either name, or an empty list |
| GET | `/api/customers/{id}` | 200 with a single customer |
| POST | `/api/customers` | 201 with the new customer and a Location header |
| PUT | `/api/customers/{id}` | 200 with the updated customer |
| DELETE | `/api/customers/{id}` | 204 with an empty body |
| GET | `/api/info` | 200 with studentId, applicationName and version. Public access. |

Send JSON with firstName, lastName and email on both POST and PUT. The server assigns the ID. Blank fields, an invalid email address or malformed JSON return 400. GET, PUT and DELETE return 404 for a missing ID. The original product CRUD routes are available under `/api/v1/products`.

## Try a complete request sequence

In Bash or Git Bash, start with an empty database or replace 1 with the ID returned by POST.

```bash
api=http://localhost:8080/api
curl -i "$api/info"
curl -i -X POST "$api/customers" -H 'Content-Type: application/json' \
  -d '{"firstName":"John","lastName":"Nowak","email":"john@example.org"}'
curl -i "$api/customers"
curl -i "$api/customers/1"
curl -i "$api/customers?name=joh"
curl -i -X PUT "$api/customers/1" -H 'Content-Type: application/json' \
  -d '{"firstName":"John","lastName":"Kowalski","email":"john.k@example.org"}'
curl -i -X DELETE "$api/customers/1"
curl -i "$api/customers/1"
```

## Test evidence

See [doc](doc/) for local API screenshots.

## Pair contributions

| Required merged pull request | Status |
| --- | --- |
| Partner implements GET /api/customers/statistics here | Awaiting partner pull request |
| Student 73195 implements equivalent statistics in partner repository | Awaiting partner repository |

The statistics response must include a live database count as totalCustomers and the implementing student's ID as contributorStudentId. Both merged PR links and the statistics screenshot belong here before submission.
