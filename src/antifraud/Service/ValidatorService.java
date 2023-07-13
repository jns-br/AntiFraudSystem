package antifraud.Service;

import antifraud.DTO.ValidationDTO;
import antifraud.Exceptions.Validation.ValidationException;
import antifraud.Model.Card.Card;
import antifraud.Model.IP.SuspiciousIP;
import antifraud.Model.StolenCard.StolenCard;
import antifraud.Model.Transaction.TransactionInformation;
import antifraud.Model.Transaction.ValidationEnum;
import antifraud.Repository.CardRepository;
import antifraud.Repository.StolenCardRepository;
import antifraud.Repository.SuspiciousIPRepository;
import antifraud.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

@Service
public class ValidatorService {

    SuspiciousIPRepository suspiciousIPRepository;
    StolenCardRepository stolenCardRepository;
    CardRepository cardRepository;
    TransactionRepository transactionRepository;

    @Autowired
    public ValidatorService(SuspiciousIPRepository suspiciousIPRepository, StolenCardRepository stolenCardRepository, CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.suspiciousIPRepository = suspiciousIPRepository;
        this.stolenCardRepository = stolenCardRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    public void validateCardNumber(String cardNumber) throws ValidationException {
        if (cardNumber == null) {
            throw new ValidationException("Card number cannot be null");
        }
        int[] digits = Arrays.stream(cardNumber.split(""))
            .mapToInt(Integer::parseInt)
            .toArray();
        int sum = IntStream.range(0, digits.length)
            .map(i -> {
                int digit = digits[digits.length - i - 1];
                if (i % 2 == 1) {
                    digit *= 2;
                }
                return digit > 9 ? digit - 9 : digit;
            })
            .sum();
        if (sum % 10 != 0) {
            throw new ValidationException(cardNumber + " has an invalid format.");
        }
    }

    public ValidationDTO validateInformation(TransactionInformation information) throws ValidationException {
        Map<ValidationEnum, String> validationMap = new HashMap<>();
        BinaryOperator<String> mergeFunc = (oldValue, newValue) -> oldValue + ", " + newValue;

        validationMap.merge(validateAmount(information.getAmount(), information.getNumber()), "amount", mergeFunc);
        validationMap.merge(validateCard(information.getNumber()), "card-number", mergeFunc);
        validationMap.merge(validateIP(information.getIp()), "ip", mergeFunc);
        validationMap.merge(validateIpCorrelation(information), "ip-correlation", mergeFunc);
        validationMap.merge(validateRegionCorrelation(information), "region-correlation", mergeFunc);

        if (validationMap.containsKey(ValidationEnum.PROHIBITED)) {
            return new ValidationDTO(ValidationEnum.PROHIBITED, validationMap.get(ValidationEnum.PROHIBITED));
        }

        if (validationMap.containsKey(ValidationEnum.MANUAL_PROCESSING)) {
            return new ValidationDTO(ValidationEnum.MANUAL_PROCESSING, validationMap.get(ValidationEnum.MANUAL_PROCESSING));
        }
        return new ValidationDTO(ValidationEnum.ALLOWED, "none");
    }

    private ValidationEnum validateIP(String ip) {
        SuspiciousIP foundIP = suspiciousIPRepository.findSuspiciousIPByIp(ip);
        return foundIP != null ? ValidationEnum.PROHIBITED : ValidationEnum.ALLOWED;
    }

    private ValidationEnum validateCard(String cardNumber) {
        StolenCard foundCard = stolenCardRepository.findStolenCardByNumber(cardNumber);
        return foundCard != null ? ValidationEnum.PROHIBITED : ValidationEnum.ALLOWED;
    }

    private ValidationEnum validateAmount(long amount, String cardNumber) throws ValidationException {
        Card card = cardRepository.findAllByNumber(cardNumber);
        if (amount <= 0) {
            throw new ValidationException("Amount cannot be negative");
        }
        if (amount <= card.getMaxAllowed()) return ValidationEnum.ALLOWED;
        if (amount <= card.getMaxManual()) return ValidationEnum.MANUAL_PROCESSING;
        return ValidationEnum.PROHIBITED;
    }

    private ValidationEnum validateRegionCorrelation(TransactionInformation transactionInformation) {
        long correlationCount = transactionRepository.findAllLastTransactionRegions(transactionInformation.getNumber(), transactionInformation.getRegion(), transactionInformation.getDate()
            .minusHours(1), transactionInformation.getDate());
        return evaluateCorrelationCount(correlationCount);
    }

    private ValidationEnum validateIpCorrelation(TransactionInformation transactionInformation) {
        long correlationCount = transactionRepository.findAllLastTransactionIps(transactionInformation.getNumber(), transactionInformation.getIp(), transactionInformation.getDate()
            .minusHours(1), transactionInformation.getDate());
        return evaluateCorrelationCount(correlationCount);
    }

    private ValidationEnum evaluateCorrelationCount(long correlationCount) {
        if (correlationCount > 2) return ValidationEnum.PROHIBITED;
        return correlationCount == 2 ? ValidationEnum.MANUAL_PROCESSING : ValidationEnum.ALLOWED;
    }
}
