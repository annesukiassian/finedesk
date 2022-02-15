package tk.finedesk.finedesk.aws.configuration.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tk.finedesk.finedesk.exceptions.UnableToSendSQSEventException;

@Getter
@Setter
@Builder
public class SQSEmailMessageDto {

    private String recipient;
    private String message;

    public String getJSONMessage() throws UnableToSendSQSEventException {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new UnableToSendSQSEventException("Unable to send message to SQS.");
        }
    }

}
