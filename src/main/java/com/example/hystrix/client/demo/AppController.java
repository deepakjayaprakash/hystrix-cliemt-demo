package com.example.hystrix.client.demo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping
public class AppController {

    Logger logger = LoggerFactory.getLogger(AppController.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/clientWithoutCB", method = RequestMethod.GET)
    public ResponseDTO clientWithoutCB() {
        return restTemplate.getForObject("http://localhost:8081/getSimpleResponse", ResponseDTO.class);
    }

    /**
     * Proxy Layer with circuit breaker
     */
    @RequestMapping(value = "/client", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "getDefaultResponse", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "5000"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
    })
    public ResponseDTO clientWithCB() {
        ResponseDTO responseDTO;
        try {
            responseDTO = restTemplate.getForObject("http://localhost:8081/getSimpleResponse", ResponseDTO.class);
            logger.info("success_response: {}", responseDTO);
        } catch (Exception e) {
            logger.error("exception_from_method: {}", e.getMessage());
            throw e;
        }
        return responseDTO;
    }

    private ResponseDTO getDefaultResponse() {
        logger.info("in default method");
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setBody("defaultResponse");
        return responseDTO;
    }
}
