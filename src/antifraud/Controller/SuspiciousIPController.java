package antifraud.Controller;

import antifraud.DTO.StatusDTO;
import antifraud.Model.ApiRoutes;
import antifraud.Model.IP.SuspiciousIP;
import antifraud.Service.SuspiciousIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SuspiciousIPController {

    SuspiciousIPService service;

    @Autowired
    public SuspiciousIPController(SuspiciousIPService service) {
        this.service = service;
    }

    @PostMapping(ApiRoutes.SUSPICIOUS_IP)
    public ResponseEntity<SuspiciousIP> addIP(@RequestBody SuspiciousIP ip) {
        return ResponseEntity.ok(service.addIP(ip));
    }

    @DeleteMapping(ApiRoutes.SUSPICIOUS_IP + "/{ip}")
    public ResponseEntity<StatusDTO> deleteIP(@PathVariable(value = "ip") String ip) {
        return ResponseEntity.ok(service.removeIP(ip));
    }

    @GetMapping(ApiRoutes.SUSPICIOUS_IP)
    public ResponseEntity<List<SuspiciousIP>> getAllIPs() {
        return ResponseEntity.ok(service.getAllIPs());
    }
}
