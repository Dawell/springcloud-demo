package cc.dawell.springcloud.accountservice;

import com.netflix.discovery.converters.Auto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.stream.Stream;

@EnableBinding(Source.class)
@EnableDiscoveryClient
@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner sampleData(AccountRepository accountRepository) {
        return arg -> {
            Stream.of("123", "456", "678")
                    .forEach(name -> accountRepository.save(new Account(null, name)));
            accountRepository.findAll().forEach(System.out::println);
        };
    }

}

@Slf4j
@RefreshScope
@RestController
class MessageController {

    private final String msg;

    @Autowired
    public MessageController(@Value("${message}") String msg){
        this.msg=msg;
    }

    @Autowired
    private Source source;

    @PostMapping(value = "/accounts")
    public void write(@RequestBody Account account) {
        source.output().send(MessageBuilder.withPayload(account.getMob()).build());
        log.info("kafka send!");
    }

    @RequestMapping("/msg")
    public String read() {
        return msg;
    }
}

@Repository
@RepositoryRestResource
interface AccountRepository extends JpaRepository<Account, Long> {

    @RestResource(path = "by-mob")
    Account findByMob(@Param("mob") String mob);

}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
class Account {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String mob;

}

