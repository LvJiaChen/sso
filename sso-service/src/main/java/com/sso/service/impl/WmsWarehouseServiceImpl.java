package com.sso.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.common.entity.WmsWarehouse;
import com.sso.common.mapper.WmsWarehouseMapper;
import com.sso.service.IWmsSerialNumberService;
import com.sso.service.IWmsWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-24
 */
@Service
public class WmsWarehouseServiceImpl extends ServiceImpl<WmsWarehouseMapper, WmsWarehouse> implements IWmsWarehouseService {

    @Autowired
    private WmsWarehouseMapper wmsWarehouseMapper;
    @Autowired
    private IWmsSerialNumberService iWmsSerialNumberService;

    @Override
    public IPage<WmsWarehouse> queryWarehouseList(Map param) throws Exception {
        QueryWrapper<WmsWarehouse> queryWrapper=new QueryWrapper<>();
        if (!StrUtil.isBlank((String)param.get("address")))
            queryWrapper.like("address",param.get("address"));
        if (!StrUtil.isBlank((String)param.get("name")))
            queryWrapper.like("name",param.get("name"));
        Page<WmsWarehouse> warehousePage = new Page<>((Integer)param.get("pageIndex") , (Integer)param.get("pageSize") , false);
        warehousePage.setSearchCount(true);
        IPage<WmsWarehouse> warehouseIPage =  wmsWarehouseMapper.selectPage(warehousePage,queryWrapper);
        return warehouseIPage;
    }

    @Override
    public void saveWarehouse(Map param) {
        WmsWarehouse warehouse= BeanUtil.toBean(param,WmsWarehouse.class, CopyOptions.create());
        if (warehouse.getId()==null){
            String code= iWmsSerialNumberService.generateSerialNumberByModelCode("wms_warehouse");
            warehouse.setCode(code);
            //添加
            wmsWarehouseMapper.insert(warehouse);
        }else {
            //编辑
            wmsWarehouseMapper.updateById(warehouse);
        }
    }

    @Override
    public void deleteWarehouse(Map param) {
        wmsWarehouseMapper.deleteById((Integer)param.get("id"));
    }
}
