package tk.finedesk.finedesk.aws.configuration.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
final public class AmazonS3SourceKey implements SourceKey {
    private final String bucket;
    private final String key;
    private String eTag;
    private Long size;
}