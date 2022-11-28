package org.sample.config;


import org.apache.ibatis.session.SqlSessionFactory;

//@Configuration
public class DataSourceConfig  {

//    @Resource
    private SqlSessionFactory sqlSessionFactory;

//    @Bean
    public SqlSessionFactory sqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        return this.sqlSessionFactory;
    }

}
