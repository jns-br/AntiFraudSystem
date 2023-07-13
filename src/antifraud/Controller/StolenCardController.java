package antifraud.Controller;

import antifraud.DTO.StatusDTO;
import antifraud.Model.ApiRoutes;
import antifraud.Model.StolenCard.StolenCard;
import antifraud.Service.StolenCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StolenCardController {

    StolenCardService service;

    @Autowired
    public StolenCardController(StolenCardService service) {
        this.service = service;
    }

    @PostMapping(ApiRoutes.STOLEN_CARD)
    public ResponseEntity<StolenCard> addStolenCard(@RequestBody StolenCard card) {
        return ResponseEntity.ok(service.addStolenCard(card));
    }

    @DeleteMapping(ApiRoutes.STOLEN_CARD + "/{number}")
    public ResponseEntity<StatusDTO> deleteStolenCard(@PathVariable(value = "number") String number) {
        return ResponseEntity.ok(service.removeStolenCard(number));
    }

    @GetMapping(ApiRoutes.STOLEN_CARD)
    public ResponseEntity<List<StolenCard>> getAllStolenCards() {
        return ResponseEntity.ok(service.getAllStolenCards());
    }
}
