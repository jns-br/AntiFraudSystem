package antifraud.Service;

import antifraud.DTO.StatusDTO;
import antifraud.Exceptions.ElementExistsException;
import antifraud.Exceptions.ElementNotFoundException;
import antifraud.Exceptions.Validation.ValidationException;
import antifraud.Model.IP.SuspiciousIP;
import antifraud.Repository.SuspiciousIPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SuspiciousIPService {

    SuspiciousIPRepository repository;

    @Autowired
    public SuspiciousIPService(SuspiciousIPRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public SuspiciousIP addIP(SuspiciousIP ip) throws ValidationException, ElementExistsException {
        validateIP(ip.getIp());
        Optional<SuspiciousIP> savedIp = Optional.ofNullable(repository.findSuspiciousIPByIp(ip.getIp()));
        savedIp.ifPresent(foundIp -> {
            throw new ElementExistsException(foundIp.getIp() + " already exists");
        });
        return repository.save(ip);
    }

    @Transactional
    public StatusDTO removeIP(String ip) throws ValidationException, ElementNotFoundException {
        validateIP(ip);
        long count = repository.deleteSuspiciousIPByIp(ip);
        if (count < 1) {
            throw new ElementNotFoundException(ip);
        }
        return new StatusDTO("IP " + ip + " successfully removed!");
    }

    public List<SuspiciousIP> getAllIPs() {
        return repository.findAllByOrderByIdAsc();
    }

    private void validateIP(String ip) throws ValidationException {
        Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$");
        Matcher matcher = pattern.matcher(ip);
        if (!matcher.find()) {
            throw new ValidationException(ip + " has an invalid format.");
        }
    }
}
