package ma.enset.eventsourcingcqrsaxon.commonapi.events;


import lombok.Getter;
import ma.enset.eventsourcingcqrsaxon.commonapi.enums.AccountStatus;

public class AccountCreatedEvent extends BaseEvent<String> {
    @Getter private String currency;
    @Getter private double balance;
    @Getter private AccountStatus status;
    public AccountCreatedEvent(String id, String currency, double balance, AccountStatus status) {
        super(id);
        this.currency = currency;
        this.balance = balance;
        this.status = status;
    }
}