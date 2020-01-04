package com.lhstack.service;

import org.springframework.data.domain.Page;

import java.util.List;

public interface IBaseService <ID,T>{

    /**
     * 通用保存方法
     * @param entity
     * @return
     */
    T save(T entity) throws Exception;

    /**
     * 根据id更新
     * @param id
     * @param entity
     * @return
     */
    T update(ID id,T entity) throws Exception;

    /**
     * 根据实体类删除
     * @param entity
     */
    void delete(T entity) throws Exception;

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(ID id) throws Exception;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    T findById(ID id) throws Exception;

    /**
     * 查询所有
     * @return
     */
    List<T> findAll() throws Exception;

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<T> findAll(Integer page, Integer size) throws Exception;
}
