package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.repository.CategoryRepository;
import pl.coderslab.charity.repository.InstitutionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/donation")
public class DonationController {

    private InstitutionRepository institutionRepository;
    private CategoryRepository categoryRepository;

    public DonationController(InstitutionRepository institutionRepository, CategoryRepository categoryRepository) {
        this.institutionRepository = institutionRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping()
    public String donationAction(Model model) {
        model.addAttribute("donation", new Donation());
        return "form";
    }

    @GetMapping("/categories")
    @ResponseBody
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/institutions")
    @ResponseBody
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
