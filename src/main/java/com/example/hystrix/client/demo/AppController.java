package com.example.hystrix.client.demo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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

//    @Autowired
    private final RestTemplate restTemplate = new RestTemplate();

    @HystrixCommand(fallbackMethod = "getDefaultResponse")
    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseDTO client() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO = restTemplate.getForObject("localhost:8081/getSimpleResponse", ResponseDTO.class);
        return responseDTO;
    }

    private ResponseDTO getDefaultResponse() {
        logger.info("in default method");
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setBody("defaultResponse");
        return responseDTO;
    }
}
