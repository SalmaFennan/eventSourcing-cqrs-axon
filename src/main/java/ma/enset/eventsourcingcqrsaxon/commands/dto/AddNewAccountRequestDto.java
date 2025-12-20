package ma.enset.eventsourcingcqrsaxon.commands.dto;

public record AddNewAccountRequestDto(
        double initialBalance,
        String currency
) {}