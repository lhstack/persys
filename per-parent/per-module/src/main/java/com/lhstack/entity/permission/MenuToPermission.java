package com.lhstack.entity.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * CLASSNAME: ${CLASS_NAME}
 * author: hp
 * date: 2019/12/27
 **/
@Data
@Accessors(chain = true)
@Entity
@Table(name = "tb_menu_and_permission",indexes = {
        @Index(name = "uniqueKey",unique = true,columnList = "mid,pid")
})
public class MenuToPermission implements Serializable {
    private static final long serialVersionUID = -2161087597201881411L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mid;

    private Long pid;
}
