package com.exxeta.investmentservicems.controller;

import com.exxeta.investmentservice.entities.AccountMovement;
import com.exxeta.investmentservice.service.AccountMovementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/account-movement/user/{userId}")
public class AccountMovementController {

    private final ObjectMapper mapper = new ObjectMapper();

    private final AccountMovementService accountMovementService;

    private final Logger logger = LoggerFactory.getLogger(InvestmentController.class);

    public AccountMovementController(AccountMovementService accountMovementService) {
        this.accountMovementService = accountMovementService;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public String postAccountMovement(@PathVariable String userId, @RequestBody AccountMovement accountMovement) {
        logger.info("Received account movement " + accountMovement);
        try {
            accountMovement.setUserId(userId);
            accountMovementService.insertAccountMovementToDatabase(accountMovement);
            return "Successfully insert";
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @GetMapping(path = "/")
    public String getAllAccountMovements(@PathVariable String userId) {
        try {
            return mapper.writeValueAsString(accountMovementService.getAllAccountMovements(userId));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
