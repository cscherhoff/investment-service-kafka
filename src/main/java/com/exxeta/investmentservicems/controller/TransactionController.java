package com.exxeta.investmentservicems.controller;

import com.exxeta.investmentservice.entities.Transaction;
import com.exxeta.investmentservice.service.InvestmentService;
import com.exxeta.investmentservice.service.TransactionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/investments/user/{userId}")
public class TransactionController {

    private final ObjectMapper mapper = new ObjectMapper();
    private final InvestmentService investmentService;

    private final TransactionHandler transactionHandler;
//    private final AccountService accountService;

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(InvestmentService investmentService, TransactionHandler transactionHandler) {
        this.investmentService = investmentService;
        this.transactionHandler = transactionHandler;
//        this.accountService = accountService;
    }

    @PostMapping(path = "/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public String addNewTransaction(@PathVariable long userId, @Valid @RequestBody Transaction transaction) {
        logger.info("Received transaction: " + transaction);
        try {
            transaction.setUserId(userId);
            transactionHandler.handleTransaction(transaction);
//            changeAccountBalance(transaction);
            return mapper.writeValueAsString(transaction);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    @GetMapping(path = "/transactions")
    public String getAllTransactions(@PathVariable long userId) {
        try {
            return mapper.writeValueAsString(investmentService.getAllTransactions(userId));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

//    private void changeAccountBalance(Transaction transaction) {
//        if (transaction.getType().equals("Buy")) {
//            accountService.sendAmountToAccountService(transaction.getUserId(), transaction.getDepotName(),
//                    transaction.getTotalPrice().negate().setScale(2, RoundingMode.HALF_UP));
//        } else {
//            accountService.sendAmountToAccountService(transaction.getUserId(), transaction.getDepotName(),
//                    transaction.getTotalPrice());
//        }
//    }
}
