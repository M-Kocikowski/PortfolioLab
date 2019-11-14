package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.charity.entity.Category;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.entity.Institution;
import pl.coderslab.charity.repository.CategoryRepository;
import pl.coderslab.charity.repository.InstitutionRepository;

import java.util.List;

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
    public String donationAction(Model model){
        model.addAttribute("donation", new Donation());
        return "form";
    }

    @GetMapping("/categories")
    @ResponseBody
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @GetMapping("/institutions")
    @ResponseBody
    public List<Institution> getAllInstitutions(){
        return institutionRepository.findAll();
    }

}
