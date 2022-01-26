import Axios from "../response/index";

const baseUrl="http://localhost:8081";
/*登录、登出*/
export const submitFormLogin = data => {
    return Axios.post(baseUrl+"/wms-user/login",data);
};

export const loginOutHeader = data => {
    return Axios.post(baseUrl+"/wms-user/logout",data);
};
/*物料管理*/
/*查询物料*/
export const queryMaterialList = data => {
    return Axios.post(baseUrl+"/wms-material/queryMaterialList",data);
};
/*编辑保存物料*/
export const saveMaterial = data => {
    return Axios.post(baseUrl+"/wms-material/saveMaterial",data);
};
/*删除物料*/
export const deleteMaterial = data => {
    return Axios.post(baseUrl+"/wms-material/deleteMaterial",data);
};
/*物料下拉选择*/
export const selectMaterial = data => {
    return Axios.post(baseUrl+"/wms-material/selectMaterial",data);
};

/*仓库管理*/
/*查询仓库*/
export const queryWarehouseList = data => {
    return Axios.post(baseUrl+"/wms-warehouse/queryWarehouseList",data);
};
/*编辑保存仓库*/
export const saveWarehouse = data => {
    return Axios.post(baseUrl+"/wms-warehouse/saveWarehouse",data);
};
/*删除仓库*/
export const deleteWarehouse = data => {
    return Axios.post(baseUrl+"/wms-warehouse/deleteWarehouse",data);
};
