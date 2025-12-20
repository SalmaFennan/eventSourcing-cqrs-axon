package ma.enset.eventsourcingcqrsaxon.commands.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class AddAccountCommand {
    @TargetAggregateIdentifier
    private String accountId;
    private double initialBalance;
    private String currency;

    // Constructeur par défaut (requis par Axon)
    public AddAccountCommand() {
    }

    // Constructeur avec paramètres
    public AddAccountCommand(String accountId, double initialBalance, String currency) {
        this.accountId = accountId;
        this.initialBalance = initialBalance;
        this.currency = currency;
    }

    // Getters
    public String getAccountId() {
        return accountId;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public String getCurrency() {
        return currency;
    }
}