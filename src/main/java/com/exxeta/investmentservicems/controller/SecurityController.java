package com.exxeta.investmentservicems.controller;

import com.exxeta.investmentservice.service.InvestmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/investments/securities")
public class SecurityController {

    private final InvestmentService investmentService;
    private final ObjectMapper mapper = new ObjectMapper();

    public SecurityController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @GetMapping
    public String getAllSecurities(@RequestParam String userId) {
        try {
            return mapper.writeValueAsString(investmentService.getAllSecurities(userId));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
