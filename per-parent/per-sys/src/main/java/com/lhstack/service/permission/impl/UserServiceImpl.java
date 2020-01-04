package com.lhstack.service.permission.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhstack.annotation.RollbackTransactional;
import com.lhstack.aspect.sys.SysLog;
import com.lhstack.controller.excontroller.RegistryException;
import com.lhstack.entity.permission.User;
import com.lhstack.entity.permission.UserExampleDTO;
import com.lhstack.mapper.permission.RoleMapper;
import com.lhstack.mapper.permission.UserMapper;
import com.lhstack.repository.permission.UserRepository;
import com.lhstack.service.permission.IUserService;
import com.lhstack.utils.PasswordEncoderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoderUtils passwordEncoderUtils;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    @Cacheable(cacheNames = "users", key = "'findByNotExistThisAndExample' + @genertorKey.getKey(#p0)")
    public PageInfo<User> findByNotExistThisAndExample(UserExampleDTO example) {
        PageHelper.startPage(example.getPage(), example.getLimit());
        List<User> users = userMapper.findByNotExistThisAndExample(example);
        return PageInfo.of(users);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "users", key = "@genertorKey.getKey('findByUsername',#p0)")
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "users", key = "'findByUsernameNotIs' + @genertorKey.getKey(#p0,#p1,#p2)")
    public Page<User> findByUsernameNotIs(String username, Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }
        return userRepository.findByUsernameIsNot(username, PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createTime"))));
    }

    @Override
    @RollbackTransactional
    @SysLog
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void deleteAllByIds(List<Long> ids) {
        userRepository.deleteByIdIn(ids);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "users", allEntries = true)
    @SysLog
    public User updateAndRole(User user, List<Long> rids) {
        Optional<User> u = userRepository.findById(user.getId());
        User result = null;
        if (u.isPresent()) {
            User current = u.get();
            current.setEmail(user.getEmail())
                    .setIcon(user.getIcon())
                    .setIsDel(user.getIsDel())
                    .setIsLock(user.getIsLock())
                    .setNickName(user.getNickName());
            result = userRepository.save(current);
            roleMapper.deleteByUid(result.getId());
            roleMapper.insertByRids(result.getId(), rids);
        }
        return result;
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "users", allEntries = true)
    @SysLog
    public User saveUserAndRole(User user, List<Long> rids) {
        user.setId(null)
                .setSalt(passwordEncoderUtils.salt())
                .setPassword(passwordEncoderUtils.genPass(user.getSalt(), user.getPassword()))
                .setCreateTime(new Date());
        User result = this.save(user);
        roleMapper.insertByRids(result.getId(), rids);
        return result;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "users", key = "@genertorKey.getKey('findByEmail',#p0)")
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "users", allEntries = true)
    @SysLog
    public User registry(User entity) {
        checkUserIsPresent(entity);
        entity.setId(null)
                .setSalt(passwordEncoderUtils.salt())
                .setPassword(passwordEncoderUtils.genPass(entity.getSalt(), entity.getPassword()))
                .setCreateTime(new Date())
                .setIsLock(false)
                .setIsDel(false)
                .setIcon("/images/err.jpg");
        User result = userRepository.save(entity);
        Integer integer = roleMapper.insertByRids(result.getId(), Arrays.asList(0L));
        if (integer < 1) {
            throw new IllegalArgumentException("用户角色初始化失败");
        }
        return result;
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "users", allEntries = true)
    @SysLog
    public Long deleteByIdAndRole(Long id) {
        return userMapper.deleteByIdAndRole(id);
    }

    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "users", allEntries = true)
    @SysLog
    public Long deleteByIdsAndRole(List<Long> ids) {
        return userMapper.deleteByIdsAndRole(ids);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "users", key = "@genertorKey.getKey('findByNickName',#p0)")
    public User findByNickName(String nickName) {
        return userRepository.findByNickName(nickName);
    }


    private void checkUserIsPresent(User entity) {
        long countByNickName = userRepository.countByNickName(entity.getNickName());
        if (countByNickName > 0) {
            throw new RegistryException("昵称已存在");
        }
        long countByUsername = userRepository.countByUsername(entity.getUsername());
        if (countByUsername > 0) {
            throw new RegistryException("用户名已存在");
        }
        long countByEmail = userRepository.countByEmail(entity.getEmail());
        if (countByEmail > 0) {
            throw new RegistryException("邮箱已存在");
        }
    }


    @Override
    @RollbackTransactional
    @CacheEvict(cacheNames = "users", allEntries = true)
    @SysLog
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    @RollbackTransactional
    @SysLog
    @CacheEvict(cacheNames = "users", allEntries = true)
    public User update(Long id, User user) {
        if (id == null)
            throw new NullPointerException("传入的id为空");
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    @RollbackTransactional
    @SysLog
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    @RollbackTransactional
    @SysLog
    @CacheEvict(cacheNames = "users", allEntries = true)
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "users", key = "'findById' + @genertorKey.getKey(#p0)")
    public User findById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "users", key = "'findAll' + @genertorKey.getKey()")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cacheable(cacheNames = "users", key = "'findAll' + @genertorKey.getKey(#p0,#p1)")
    public Page<User> findAll(Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }
        return userRepository.findAll(PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createTime"))));
    }
}
