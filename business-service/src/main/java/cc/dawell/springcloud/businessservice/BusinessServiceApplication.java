package cc.dawell.springcloud.businessservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableBinding(Sink.class)
@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class BusinessServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessServiceApplication.class, args);
    }

}

@Slf4j
@Component
class AccountConsumer {

    @StreamListener(Sink.INPUT)
    public void write(String name) {
        log.info(name);
    }
}

@FeignClient("account-service")
interface AccountRead {

    @RequestMapping(method = RequestMethod.GET, value = "/msg")
    String read();

}

@RestController
class AccountController {

    @Autowired
    private LoadBalancerClient client;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccountRead accountRead;

    public String fallback() {
        return "掛了";
    }

    @HystrixCommand(fallbackMethod = "fallback")
    @RequestMapping("/account-msg")
    public String read() {
        return accountRead.read();
    }

    @RequestMapping("/account-msg2")
    public String read2() {
        ServiceInstance serviceInstance = client.choose("account-service");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/msg";
        return restTemplate.getForObject(url, String.class);
    }

}

/*@Data
class Account {
    private String mob;
}*/
