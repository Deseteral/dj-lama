package xyz.deseteral.djlama.integration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import spock.lang.Specification
import xyz.deseteral.djlama.DjLamaApplication

@SpringBootTest(
    classes = DjLamaApplication,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
abstract class IntegrationSpec extends Specification {

    @Value('${local.server.port}')
    protected int port

    @Autowired
    TestRestTemplate restTemplate

    protected URI localURI(String endpoint) {
        return new URI("http://localhost:$port$endpoint")
    }
}
