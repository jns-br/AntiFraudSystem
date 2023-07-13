package antifraud.Service;

import antifraud.DTO.ValidationDTO;
import antifraud.Exceptions.ElementNotFoundException;
import antifraud.Exceptions.UnprocessableEntityException;
import antifraud.Exceptions.Validation.ValidationException;
import antifraud.Model.Card.Card;
import antifraud.Model.Transaction.TransactionInformation;
import antifraud.Model.Transaction.ValidationEnum;
import antifraud.Repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    CardRepository cardRepository;
    ValidatorService validatorService;


    @Autowired
    public CardService(CardRepository cardRepository, ValidatorService validatorService) {
        this.cardRepository = cardRepository;
        this.validatorService = validatorService;
    }

    @Transactional
    public ValidationDTO addTransactionToCard(TransactionInformation information) throws ValidationException {
        validatorService.validateCardNumber(information.getNumber());
        Optional<Card> savedCard = Optional.ofNullable(cardRepository.findAllByNumber(information.getNumber()));
        Card card = savedCard.orElseGet(() -> {
            Card newCard = new Card();
            newCard.setNumber(information.getNumber());
            return cardRepository.save(newCard);
        });
        ValidationDTO validationResult = validatorService.validateInformation(information);
        information.setResult(validationResult.result());
        card.getTransactions()
            .add(information);
        cardRepository.save(card);
        return validationResult;
    }

    public Card getCardByNumber(String cardNumber) throws ElementNotFoundException, ValidationException {
        validatorService.validateCardNumber(cardNumber);
        Optional<Card> savedCard = Optional.ofNullable(cardRepository.findAllByNumber(cardNumber));
        return savedCard.orElseThrow(() -> new ElementNotFoundException("Card " + cardNumber + " not found"));
    }

    public List<TransactionInformation> getTransactionHistoryForCard(String cardNumber) {
        return getCardByNumber(cardNumber).getTransactions();
    }

    @Transactional
    public void updateLimits(ValidationEnum feedback, TransactionInformation transactionInformation) throws UnprocessableEntityException {
        Card card = getCardByNumber(transactionInformation.getNumber());
        ValidationEnum transactionResult = transactionInformation.getResult();
        long currentAllowedLimit = card.getMaxAllowed();
        long currentManualLimit = card.getMaxManual();
        long transactionAmount = transactionInformation.getAmount();
        if (feedback == transactionResult) {
            throw new UnprocessableEntityException("Feedback and Transaction Evaluation are equal");
        }

        if (transactionResult == ValidationEnum.ALLOWED) {
            if (feedback == ValidationEnum.MANUAL_PROCESSING) {
                card.setMaxAllowed(decreaseLimit(currentAllowedLimit, transactionAmount));
            }

            if (feedback == ValidationEnum.PROHIBITED) {
                card.setMaxAllowed(decreaseLimit(currentAllowedLimit, transactionAmount));
                card.setMaxManual(decreaseLimit(currentManualLimit, transactionAmount));
            }
        }

        if (transactionResult == ValidationEnum.MANUAL_PROCESSING) {
            if (feedback == ValidationEnum.ALLOWED) {
                card.setMaxAllowed(increaseLimit(currentAllowedLimit, transactionAmount));
            }
            if (feedback == ValidationEnum.PROHIBITED) {
                card.setMaxManual(decreaseLimit(currentManualLimit, transactionAmount));

            }
        }

        if (transactionResult == ValidationEnum.PROHIBITED) {
            if (feedback == ValidationEnum.ALLOWED) {
                card.setMaxAllowed(increaseLimit(currentAllowedLimit, transactionAmount));
                card.setMaxManual(increaseLimit(currentManualLimit, transactionAmount));
            }
            if (feedback == ValidationEnum.MANUAL_PROCESSING) {
                card.setMaxManual(increaseLimit(currentManualLimit, transactionAmount));
            }
        }
        cardRepository.save(card);
    }

    private long decreaseLimit(long currentLimit, long transactionAmount) {
        return (long) Math.ceil(0.8 * currentLimit - 0.2 * transactionAmount);
    }

    private long increaseLimit(long currentLimit, long transactionAmount) {
        return (long) Math.ceil(0.8 * currentLimit + 0.2 * transactionAmount);
    }
}
