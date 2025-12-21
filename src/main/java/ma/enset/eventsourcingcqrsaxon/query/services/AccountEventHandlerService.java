package ma.enset.eventsourcingcqrsaxon.query.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import ma.enset.eventsourcingcqrsaxon.commonapi.enums.TransactionType;
import ma.enset.eventsourcingcqrsaxon.commonapi.events.AccountCreatedEvent;
import ma.enset.eventsourcingcqrsaxon.commonapi.events.AccountCreditedEvent;
import ma.enset.eventsourcingcqrsaxon.commonapi.events.AccountDebitedEvent;
import ma.enset.eventsourcingcqrsaxon.query.dto.AccountWatchEvent;
import ma.enset.eventsourcingcqrsaxon.query.entities.Account;
import ma.enset.eventsourcingcqrsaxon.query.entities.AccountTransaction;
import ma.enset.eventsourcingcqrsaxon.query.queries.GetAccountBalanceStream;
import ma.enset.eventsourcingcqrsaxon.query.queries.GetAccountById;
import ma.enset.eventsourcingcqrsaxon.query.queries.GetAllAccounts;
import ma.enset.eventsourcingcqrsaxon.query.repository.AccountRepository;
import ma.enset.eventsourcingcqrsaxon.query.repository.TransactionRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class AccountEventHandlerService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private QueryUpdateEmitter queryUpdateEmitter;


    public AccountEventHandlerService(AccountRepository accountRepository, TransactionRepository transactionRepository, QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }
    @EventHandler
    public void on(AccountCreatedEvent event, EventMessage<AccountCreatedEvent> eventMessage){
        log.info("**********************");
        log.info("AccountCreatedEvent received");
        Account account=new Account();
        account.setId(event.getId());
        account.setBalance(event.getBalance());
        account.setStatus(event.getStatus());
        account.setCurrency(event.getCurrency());
        account.setCreatedAt(eventMessage.getTimestamp());
        accountRepository.save(account);
    }
    @EventHandler
    public void on(AccountCreditedEvent event, EventMessage<AccountCreatedEvent> eventMessage){
        log.info("**********************");
        log.info("AccountCreditedEvent received");
        Account account=accountRepository.findById(event.getId()).get();
        AccountTransaction accountTransaction=AccountTransaction.builder()
                .account(account)
                .amount(event.getAmount())
                .type(TransactionType.CREDIT)
                .timestamp(eventMessage.getTimestamp())
                .build();
        transactionRepository.save(accountTransaction);
        account.setBalance(account.getBalance()+event.getAmount());
        accountRepository.save(account);
        AccountWatchEvent accountWatchEvent=new AccountWatchEvent(
                accountTransaction.getTimestamp(),account.getId(),account.getBalance(),accountTransaction.getType(),accountTransaction.getAmount()
        );
        queryUpdateEmitter.emit(GetAccountBalanceStream.class,(query)->(query.getAccountId().equals(account.getId())),accountWatchEvent);
    }
    @EventHandler
    public void on(AccountDebitedEvent event, EventMessage<AccountCreatedEvent> eventMessage){
        log.info("**********************");
        log.info("AccountDebitedEvent received");
        Account account=accountRepository.findById(event.getId()).get();
        AccountTransaction accountTransaction=AccountTransaction.builder()
                .account(account)
                .amount(event.getAmount())
                .type(TransactionType.DEBIT)
                .timestamp(eventMessage.getTimestamp())
                .build();
        transactionRepository.save(accountTransaction);
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepository.save(account);
        AccountWatchEvent accountWatchEvent=new AccountWatchEvent(
                accountTransaction.getTimestamp(),account.getId(),account.getBalance(),accountTransaction.getType(),accountTransaction.getAmount()
        );
        queryUpdateEmitter.emit(GetAccountBalanceStream.class,(query)->(query.getAccountId().equals(account.getId())),accountWatchEvent);
    }
    @QueryHandler
    public List<Account> on(GetAllAccounts query){
        return accountRepository.findAll();
    }
    @QueryHandler
    public Account on(GetAccountById query){
        return accountRepository.findById(query.getAccountId()).get();
    }
    @QueryHandler
    public AccountWatchEvent on(GetAccountBalanceStream query){
        Account account = accountRepository.findById(query.getAccountId()).get();
        AccountTransaction accountTransaction=transactionRepository.findTop1ByAccountIdOrderByTimestampDesc(query.getAccountId());
        if(accountTransaction!=null)
            return new AccountWatchEvent(
                    accountTransaction.getTimestamp(),
                    account.getId(),account.getBalance(),accountTransaction.getType(),accountTransaction.getAmount()
            );
        return null;
    }
}
