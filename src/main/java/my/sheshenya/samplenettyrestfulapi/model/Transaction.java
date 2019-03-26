package my.sheshenya.samplenettyrestfulapi.model;

//import com.fasterxml.jackson.annotation.JsonProperty;

import com.sun.org.apache.xalan.internal.lib.ExsltDatetime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private String id;
    private Date date;

    //@JsonProperty("account-from-id")
    private Long fromAccountId;
    //@JsonProperty("account-to-id")
    private Long toAccountId;
    private Integer amount;

    private String status;
}
