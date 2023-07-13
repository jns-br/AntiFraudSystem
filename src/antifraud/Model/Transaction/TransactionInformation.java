package antifraud.Model.Transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transactions")
public class TransactionInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long transactionId;
    private long amount;
    private String ip;
    private String number;
    private String region;
    @Column(name = "created_at")
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private ValidationEnum result;
    @Enumerated(EnumType.STRING)
    private ValidationEnum feedback;


    @JsonProperty("feedback")
    public String getFeedbackString() {
        return feedback == null ? "" : feedback.name();
    }
}
