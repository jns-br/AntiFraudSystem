package antifraud.Controller;

import antifraud.DTO.ValidationDTO;
import antifraud.Model.ApiRoutes;
import antifraud.Model.Transaction.Requests.FeedbackRequest;
import antifraud.Model.Transaction.TransactionInformation;
import antifraud.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {
    TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(ApiRoutes.TRANSACTION)
    public ValidationDTO validateAndSave(@RequestBody TransactionInformation information) {
        return transactionService.saveTransaction(information);
    }

    @PutMapping(ApiRoutes.TRANSACTION)
    public TransactionInformation updateTransaction(@RequestBody FeedbackRequest request) {
        return transactionService.updateTransaction(request.transactionId(), request.feedback());
    }

    @GetMapping(ApiRoutes.ANTIFRAUD_HISTORY)
    public List<TransactionInformation> getHistory() {
        return transactionService.getHistory();
    }

    @GetMapping(ApiRoutes.ANTIFRAUD_HISTORY + "/{number}")
    public List<TransactionInformation> getHistoryForCard(@PathVariable(value = "number") String number) {
        return transactionService.getHistoryForCard(number);
    }


}
