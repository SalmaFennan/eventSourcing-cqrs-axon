package ma.enset.eventsourcingcqrsaxon.commands.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent {

    private String accountId;
    private double initialBalance;
    private String currency;
    private String status;
}
