package com.lhstack.entity.permission.api;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "tb_api_info_role",indexes = {
        @Index(name = "uniqueKey",columnList = "api_info_id,role_id",unique = true)
})
@Entity
@Data
@Accessors(chain = true)
public class ApiInfoToRole implements Serializable {

    private static final long serialVersionUID = -7064159134222463022L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_info_id")
    private Long apiInfoId;

    @Column(name = "role_id")
    private Long roleId;
}
