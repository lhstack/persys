package com.lhstack.controller.permission;

public interface PermissionsQueryConst {

    String[] userPermissions = {
            "ADMIN_USER_QUERY","ADMIN_USER_ADD","ADMIN_USER_DELETE","ADMIN_USER_UPDATE"
    };

    int HAS_ROLE = 0;

    int HAS_PERMISSION = 1;

    int AUTHORITY_ANY = 2;

    int AUTHORITY_ALL = 3;
}
