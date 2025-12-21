package ma.enset.eventsourcingcqrsaxon.query.repository;

import ma.enset.eventsourcingcqrsaxon.query.entities.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<AccountTransaction,Long> {
    AccountTransaction findTop1ByAccountIdOrderByTimestampDesc(String accountId);
}
