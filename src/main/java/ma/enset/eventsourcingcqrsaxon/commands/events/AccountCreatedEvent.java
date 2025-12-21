package ma.enset.eventsourcingcqrsaxon.commands.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ma.enset.eventsourcingcqrsaxon.commands.enums.AccountStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent {

    private String accountId;
    private double initialBalance;
    private String currency;
    private AccountStatus status;
}
