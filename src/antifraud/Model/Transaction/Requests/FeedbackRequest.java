package antifraud.Model.Transaction.Requests;

import antifraud.Model.Transaction.ValidationEnum;

public record FeedbackRequest(long transactionId, ValidationEnum feedback) {
}
