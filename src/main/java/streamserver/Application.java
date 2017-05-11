package streamserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RestController
    public static class StreamingController {

        private static byte[] CONTENT = new byte[1024];

        @GetMapping(value = "/streaming-response-body", produces = MediaType.TEXT_PLAIN_VALUE)
        public StreamingResponseBody getStreamingResponseBody() {
            return outputStream -> outputStream.write(CONTENT);
        }

        @GetMapping(value = "/response-entity", produces = MediaType.TEXT_PLAIN_VALUE)
        public ResponseEntity<StreamingResponseBody> getResponseEntity() {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(outputStream -> outputStream.write(CONTENT), headers, HttpStatus.OK);
        }
    }
}
