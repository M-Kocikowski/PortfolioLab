package pl.coderslab.charity.controller;

import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.repository.CategoryRepository;
import pl.coderslab.charity.repository.InstitutionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/donation")
public class DonationRestController {

    private InstitutionRepository institutionRepository;
    private CategoryRepository categoryRepository;

    public DonationRestController(InstitutionRepository institutionRepository, CategoryRepository categoryRepository) {
        this.institutionRepository = institutionRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/institutions")
    public List<Institution> getAllInstitutions(@RequestParam(defaultValue = "") String categories) {
        if (categories.isEmpty()){
            return null;
        }
        String[] categoriesArray = categories.split(",");
        Arrays.stream(categoriesArray).forEach(System.out::println);
        List<Institution> institutionList =
                institutionRepository.getInstitutionsByCategories(Long.parseLong(categoriesArray[0]));
        for (int i = 1; i < categoriesArray.length; i++) {
            Category categoryById = categoryRepository.findById(Long.parseLong(categoriesArray[i])).orElse(null);
            institutionList = institutionList.stream()
                    .filter(institution ->
                            institution.getCategories().indexOf(categoryById) != -1)
                    .collect(Collectors.toList());
        }
        return institutionList;
    }
}
