package my.sheshenya.samplenettyrestfulapi.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Account implements Serializable {

//    private final Lock lock = new ReentrantLock();

    @Getter
    private String id;

    @Getter @Setter
    private String name;

    /*
    * As a balance type we could choose JSR 354: Money and Currency API
    * http://javamoney.github.io/api.html
    * https://jcp.org/en/jsr/detail?id=354
    *
    */
    private BigDecimal balance = BigDecimal.ZERO;

    public Account(String id, String name, BigDecimal initBalance) {
        this.id = id;
        this.name = name;

        // is possible negative balance?
        this.balance = initBalance;
    }

    public synchronized void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public synchronized void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

//    public Lock getLock() {
//        return lock;
//    }
}
