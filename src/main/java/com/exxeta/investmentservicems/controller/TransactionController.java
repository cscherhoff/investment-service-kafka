package com.exxeta.investmentservicems.controller;

import com.exxeta.investmentservice.service.ImportService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/investments/user/{userId}")
public class TransactionController {

    private final ObjectMapper mapper = new ObjectMapper();
    private final InvestmentService investmentService;

    private final TransactionHandler transactionHandler;

    private final ImportService importService;
//    private final AccountService accountService;

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(InvestmentService investmentService, TransactionHandler transactionHandler, ImportService importService) {
        this.investmentService = investmentService;
        this.transactionHandler = transactionHandler;
//        this.accountService = accountService;
        this.importService = importService;
    }

    @PostMapping(path = "/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public String addNewTransaction(@PathVariable String userId, @Valid @RequestBody TransactionDto transactionDto) {
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

    @GetMapping(path = "/download")
    public String downloadTransactions() throws IOException {
        logger.info("Trying to download transactions...");
        investmentService.downloadTransactions("1234567");
        return "download was successfully";
    }

    @GetMapping(path = "/import")
    public String importTransactions() {
        logger.info("Trying to import transactions....");
        importService.importTransactions();
        return "...import was successful";
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        importService.importTransactions(file);
        return "Upload " + file.getName() + " was successful";
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
    public String getAllTransactions(@PathVariable String userId) {
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

        String isin = "";
        String securityName = "";
        if (transaction.getSecurity() != null) {
            isin = transaction.getSecurity().getIsin();
            securityName = transaction.getSecurity().getSecurityName();
        }
        return new TransactionDto(
                transaction.getUserId(),
                transaction.getDate(),
                transaction.getDepotName(),
                transaction.getType(),
                isin,
                securityName,
                transaction.getNumber(),
                transaction.getPrice(),
                transaction.getExpenses(),
                transaction.getTotalPrice()
        );
    }

    @GetMapping("/recalculate")
    public String recalculateAll(@PathVariable String userId) {
        return transactionHandler.recalculateAll(userId);
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
