package tk.finedesk.finedesk.aws.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${aws.region}")
    private String region;

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder().region(Region.of(region)).build();
    }

}
