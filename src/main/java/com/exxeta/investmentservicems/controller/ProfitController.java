package com.exxeta.investmentservicems.controller;

import com.exxeta.investmentservice.service.InvestmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/investments/user/{userId}")
public class ProfitController {

    private final InvestmentService investmentService;
    private final ObjectMapper mapper = new ObjectMapper();

    public ProfitController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @GetMapping(path = "/profit")
    public String getAllProfits(@PathVariable long userId) {
        try {
            return mapper.writeValueAsString(investmentService.getAllProfits(userId));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
