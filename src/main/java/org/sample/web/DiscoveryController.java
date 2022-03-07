package org.sample.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * nacos测试代码
 */
@Controller
@RequestMapping("discovery")
public class DiscoveryController {

//    @NacosInjected
//    private NamingService namingService;

//    @RequestMapping(value = "/get", method = GET)
//    @ResponseBody
//    public List<Instance> get(@RequestParam String serviceName) throws NacosException {
//        return namingService.getAllInstances(serviceName);
//    }
}

