package com.yyl.systemdemo.service.impl;

import com.yyl.entity.EPermission;
import com.yyl.systemdemo.dao.EPermissionMapper;
import com.yyl.systemdemo.service.IEPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 左侧菜单地址 服务实现类
 * </p>
 *
 * @author yuyongli
 * @since 2022-12-28
 */
@Service
public class EPermissionServiceImpl extends ServiceImpl<EPermissionMapper, EPermission> implements IEPermissionService {

    @Resource
    private EPermissionMapper permissionMapper;
    @Override
    public List<EPermission> getAllMenus() {

        return permissionMapper.getAllPermission();
    }
}