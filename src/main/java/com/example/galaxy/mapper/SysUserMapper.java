package com.example.galaxy.mapper;

import com.example.galaxy.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {
    public int insertUser(SysUser sysUser);

    public int deleteUserById(Long id); //

    public int deleteUserByAccount(String account); // 同上

    public SysUser getUserById(Long userId); // 同上

    public SysUser getUserByAccount(String userAccount); // 同上

    public int updateUserByIdSelective(SysUser user);

    public int updateUserByAccountSelective(SysUser user);

    public List<SysUser> getAllSysUser();

    public int countUserByAccount(String account);
}