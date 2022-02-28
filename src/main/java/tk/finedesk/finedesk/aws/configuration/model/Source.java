package tk.finedesk.finedesk.aws.configuration.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.InputStream;

@RequiredArgsConstructor
@Value
public class Source {
    SourceKey sourceKey;
    InputStream content;
}
