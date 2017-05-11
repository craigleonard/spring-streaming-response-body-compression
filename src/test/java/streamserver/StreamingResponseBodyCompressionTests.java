package streamserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StreamingResponseBodyCompressionTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void endpoint_returning_streaming_response_body_is_not_compressed() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(singletonList(MediaType.TEXT_PLAIN));
        requestHeaders.set(HttpHeaders.ACCEPT_ENCODING, "gzip");

        HttpEntity<Void> entity = new HttpEntity<>(requestHeaders);
        ResponseEntity<Void> response = restTemplate.exchange("/streaming-response-body", HttpMethod.GET, entity, Void.class);

        assertThat(response.getHeaders())
                .doesNotContainKey(HttpHeaders.CONTENT_TYPE)
                .doesNotContainKey(HttpHeaders.CONTENT_ENCODING);
    }

    @Test
    public void endpoint_returning_response_entity_is_compressed() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(singletonList(MediaType.TEXT_PLAIN));
        requestHeaders.set(HttpHeaders.ACCEPT_ENCODING, "gzip");

        HttpEntity<Void> entity = new HttpEntity<>(requestHeaders);
        ResponseEntity<Void> response = restTemplate.exchange("/response-entity", HttpMethod.GET, entity, Void.class);

        assertThat(response.getHeaders()).contains(
                entry(HttpHeaders.CONTENT_ENCODING, singletonList("gzip")),
                entry(HttpHeaders.CONTENT_TYPE, singletonList(MediaType.TEXT_PLAIN_VALUE)));
    }

}
