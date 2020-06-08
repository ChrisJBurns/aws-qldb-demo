package shop.backend.api;

import com.amazonaws.services.qldb.AmazonQLDB;
import com.amazonaws.services.qldb.AmazonQLDBClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QLDBConfig {

    @Bean
    public AmazonQLDB qldbClient() {
        return AmazonQLDBClientBuilder.standard().build();
    }
}
