package pl.coderslab.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.coderslab.charity.entity.Institution;

import java.util.List;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    @Query("SELECT DISTINCT i FROM Institution i JOIN i.categories c WHERE c.id IN ?1 ")
    public List<Institution> getInstitutionsByCategories(Long categoryId);
}
