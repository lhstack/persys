package com.lhstack.repository.permission;

import com.lhstack.entity.permission.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Long> {
    List<Menu> findByParentId(Long parentId);

}
