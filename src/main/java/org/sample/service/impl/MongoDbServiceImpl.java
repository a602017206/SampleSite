package org.sample.service.impl;

import lombok.extern.log4j.Log4j2;
import org.sample.domain.Book;
import org.sample.service.MongoDbService;
import org.springframework.stereotype.Service;


@Service
@Log4j2
public class MongoDbServiceImpl extends MongoDbDao<Book> implements MongoDbService {

    @Override
    public Class<Book> getEntityClass() {
        return Book.class;
    }
}

