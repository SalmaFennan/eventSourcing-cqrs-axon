package ma.enset.eventsourcingcqrsaxon.query.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountBalanceStream {
    private String accountId;
}
