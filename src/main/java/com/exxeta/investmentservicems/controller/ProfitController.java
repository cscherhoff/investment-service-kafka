package com.exxeta.investmentservicems.controller;

import com.exxeta.investmentservice.service.InvestmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/investments")
public class ProfitController {

    private final InvestmentService investmentService;
    private final ObjectMapper mapper = new ObjectMapper();

    public ProfitController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @GetMapping(path = "/profit")
    public String getAllProfits(@RequestParam String userId) {
        try {
            return mapper.writeValueAsString(investmentService.getAllProfits(userId));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
