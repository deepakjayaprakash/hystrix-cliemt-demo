package com.example.hystrix.client.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class AppController {

    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseDTO client() {
        ResponseDTO responseDTO = new ResponseDTO();
        return responseDTO;
    }
}
