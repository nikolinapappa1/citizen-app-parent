package gr.uoa.di.citizen.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "citizens")
public class Citizen {

    @Id
    @NotBlank(message = "Ο ΑΤ είναι υποχρεωτικό πεδίο.")
    @Size(min = 8, max = 8, message = "Ο ΑΤ πρέπει να αποτελείται από 8 χαρακτήρες.")
    private String at;

    @NotBlank(message = "Το όνομα είναι υποχρεωτικό πεδίο.")
    private String firstName;

    @NotBlank(message = "Το επίθετο είναι υποχρεωτικό πεδίο.")
    private String lastName;

    @NotBlank(message = "Το φύλο είναι υποχρεωτικό πεδίο.")
    private String gender;

    @NotBlank(message = "Η ημερομηνία γέννησης είναι υποχρεωτικό πεδίο.")
    private String birthDate;

    private String afm;

    private String address;

    // Προκαθορισμένος δομητής για το JPA
    public Citizen() {
    }

    // Πλήρης δομητής για τη δημιουργία αντικειμένων
    public Citizen(String at, String firstName, String lastName, String gender, String birthDate, String afm, String address) {
        this.at = at;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.afm = afm;
        this.address = address;
    }

    // Μέθοδοι Getter και Setter
    public String getAt() { return at; }
    public void setAt(String at) { this.at = at; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getAfm() { return afm; }
    public void setAfm(String afm) { this.afm = afm; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}