package my.sheshenya.samplenettyrestfulapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

@Data
@NoArgsConstructor
public class Transaction implements Serializable {

    private String id;
    private Date date;

    //    @JsonProperty("account-to-id")
    private String fromAccountId;
    private String toAccountId;

    private double amount;
    private String status;

    public Transaction(String fromAccountId, String toAccountId, double amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public Transaction(String id, Date date, String fromAccountId, String toAccountId, double amount, String status) {
        this.id = id;
        this.date = date;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = status;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(",");
        joiner
                .add("id=" + id)
                .add("date=" + String.valueOf(date))
                .add("fromAccountId="+ fromAccountId)
                .add("toAccountId="+toAccountId)
                .add("amount="+String.valueOf(amount))
                .add("status="+status);
        return joiner.toString();
    }

}
