package org.sample.service;

import java.util.List;

public interface MongoDbBaseService<T> {

    Class<T> getEntityClass();

    T save(T t);

    T queryById(Integer id);

    List<T> queryList(T object);

    T queryOne(T object);

    List<T> getPage(T object, int start, int size);

    Long getCount(T object);

    int delete(T t);

    void deleteById(Integer id);

    void updateFirst(T srcObj, T targetObj);

}
