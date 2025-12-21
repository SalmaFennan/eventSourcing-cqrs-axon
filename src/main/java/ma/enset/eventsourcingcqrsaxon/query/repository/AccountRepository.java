package ma.enset.eventsourcingcqrsaxon.query.repository;
import ma.enset.eventsourcingcqrsaxon.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}