package com.sso.web.application.Controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.common.entity.WmsWarehouse;
import com.sso.service.IWmsWarehouseService;
import com.sso.web.application.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lvxiaozuo
 * @since 2022-01-24
 */
@Controller
@RequestMapping("/wms-warehouse")
@ResponseBody
public class WmsWarehouseController {
    @Autowired
    private IWmsWarehouseService iWmsWarehouseService;


    /**
     * 查询物料表数据
     *
     * @param
     * @return
     */
    @PostMapping("/queryWarehouseList")
    public Result queryWarehouseList(@RequestBody Map param) throws Exception {
        IPage<WmsWarehouse> materialIPage= iWmsWarehouseService.queryWarehouseList(param);
        return Result.ok(materialIPage);
    }

    /**
     * 保存物料
     *
     * @param
     * @return
     */
    @PostMapping("/saveWarehouse")
    public Result saveWarehouse(@RequestBody Map param) {
        iWmsWarehouseService.saveWarehouse(param);
        return Result.ok();
    }

    /**
     * 删除物料
     *
     * @param
     * @return
     */
    @PostMapping("/deleteWarehouse")
    public Result deleteWarehouse(@RequestBody Map param) {
        iWmsWarehouseService.deleteWarehouse(param);
        return Result.ok();
    }
}
