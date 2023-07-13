package antifraud.Model.Card;

import antifraud.Model.Transaction.TransactionInformation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String number;
    @Column(name = "max_allowed")
    private long maxAllowed = 200;
    @Column(name = "max_manual")
    private long maxManual = 1500;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id", nullable = false)
    List<TransactionInformation> transactions = new ArrayList<>();

}
