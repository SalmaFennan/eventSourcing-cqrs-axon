package ma.enset.eventsourcingcqrsaxon.commands.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountRequestDTO {
    private String currency;
    private double initialBalance;
}
