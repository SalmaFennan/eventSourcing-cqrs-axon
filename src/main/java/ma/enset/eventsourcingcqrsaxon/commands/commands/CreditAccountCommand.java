package ma.enset.eventsourcingcqrsaxon.commands.commands;
import lombok.Getter;
import ma.enset.eventsourcingcqrsaxon.commands.aggregates.BaseCommand;

public class CreditAccountCommand extends BaseCommand<String> {
    @Getter
    private String currency;
    @Getter private double amount;
    public CreditAccountCommand(String id, String currency, double amount) {
        super(id);
        this.currency = currency;
        this.amount = amount;
    }
}

