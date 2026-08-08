package gr.uoa.di.citizen;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import gr.uoa.di.citizen.domain.Citizen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CitizenIntegrationTests {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api/citizens";
    }

    @Test
    public void testAllCitizenEndpoints() {
        Citizen citizen = new Citizen("99999999", "Giannis", "Antetokounmpo", "Male", "06-12-1994", "123456789", "Athina");

        // 1. ΔΟΚΙΜΗ POST (Εισαγωγή Πολίτη)
        given()
            .contentType(ContentType.JSON)
            .body(citizen)
        .when()
            .post()
        .then()
            .statusCode(201)
            .body("at", equalTo("99999999"))
            .body("firstName", equalTo("Giannis"));

        // 2. ΔΟΚΙΜΗ GET /{at} (Εμφάνιση με βάση τον ΑΤ)
        given()
        .when()
            .get("/99999999")
        .then()
            .statusCode(200)
            .body("lastName", equalTo("Antetokounmpo"));

        // 3. ΔΟΚΙΜΗ PUT /{at} (Ενημέρωση ΑΦΜ & Διεύθυνσης)
        Citizen updatedData = new Citizen();
        updatedData.setAfm("987654321");
        updatedData.setAddress("Thessaloniki");

        given()
            .contentType(ContentType.JSON)
            .body(updatedData)
        .when()
            .put("/99999999")
        .then()
            .statusCode(200)
            .body("afm", equalTo("987654321"))
            .body("address", equalTo("Thessaloniki"));

        // 4. ΔΟΚΙΜΗ GET /search (Αναζήτηση)
        given()
            .queryParam("firstName", "Giannis")
        .when()
            .get("/search")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(1));

        // 5. ΔΟΚΙΜΗ DELETE /{at} (Διαγραφή)
        given()
        .when()
            .delete("/99999999")
        .then()
            .statusCode(200);
            
        // Επιβεβαίωση ότι διαγράφηκε (πρέπει να επιστρέψει 404)
        given()
        .when()
            .get("/99999999")
        .then()
            .statusCode(404);
    }
}