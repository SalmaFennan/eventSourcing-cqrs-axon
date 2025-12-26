package ma.enset.analyticsservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.analyticsservice.entities.AccountAnalytics;
import ma.enset.analyticsservice.queries.GetAccountAnalyticsByAccountId;
import ma.enset.analyticsservice.queries.GetAllAccountAnalytics;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query")
@AllArgsConstructor
@Slf4j
public class AccountAnalyticsController {
    private QueryGateway queryGateway;
    @GetMapping("/accountAnalytics")
    public CompletableFuture<List<AccountAnalytics>> accountAnalytics(){
        return queryGateway.query(new GetAllAccountAnalytics(), ResponseTypes.multipleInstancesOf(AccountAnalytics.class));
    }
    @GetMapping("/accountAnalytics/{accountId}")
    public CompletableFuture<AccountAnalytics> accountAnalyticsByAccountId( @PathVariable String accountId){
        return queryGateway.query(new GetAccountAnalyticsByAccountId(accountId), ResponseTypes.instanceOf(AccountAnalytics.class));
    }
    @GetMapping(value = "/accountAnalytics/{accountId}/watch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountAnalytics> watchAccountAccount(@PathVariable String accountId){
        SubscriptionQueryResult<AccountAnalytics, AccountAnalytics> result = queryGateway.subscriptionQuery(new GetAccountAnalyticsByAccountId(accountId),
                ResponseTypes.instanceOf(AccountAnalytics.class), ResponseTypes.instanceOf(AccountAnalytics.class));
        return result.initialResult().concatWith(result.updates());
    }

}