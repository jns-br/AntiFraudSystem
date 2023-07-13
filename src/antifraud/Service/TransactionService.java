package antifraud.Service;

import antifraud.DTO.ValidationDTO;
import antifraud.Exceptions.ElementExistsException;
import antifraud.Exceptions.ElementNotFoundException;
import antifraud.Exceptions.UnprocessableEntityException;
import antifraud.Model.Transaction.TransactionInformation;
import antifraud.Model.Transaction.ValidationEnum;
import antifraud.Repository.StolenCardRepository;
import antifraud.Repository.SuspiciousIPRepository;
import antifraud.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    SuspiciousIPRepository suspiciousIPRepository;
    StolenCardRepository stolenCardRepository;

    TransactionRepository transactionRepository;

    CardService cardService;
    ValidatorService validatorService;

    @Autowired
    public TransactionService(SuspiciousIPRepository suspiciousIPRepository, StolenCardRepository stolenCardRepository, TransactionRepository transactionRepository, CardService cardService, ValidatorService validatorService) {
        this.suspiciousIPRepository = suspiciousIPRepository;
        this.stolenCardRepository = stolenCardRepository;
        this.transactionRepository = transactionRepository;
        this.cardService = cardService;
        this.validatorService = validatorService;
    }


    @Transactional
    public ValidationDTO saveTransaction(TransactionInformation information) {
        return cardService.addTransactionToCard(information);
    }

    @Transactional
    public TransactionInformation updateTransaction(long transactionId, ValidationEnum feedback) throws ElementNotFoundException, ElementExistsException, UnprocessableEntityException {
        Optional<TransactionInformation> savedTransaction = Optional.ofNullable(transactionRepository.findAllByTransactionId(transactionId));
        TransactionInformation information = savedTransaction.orElseThrow(() -> new ElementNotFoundException(String.valueOf(transactionId)));
        Optional<ValidationEnum> savedFeedback = Optional.ofNullable(information.getFeedback());
        savedFeedback.ifPresent(s -> {
            throw new ElementExistsException("Feedback has already been set to " + s);
        });
        cardService.updateLimits(feedback, information);
        information.setFeedback(feedback);
        transactionRepository.save(information);
        return information;
    }

    public List<TransactionInformation> getHistory() {
        return transactionRepository.findAll();
    }

    public List<TransactionInformation> getHistoryForCard(String cardNumber) {
        return cardService.getTransactionHistoryForCard(cardNumber);
    }


}
