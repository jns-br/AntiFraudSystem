package antifraud.Repository;

import antifraud.Model.IP.SuspiciousIP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuspiciousIPRepository extends CrudRepository<SuspiciousIP, Long> {
    long deleteSuspiciousIPByIp(String ip);

    SuspiciousIP findSuspiciousIPByIp(String ip);

    List<SuspiciousIP> findAllByOrderByIdAsc();
}
