# 🚀 CAMPUS API Test Automation – Rest Assured Project

This project is a comprehensive **API test automation suite** designed to test the **CAMPUS system’s REST APIs** using **Java**, **REST Assured**, and **TestNG**.
It validates business-critical functionalities of the campus management system by executing API requests and asserting responses.

---

## 🎯 Project Goals

* Automate API validation of core CAMPUS modules (Login, Parameters, Entrance Exams, Student Groups, Education Standards, Grading Schemes, Incidents, etc.)
* Ensure end-to-end CRUD operations work as expected for all endpoints.
* Perform functional assertions, response time checks, and error validations.
* Log all API requests and responses for transparent reporting and debugging.

---

## ⚙️ Technologies & Tools

* **Java 17+**
* **Maven**
* **REST Assured**
* **TestNG**
* **Hamcrest**
* **Allure (optional for reporting)**

---

## 🗂️ Project Structure & Tested Scenarios

### 1. Environment & Configuration

* **Base URL:** `https://test.mersys.io`
* **Default Content-Type:** `application/json`
* **Role:** `Admin`
* **Username / Password:** `turkeyts / TechnoStudy123`
* **Authorization:** JWT Bearer Token is obtained via login API.

### 2. Sample Directory Layout

```
src
 └─ test
     ├─ java
     │   └─ Model
     │        ├─ BaseTest.java
     │        ├─ Login.java
     │        ├─ Country.java
     │        ├─ State.java
     │        ├─ City.java
     │        ├─ EntranceExam.java
     │        ├─ CustomFieldGroup.java
     │        ├─ StudentGroup.java
     │        ├─ EducationStandard.java
     │        ├─ GradingScheme.java
     │        └─ IncidentType.java
     └─ resources
         └─ testng.xml
```

---

## 🚀 Tested Endpoints & Acceptance Criteria

| Module                | Endpoint Examples                       | Assertions                                                            |
| --------------------- | --------------------------------------- | --------------------------------------------------------------------- |
| **Login**             | `/auth/login`                           | 400 on invalid, 200 on valid login, token retrieval.                  |
| **Country**           | `/api/countries`                        | POST returns 201, correct state list.                                 |
| **State**             | `/api/states`                           | CRUD: 201 add, 200 update, 200/204 delete, <1000 ms GET.              |
| **City**              | `/api/cities`                           | Full CRUD, <1000 ms list, data integrity of city-state-country.       |
| **EntranceExam**      | `/api/exams`                            | Mandatory fields check, 201 add, 200 update, 404 invalid edit/delete. |
| **CustomFields**      | `/api/custom-field-groups`              | OrderNo, name duplicate handling, CRUD with 201,200,204.              |
| **StudentGroup**      | `/api/student-groups` + `/add-students` | SchoolID mandatory, 201 creation, 200 updates, 400 on invalid delete. |
| **EducationStandard** | `/api/education-standard`               | CRUD with validations on school ID, name, description.                |
| **GradingScheme**     | `/api/grading-schemes`                  | GET by school, POST with grading type, PUT edit, DELETE checks.       |
| **IncidentType**      | `/api/incident-type`                    | CRUD with min/max negative score, 400 on invalid delete.              |

---

## 📝 Key Test Highlights

* **Negative Testing:** Invalid IDs for edits & deletes return appropriate 400 / 404 errors with meaningful JSON messages.
* **Timing Tests:** State, City, and Custom Field Group lists must return in under `1000ms`.
* **Uniqueness Checks:** CustomField prevents duplicate `orderNo` or `name`.
* **Data Integrity:** Ensures correct mapping of state -> city -> country relations.

---

## 📈 Sample Assertions (REST Assured + Hamcrest)

```java
given()
    .spec(spec)
    .contentType(ContentType.JSON)
    .body(stateMap)
.when()
    .post("/school-service/api/states")
.then()
    .statusCode(201)
    .body("name", startsWith("SampleState"))
    .body("code", startsWith("SS"))
    .body("country.name", notNullValue());
```

```java
given()
    .spec(spec)
.when()
    .get("/school-service/api/states")
.then()
    .statusCode(200)
    .time(lessThan(1000L));
```

---

## 📂 How to Run

1️⃣ Clone this repository:

```bash
git clone https://github.com/your-username/campus-api-automation.git
```

2️⃣ Navigate to project root and install dependencies:

```bash
mvn clean install
```

3️⃣ Run tests via TestNG:

```bash
mvn test
```

4️⃣ (Optional) Generate Allure reports:

```bash
allure serve target/allure-results
```

---

## 🚀 Key Highlights for QA / CI

* Can be integrated into Jenkins pipelines for nightly runs.
* Easy to parameterize with `testng.xml` for parallel modules (eg: run EntranceExam + State together).
* Extensively logs requests and responses with `.log().all()` for debugging.

---

## 👨‍💻 Contributors

| İsim           | GitHub Profili                                      |
|----------------|------------------------------------------------------|
| Merve Kıtır    | [github.com/mervektr](https://github.com/mervektr)   |
| Tolga Aktaş    | [github.com/aktstlga](https://github.com/aktstlga)   |

---

✅ **This REST Assured automation suite ensures the CAMPUS API platform is reliable, meets business acceptance criteria, and is CI-ready.**
