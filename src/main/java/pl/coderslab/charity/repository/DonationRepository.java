package pl.coderslab.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.charity.entity.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("SELECT SUM(d.quantity) FROM Donation d")
    public Integer getQuantity();

    @Query("SELECT COUNT(DISTINCT d.institution) FROM Donation d")
    public Integer getInstitutions();
}
