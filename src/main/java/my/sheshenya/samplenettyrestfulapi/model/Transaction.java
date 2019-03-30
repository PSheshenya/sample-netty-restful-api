package my.sheshenya.samplenettyrestfulapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;


public class Transaction implements Serializable {

    private String id;
    private Date date;

    //    @JsonProperty("account-to-id")
    private String fromAccountId;
    private String toAccountId;

    private double amount;
    private String status;


    public Transaction() {

    }

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
                .add("id=").add(id)
                .add("date=").add(date.toString())
                .add("fromAccountId=").add(fromAccountId)
                .add("toAccountId=").add(toAccountId)
                .add("amount=").add(String.valueOf(amount))
                .add("status=").add(status);
        return joiner.toString();
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
