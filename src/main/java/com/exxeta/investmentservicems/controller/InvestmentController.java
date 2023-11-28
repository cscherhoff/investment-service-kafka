package com.exxeta.investmentservicems.controller;

import com.exxeta.investmentservice.entities.Investment;
import com.exxeta.investmentservice.service.InvestmentHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/investments/user/{userId}")
public class InvestmentController {

    private final ObjectMapper mapper = new ObjectMapper();

    private final InvestmentHandler investmentHandler;

    private final Logger logger = LoggerFactory.getLogger(InvestmentController.class);

    public InvestmentController(InvestmentHandler investmentHandler) {this.investmentHandler = investmentHandler;}

    @PostMapping(path = "/invested")
    @ResponseStatus(HttpStatus.CREATED)
    public String postInvested(@PathVariable String userId, @Valid @RequestBody Investment investment) {
        logger.info("Received investment: " + investment);
        try {
            investment.userId = userId;
            investmentHandler.handleNewInvestment(investment);
            return mapper.writeValueAsString(investment);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @GetMapping(path = "/invested")
    public String getInformations(@PathVariable String userId) {
        try {
            return mapper.writeValueAsString(investmentHandler.getInvestedInformation(userId));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
