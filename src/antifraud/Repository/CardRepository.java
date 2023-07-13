package antifraud.Repository;

import antifraud.Model.Card.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<Card, String> {
    Card findAllByNumber(String number);
}
