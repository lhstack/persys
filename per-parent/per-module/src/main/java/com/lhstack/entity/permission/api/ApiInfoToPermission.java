package com.lhstack.entity.permission.api;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Accessors(chain = true)
@Data
@Table(name = "tb_api_info_permission",indexes = {
        @Index(name = "uniqueKey",columnList = "api_info_id,permission_id",unique = true)
})
public class ApiInfoToPermission implements Serializable {
    private static final long serialVersionUID = 5675769339478054375L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "api_info_id")
    private Long apiInfoId;

    @Column(name = "permission_id")
    private Long permissionId;
}
