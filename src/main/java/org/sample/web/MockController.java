package org.sample.web;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dingwei
 */
@RequestMapping(value = "/mock")
@RestController
@Log4j2
public class MockController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String mockTest(String clazz, String method, String[] args) {



        return "";
    }

}
