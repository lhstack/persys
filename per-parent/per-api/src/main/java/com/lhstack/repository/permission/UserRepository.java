package com.lhstack.repository.permission;

import com.lhstack.entity.permission.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    Page<User> findByUsernameIsNot(String username, Pageable pageable);

    void deleteByIdIn(Iterable<Long> ids);

    User findByEmail(String email);

    long countByUsername(String username);

    long countByNickName(String username);

    long countByEmail(String email);

    User findByNickName(String nickName);
}
