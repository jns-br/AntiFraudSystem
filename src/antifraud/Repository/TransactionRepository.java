package antifraud.Repository;

import antifraud.Model.Transaction.TransactionInformation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<TransactionInformation, Long> {

    @Query(value = "SELECT COUNT(DISTINCT region) FROM transactions WHERE number = :cardNumber AND region != :currentRegion AND created_at BETWEEN :oneHourAgo AND :currentDate", nativeQuery = true)
    long findAllLastTransactionRegions(@Param("cardNumber") String cardNumber, @Param("currentRegion") String currentRegion, @Param("oneHourAgo") LocalDateTime oneHourAgo, @Param("currentDate") LocalDateTime currentDate);

    @Query(value = "SELECT COUNT(DISTINCT ip) FROM transactions WHERE number = :cardNumber AND ip != :currentIp  AND created_at BETWEEN :oneHourAgo AND :currentDate", nativeQuery = true)
    long findAllLastTransactionIps(@Param("cardNumber") String cardNumber, @Param("currentIp") String currentIp, @Param("oneHourAgo") LocalDateTime oneHourAgo, @Param("currentDate") LocalDateTime currentDate);

    TransactionInformation findAllByTransactionId(long transactionId);

    List<TransactionInformation> findAll();

    List<TransactionInformation> findAllByNumber(String number);
}
