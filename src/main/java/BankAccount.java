import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class BankAccount {

    private Integer accountNumber;
    private Client client;
    private String openDate;
    private String currency;
    private volatile long balance;

    // пополнение счета
    public synchronized void debit(long amount) {
        balance += amount;
    }

    // снятие со счета
    public synchronized void credit(long amount) {
        balance -= amount;
    }
}
