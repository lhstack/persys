package com.lhstack.entity.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "tb_menu",indexes = {
        @Index(name = "uniqueKey",columnList = "menu_name,href",unique = true)
})
@Entity
@Data
@Accessors(chain = true)
public class Menu implements Serializable {


    private static final long serialVersionUID = 497810560344431875L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,name = "menu_name")
    private String menuName;

    private Boolean enable;

    @Column(name = "is_parent")
    private Boolean isParent;

    private String icon;

    private String href;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "sort_by")
    private Integer sortBy;

    @Transient
    private List<Menu> child;
}
