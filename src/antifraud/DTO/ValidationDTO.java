package antifraud.DTO;

import antifraud.Model.Transaction.ValidationEnum;

public record ValidationDTO(ValidationEnum result, String info) {
}
