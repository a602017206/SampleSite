package org.sample.service.impl;

import org.sample.service.ElasticSerachService;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.cluster.ClusterHealth;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ElasticSerachServiceImpl implements ElasticSerachService {

    @Resource
    private ElasticsearchRestTemplate template;


    public void test() {
        ClusterHealth health = template.cluster().health();
    }

}
