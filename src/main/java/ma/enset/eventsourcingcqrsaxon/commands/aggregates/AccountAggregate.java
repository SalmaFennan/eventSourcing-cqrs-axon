package ma.enset.eventsourcingcqrsaxon.commands.aggregates;

import lombok.NoArgsConstructor;
import ma.enset.eventsourcingcqrsaxon.commands.commands.AddAccountCommand;
import ma.enset.eventsourcingcqrsaxon.commands.events.AccountCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private String status;

    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        // Validation métier
        if (command.getInitialBalance() < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        // Publier l'événement
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getAccountId(),
                command.getInitialBalance(),
                command.getCurrency(),
                "ACTIVATED"
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getAccountId();
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = event.getStatus();
    }
}