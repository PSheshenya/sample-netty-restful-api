package my.sheshenya.samplenettyrestfulapi.model;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Account implements Serializable {

    @Getter
    private String id;

    @Getter @Setter
    private String name;

    @Getter
    private double balance = 0;

    public Account(String id, String name, double initBalance) {
        this.id = id;
        this.name = name;

        // is possible negative balance?
        this.balance = initBalance;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

}
