package com.lhstack.mapper.permission;

import com.lhstack.entity.permission.Menu;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;
import java.util.Map;

public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> findAllMenuExample(Map<String, Object> initExample);
}
