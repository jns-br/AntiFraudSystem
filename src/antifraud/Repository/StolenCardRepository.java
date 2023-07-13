package antifraud.Repository;

import antifraud.Model.StolenCard.StolenCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StolenCardRepository extends CrudRepository<StolenCard, Long> {

    StolenCard findStolenCardByNumber(String number);

    long deleteStolenCardByNumber(String number);

    List<StolenCard> findAllByOrderByIdAsc();
}
