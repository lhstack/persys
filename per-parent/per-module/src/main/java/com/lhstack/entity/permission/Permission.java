package com.lhstack.entity.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "tb_permission",indexes = {
        @Index(name = "uniqueKey",columnList = "permission_name",unique = true)
})
public class Permission implements Serializable {

    private static final long serialVersionUID = 6050607705989746478L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission_name",unique = true)
    private String permissionName;

    @Column(name = "logogram_name",unique = true)
    private String logogramName;

    private String icon;
}
