package gr.uoa.di.citizen;

import gr.uoa.di.citizen.domain.Citizen;
import org.springframework.web.client.RestTemplate;
import java.util.Scanner;

public class CitizenClient {

    private static final String BASE_URL = "http://localhost:8080/api/citizens";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Καλωσήρθατε στον Client Διαχείρισης Πολιτών ===");

        while (true) {
            System.out.println("\n--- ΜΕΝΟΥ ΕΠΙΛΟΓΩΝ ---");
            System.out.println("1. Εισαγωγή νέου πολίτη");
            System.out.println("2. Διαγραφή πολίτη (με ΑΤ)");
            System.out.println("3. Ενημέρωση πολίτη (ΑΦΜ & Διεύθυνση)");
            System.out.println("4. Εμφάνιση πολίτη (με ΑΤ)");
            System.out.println("5. Έξοδος (Οποιαδήποτε άλλη επιλογή τερματίζει το πρόγραμμα)");
            System.out.print("Παρακαλώ επιλέξτε (1-5): ");

            String choice = scanner.nextLine();

            try {
                if (choice.equals("1")) {
                    Citizen c = new Citizen();
                    System.out.print("Δώστε ΑΤ (8 χαρακτήρες): "); c.setAt(scanner.nextLine());
                    System.out.print("Δώστε Όνομα: "); c.setFirstName(scanner.nextLine());
                    System.out.print("Δώστε Επίθετο: "); c.setLastName(scanner.nextLine());
                    System.out.print("Δώστε Φύλο: "); c.setGender(scanner.nextLine());
                    System.out.print("Δώστε Ημερομηνία Γέννησης (ΧΧ-ΥΥ-ΚΚΚΚ): "); c.setBirthDate(scanner.nextLine());
                    System.out.print("Δώστε ΑΦΜ (9 ψηφία, προαιρετικό): "); c.setAfm(scanner.nextLine());
                    System.out.print("Δώστε Διεύθυνση (προαιρετικό): "); c.setAddress(scanner.nextLine());

                    Citizen response = restTemplate.postForObject(BASE_URL, c, Citizen.class);
                    System.out.println("Επιτυχής εισαγωγή! Πολίτης: " + response.getFirstName() + " " + response.getLastName());

                } else if (choice.equals("2")) {
                    System.out.print("Δώστε τον ΑΤ του πολίτη προς διαγραφή: ");
                    String at = scanner.nextLine();
                    restTemplate.delete(BASE_URL + "/" + at);
                    System.out.println("Η εντολή διαγραφής στάλθηκε επιτυχώς.");

                } else if (choice.equals("3")) {
                    System.out.print("Δώστε τον ΑΤ του πολίτη προς ενημέρωση: ");
                    String at = scanner.nextLine();
                    Citizen c = new Citizen();
                    System.out.print("Δώστε νέο ΑΦΜ: "); c.setAfm(scanner.nextLine());
                    System.out.print("Δώστε νέα Διεύθυνση: "); c.setAddress(scanner.nextLine());

                    restTemplate.put(BASE_URL + "/" + at, c);
                    System.out.println("Η εντολή ενημέρωσης στάλθηκε επιτυχώς.");

                } else if (choice.equals("4")) {
                    System.out.print("Δώστε τον ΑΤ του πολίτη προς εμφάνιση: ");
                    String at = scanner.nextLine();
                    String response = restTemplate.getForObject(BASE_URL + "/" + at, String.class);
                    System.out.println("Στοιχεία Πολίτη: " + response);

                } else {
                    System.out.println("Έξοδος. Το πρόγραμμα τερματίζεται. Αντίο!");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Αποτυχία λειτουργίας: " + e.getMessage());
            }
        }
        scanner.close();
    }
}