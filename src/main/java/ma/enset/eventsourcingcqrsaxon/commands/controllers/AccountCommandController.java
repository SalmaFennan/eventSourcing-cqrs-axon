package ma.enset.eventsourcingcqrsaxon.commands.controllers;
import ma.enset.eventsourcingcqrsaxon.commands.commands.AddAccountCommand;
import ma.enset.eventsourcingcqrsaxon.commands.dto.AddNewAccountRequestDto;
import org.axonframework.commandhandling.gateway.CommandGateway;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
@RestController
@RequestMapping(path="/commands/accounts")
public class AccountCommandController {
    private final CommandGateway commandGateway;
    public AccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/add")
    public CompletableFuture<String> addNewAccount(@RequestBody AddNewAccountRequestDto request) {

        return commandGateway.send(new AddAccountCommand(
                UUID.randomUUID().toString(),
                request.initialBalance(),
                request.currency()
        ));
    }
}
