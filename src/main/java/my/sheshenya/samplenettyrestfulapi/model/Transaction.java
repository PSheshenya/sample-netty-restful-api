package my.sheshenya.samplenettyrestfulapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.StringJoiner;

@Data
@NoArgsConstructor
public class Transaction implements Serializable {

    private String id;
    private Date date = new Date();

    //    @JsonProperty("account-to-id")
    private String fromAccountId;
    private String toAccountId;

    private BigDecimal amount;
    private String status;

    public Transaction(String fromAccountId, String toAccountId, BigDecimal amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public Transaction(String id, Date date, String fromAccountId, String toAccountId, BigDecimal amount, String status) {
        this.id = id;
        this.date = new Date(date.getTime());
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = status;
    }

    /**
     * Date class is mutable so we need a little care here.
     * We should not return the reference of original instance variable.
     * Instead a new Date object, with content copied to it, should be returned.
     * */
    public Date getDate() {
        return new Date(this.date.getTime());
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
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
