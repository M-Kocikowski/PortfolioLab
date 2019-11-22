package pl.coderslab.charity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.charity.entity.Donation;
import pl.coderslab.charity.repository.DonationRepository;

@Controller
@RequestMapping("/donation")
public class DonationController {

    private DonationRepository donationRepository;

    public DonationController(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @GetMapping()
    public String donationAction(Model model) {
        model.addAttribute("donation", new Donation());
        return "form";
    }

    @PostMapping(consumes = "application/json")
    public String postDonation(@RequestBody Donation donation){
        donationRepository.save(donation);
        return "form-confirmation";
    }

}
