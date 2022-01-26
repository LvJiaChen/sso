package com.sso.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.common.entity.WmsWarehouse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-24
 */
public interface IWmsWarehouseService extends IService<WmsWarehouse> {

    IPage<WmsWarehouse> queryWarehouseList(Map param) throws Exception;

    void saveWarehouse(Map param);

    void deleteWarehouse(Map param);
}
