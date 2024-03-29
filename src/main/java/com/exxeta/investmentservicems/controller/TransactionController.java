package com.exxeta.investmentservicems.controller;

import com.exxeta.investmentservicems.dtos.TransactionDto;
import com.exxeta.investmentservice.entities.Security;
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
import java.util.ArrayList;
import java.util.List;

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

    @PostMapping(path = "/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public String addNewTransaction(@PathVariable long userId, @Valid @RequestBody TransactionDto transactionDto) {
        logger.info("Received transaction: " + transactionDto);
        try {
            Transaction transaction = transformToTransactionEntity(transactionDto);
            transaction.setUserId(userId);
            transactionHandler.handleTransaction(transaction);
//            changeAccountBalance(transaction);
            return mapper.writeValueAsString(transactionDto);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }

    private Transaction transformToTransactionEntity(TransactionDto transactionDto) {
        return new Transaction(
                transactionDto.getUserId(),
                transactionDto.getDate(),
                transactionDto.getDepotName(),
                transactionDto.getType(),
                new Security(transactionDto.getIsin(), transactionDto.getSecurityName()),
                transactionDto.getNumber(),
                transactionDto.getPrice(),
                transactionDto.getExpenses(),
                transactionDto.getTotalPrice()
        );
    }

    @GetMapping(path = "/transactions")
    public String getAllTransactions(@PathVariable long userId) {
        try {
            List<Transaction> allTransactions = investmentService.getAllTransactions(userId);
            List<TransactionDto> transactionDtoList = new ArrayList<>();
            allTransactions.forEach(transaction -> transactionDtoList.add(transformToTransactionDto(transaction)));
            return mapper.writeValueAsString(transactionDtoList);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());

        }
    }

    private TransactionDto transformToTransactionDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getUserId(),
                transaction.getDate(),
                transaction.getDepotName(),
                transaction.getType(),
                transaction.getSecurity().getIsin(),
                transaction.getSecurity().getSecurityName(),
                transaction.getNumber(),
                transaction.getPrice(),
                transaction.getExpenses(),
                transaction.getTotalPrice()
        );
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
