package tk.finedesk.finedesk.aws.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;


@Service
@Slf4j
public class AmazonSQSService {

    private final SqsClient sqsClient;
    private final int waitTimeoutSeconds;
    private final int visibilityTimoutInSeconds;
    private final String messageGroupId;


    public AmazonSQSService(SqsClient sqsClient,
                            @Value("${aws.wait-timeout-seconds}") int waitTimeoutSeconds,
                            @Value("${aws.visibility-timeout-seconds}") int visibilityTimoutInSeconds,
                            @Value("${sqs.message-group-id}") String messageGroupId) {
        this.sqsClient = sqsClient;
        this.waitTimeoutSeconds = waitTimeoutSeconds;
        this.visibilityTimoutInSeconds = visibilityTimoutInSeconds;
        this.messageGroupId = messageGroupId;
    }

    public void putMessagesIntoQueue(String sqsQueue, Message message) {
        log.debug("Put message into queue {}...", sqsQueue);
        var sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(sqsQueue)
                .messageBody(message.body())
                .messageGroupId(messageGroupId)
                .messageAttributes(message.messageAttributes())
                .build();
        try {
            sqsClient.sendMessage(sendMessageRequest);
        } catch (SdkServiceException exception) {
            log.debug("Unable to put message into queue {}...", sqsQueue);
        }
    }

}
