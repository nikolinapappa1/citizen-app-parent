package gr.uoa.di.citizen;

import gr.uoa.di.citizen.domain.Citizen;
import gr.uoa.di.citizen.repository.CitizenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CitizenTests {

    @Autowired
    private CitizenRepository citizenRepository;

    // 1. Έλεγχος ότι ο Constructor και οι Getters/Setters δουλεύουν σωστά
    @Test
    public void testCitizenConstructorAndGetters() {
        Citizen citizen = new Citizen("12345678", "Nikos", "Papadopoulos", "Male", "15-05-1990", "123456789", "Athina");
        assertEquals("12345678", citizen.getAt());
        assertEquals("Nikos", citizen.getFirstName());
        assertEquals("Papadopoulos", citizen.getLastName());
        assertEquals("Male", citizen.getGender());
        assertEquals("15-05-1990", citizen.getBirthDate());
        assertEquals("123456789", citizen.getAfm());
        assertEquals("Athina", citizen.getAddress());
    }

    // 2. Έλεγχος Πλήρους Κύκλου (Save, Find, Update, Delete) στη Βάση Δεδομένων
    @Test
    public void testFullRepositoryCRUD() {
        // --- CREATE (Αποθήκευση) ---
        Citizen citizen = new Citizen("87654321", "Maria", "Anagnostou", "Female", "20-10-1995", "987654321", "Patra");
        citizenRepository.save(citizen);
        
        // --- READ (Ανάγνωση) ---
        Citizen found = citizenRepository.findById("87654321").orElse(null);
        assertNotNull(found, "Ο πολίτης θα έπρεπε να βρεθεί στη βάση");
        assertEquals("Maria", found.getFirstName());
        
        // --- UPDATE (Ενημέρωση) ---
        found.setAddress("Thessaloniki");
        found.setAfm("000000000");
        citizenRepository.save(found);
        
        Citizen updated = citizenRepository.findById("87654321").orElse(null);
        assertNotNull(updated);
        assertEquals("Thessaloniki", updated.getAddress(), "Η διεύθυνση έπρεπε να έχει αλλάξει");
        assertEquals("000000000", updated.getAfm(), "Το ΑΦΜ έπρεπε να έχει αλλάξει");
        
        // --- DELETE (Διαγραφή) ---
        citizenRepository.delete(updated);
        Citizen afterDelete = citizenRepository.findById("87654321").orElse(null);
        assertNull(afterDelete, "Ο πολίτης θα έπρεπε να έχει διαγραφεί από τη βάση");
    }
}