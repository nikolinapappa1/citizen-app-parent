package gr.uoa.di.citizen.repository;

import gr.uoa.di.citizen.domain.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenRepository extends JpaRepository<Citizen, String> {
    // Το JpaRepository μας δίνει έτοιμες τις μεθόδους save(), findAll(), findById() κλπ.
}