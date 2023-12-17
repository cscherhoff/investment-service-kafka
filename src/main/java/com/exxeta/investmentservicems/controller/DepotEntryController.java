package com.exxeta.investmentservicems.controller;

import com.exxeta.investmentservice.service.InvestmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/investments")
public class DepotEntryController {

    private final InvestmentService investmentService;
    private final ObjectMapper mapper = new ObjectMapper();

    public DepotEntryController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @GetMapping(path = "/depotEntries")
    public String getAllDepotEntries(@RequestParam String userId) {
        try {
            return mapper.writeValueAsString(investmentService.getAllDepotEntries(userId));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @GetMapping(path = "/depotEntries/{depotName}")
    public String getAllDepotEntriesForAGivenDepot(@RequestParam String userId, @PathVariable String depotName) {
        try {
            return mapper.writeValueAsString(investmentService.getAllDepotEntries(userId, depotName));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
