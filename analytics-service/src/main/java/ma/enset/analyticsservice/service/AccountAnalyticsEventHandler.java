package ma.enset.analyticsservice.service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.analyticsservice.entities.AccountAnalytics;
import ma.enset.analyticsservice.queries.GetAccountAnalyticsByAccountId;
import ma.enset.analyticsservice.queries.GetAllAccountAnalytics;
import ma.enset.analyticsservice.repo.AccountAnalyticsRepo;
import ma.enset.eventsourcingcqrsaxon.commonapi.events.AccountCreatedEvent;
import ma.enset.eventsourcingcqrsaxon.commonapi.events.AccountCreditedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;
import ma.enset.eventsourcingcqrsaxon.commonapi.events.AccountDebitedEvent;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AccountAnalyticsEventHandler {
    private AccountAnalyticsRepo accountAnalyticsRepo;
    private QueryUpdateEmitter queryUpdateEmitter;

    @EventHandler
    public void on(AccountCreatedEvent event){
        log.info("AccountCreatedEvent received");
        AccountAnalytics accountAnalytics=AccountAnalytics.builder()
                .accountId(event.getId())
                .status(event.getStatus().toString())
                .balance(event.getBalance())
                .totalDebit(0)
                .totalCredit(0)
                .numberDebitOperations(0)
                .numberCreditOperations(0)
                .build();
        accountAnalyticsRepo.save(accountAnalytics);
    }
    @EventHandler
    public void on(AccountCreditedEvent event){
        log.info("AccountCreatedEvent received");
        AccountAnalytics accountAnalytics=accountAnalyticsRepo.findByAccountId(event.getId());
        accountAnalytics.setTotalCredit(accountAnalytics.getTotalCredit()+event.getAmount());
        accountAnalytics.setNumberCreditOperations(accountAnalytics.getNumberCreditOperations()+1);
        accountAnalytics.setBalance(accountAnalytics.getBalance()+event.getAmount());
        accountAnalyticsRepo.save(accountAnalytics);
        queryUpdateEmitter.emit(GetAccountAnalyticsByAccountId.class,(query)->query.getAccountId().equals(event.getId()),accountAnalytics);
    }
    @EventHandler
    public void on(AccountDebitedEvent event){
        log.info("AccountDebitedEvent received");
        AccountAnalytics accountAnalytics=accountAnalyticsRepo.findByAccountId(event.getId());
        accountAnalytics.setTotalDebit(accountAnalytics.getTotalDebit()+event.getAmount());
        accountAnalytics.setNumberDebitOperations(accountAnalytics.getNumberDebitOperations()+1);
        accountAnalytics.setBalance(accountAnalytics.getBalance()-event.getAmount());
        accountAnalyticsRepo.save(accountAnalytics);
        queryUpdateEmitter.emit(GetAccountAnalyticsByAccountId.class,(query)->query.getAccountId().equals(event.getId()),accountAnalytics);
    }
    @QueryHandler
    public List<AccountAnalytics> on(GetAllAccountAnalytics query){
        return accountAnalyticsRepo.findAll();
    }
    @QueryHandler
    public AccountAnalytics on(GetAccountAnalyticsByAccountId query){
        return accountAnalyticsRepo.findByAccountId(query.getAccountId());
    }
}

