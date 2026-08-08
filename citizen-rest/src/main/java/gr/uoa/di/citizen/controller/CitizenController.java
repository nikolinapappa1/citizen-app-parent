package gr.uoa.di.citizen.controller;

import gr.uoa.di.citizen.domain.Citizen;
import gr.uoa.di.citizen.repository.CitizenRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/citizens")
public class CitizenController {

    @Autowired
    private CitizenRepository citizenRepository;

    // 1. ΕΙΣΑΓΩΓΗ: Έλεγχος αν υπάρχει ήδη ή αν η ημερομηνία έχει λάθος μορφή
    @PostMapping
    public ResponseEntity<?> createCitizen(@Valid @RequestBody Citizen citizen) {
        if (citizenRepository.existsById(citizen.getAt())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Σφάλμα: Ο πολίτης με ΑΤ " + citizen.getAt() + " υπάρχει ήδη στο μητρώο.");
        }
        
        // Έλεγχος για τη μορφή της ημερομηνίας ΧΧ-ΥΥ-ΚΚΚΚ
        if (citizen.getBirthDate() != null && !citizen.getBirthDate().matches("^\\d{2}-\\d{2}-\\d{4}$")) {
            return ResponseEntity.badRequest()
                    .body("Σφάλμα: Η ημερομηνία γέννησης πρέπει να είναι της μορφής ΧΧ-ΥΥ-ΚΚΚΚ (π.χ. 12-11-2008).");
        }

        // Έλεγχος για το ΑΦΜ (αν δοθεί, πρέπει να έχει 9 ψηφία)
        if (citizen.getAfm() != null && !citizen.getAfm().isEmpty() && !citizen.getAfm().matches("^\\d{9}$")) {
            return ResponseEntity.badRequest()
                    .body("Σφάλμα: Το ΑΦΜ πρέπει να αποτελείται αποκλειστικά από 9 ψηφία.");
        }

        Citizen savedCitizen = citizenRepository.save(citizen);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCitizen);
    }

    // 2. ΔΙΑΓΡΑΦΗ: Με βάση τον ΑΤ
    @DeleteMapping("/{at}")
    public ResponseEntity<?> deleteCitizen(@PathVariable String at) {
        if (at == null || at.trim().isEmpty() || at.length() != 8) {
            return ResponseEntity.badRequest().body("Σφάλμα: Λανθασμένος, κενός ή μη έγκυρος ΑΤ.");
        }
        if (!citizenRepository.existsById(at)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Σφάλμα: Δεν βρέθηκε πολίτης με ΑΤ " + at + " για να διαγραφεί.");
        }
        citizenRepository.deleteById(at);
        return ResponseEntity.ok("Η εγγραφή του πολίτη με ΑΤ " + at + " διαγράφηκε επιτυχώς.");
    }

    // 3. ΕΝΗΜΕΡΩΣΗ: Μόνο για ΑΦΜ & Διεύθυνση κατοικίας με βάση τον ΑΤ
    @PutMapping("/{at}")
    public ResponseEntity<?> updateCitizen(@PathVariable String at, @RequestBody Citizen updatedData) {
        if (!citizenRepository.existsById(at)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Σφάλμα: Δεν υπάρχει πολίτης με ΑΤ " + at + " για ενημέρωση.");
        }
        
        // Έλεγχος εγκυρότητας ΑΦΜ (αν δοθεί)
        if (updatedData.getAfm() != null && !updatedData.getAfm().isEmpty() && !updatedData.getAfm().matches("^\\d{9}$")) {
            return ResponseEntity.badRequest().body("Σφάλμα: Μη έγκυρη τιμή για το ΑΦΜ (πρέπει να είναι 9 ψηφία).");
        }

        Citizen existingCitizen = citizenRepository.findById(at).get();
        
        // Η εκφώνηση λέει ενημέρωση ΜΟΝΟ για ΑΦΜ και Διεύθυνση
        existingCitizen.setAfm(updatedData.getAfm());
        existingCitizen.setAddress(updatedData.getAddress());
        
        citizenRepository.save(existingCitizen);
        return ResponseEntity.ok(existingCitizen);
    }

    // 4. ΕΜΦΑΝΙΣΗ: Επιστροφή στοιχείων με βάση τον ΑΤ
    @GetMapping("/{at}")
    public ResponseEntity<?> getCitizenByAt(@PathVariable String at) {
        if (at == null || at.trim().isEmpty() || at.length() != 8) {
            return ResponseEntity.badRequest().body("Σφάλμα: Ο ΑΤ δεν είναι έγκυρος (πρέπει να έχει 8 χαρακτήρες).");
        }
        return citizenRepository.findById(at)
                .map(citizen -> ResponseEntity.ok((Object) citizen))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Σφάλμα: Δεν βρέθηκε πολίτης με ΑΤ " + at));
    }

    // 5. ΑΝΑΖΗΤΗΣΗ: Με βάση οποιοδήποτε πεδίο ή συνδυασμό πεδίων
    @GetMapping("/search")
    public ResponseEntity<?> searchCitizens(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String afm,
            @RequestParam(required = false) String address) {

        // Φιλτράρισμα στη μνήμη από όλα τα records για δυναμικό συνδυασμό
        List<Citizen> results = citizenRepository.findAll().stream().filter(c -> {
            boolean matches = true;
            if (firstName != null && !firstName.isEmpty()) matches &= c.getFirstName().equalsIgnoreCase(firstName);
            if (lastName != null && !lastName.isEmpty()) matches &= c.getLastName().equalsIgnoreCase(lastName);
            if (gender != null && !gender.isEmpty()) matches &= c.getGender().equalsIgnoreCase(gender);
            if (afm != null && !afm.isEmpty()) matches &= afm.equals(c.getAfm());
            if (address != null && !address.isEmpty()) matches &= c.getAddress().equalsIgnoreCase(address);
            return matches;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }
}