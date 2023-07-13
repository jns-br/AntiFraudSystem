package antifraud.Service;

import antifraud.DTO.StatusDTO;
import antifraud.Exceptions.ElementExistsException;
import antifraud.Exceptions.ElementNotFoundException;
import antifraud.Exceptions.Validation.ValidationException;
import antifraud.Model.StolenCard.StolenCard;
import antifraud.Repository.StolenCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StolenCardService {

    StolenCardRepository repository;
    ValidatorService validatorService;

    @Autowired
    public StolenCardService(StolenCardRepository repository, ValidatorService validatorService) {
        this.repository = repository;
        this.validatorService = validatorService;
    }

    @Transactional
    public StolenCard addStolenCard(StolenCard card) {
        validatorService.validateCardNumber(card.getNumber());
        Optional<StolenCard> savedCard = Optional.ofNullable(repository.findStolenCardByNumber(card.getNumber()));
        savedCard.ifPresent(foundCard -> {
            throw new ElementExistsException(foundCard.getNumber() + " already exists");
        });
        return repository.save(card);
    }

    @Transactional
    public StatusDTO removeStolenCard(String cardNumber) throws ValidationException {
        validatorService.validateCardNumber(cardNumber);
        long count = repository.deleteStolenCardByNumber(cardNumber);
        if (count < 1) {
            throw new ElementNotFoundException(cardNumber);
        }
        return new StatusDTO("Card " + cardNumber + " successfully removed!");
    }

    public List<StolenCard> getAllStolenCards() {
        return repository.findAllByOrderByIdAsc();
    }

}
