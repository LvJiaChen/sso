<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-calendar"></i> 物料管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-input v-model="query.materialNo" placeholder="物料编码" class="handle-input mr10"></el-input>
        <el-input v-model="query.materialName" placeholder="物料名称" class="handle-input mr10"></el-input>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button type="primary" icon="el-icon-search" @click="handleAddEdit(null,null)">添加</el-button>
      </div>
      <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header">
        <el-table-column prop="id" label="ID" width="55" align="center"></el-table-column>
        <el-table-column prop="materialNo" label="物料编码" width="170"></el-table-column>
        <el-table-column prop="materialName" label="物料名称" width="170"></el-table-column>
        <el-table-column prop="brand" label="品牌"></el-table-column>
        <el-table-column prop="space" label="规格型号"></el-table-column>
        <el-table-column label="价格" width="100">
          <template #default="scope">￥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="100"></el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
        <el-table-column prop="creator" label="创建人"></el-table-column>
        <el-table-column prop="createTime" width="160" label="创建时间">
          <template #default="scope">
            {{operatingTime(scope.row.createTime)}}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button type="text" icon="el-icon-edit" @click="handleAddEdit(scope.$index, scope.row)">编辑
            </el-button>
            <el-button type="text" icon="el-icon-delete" class="red"
                       @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                       :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
      </div>
    </div>
    <!-- 添加、编辑弹出框 -->
    <el-dialog title="添加/编辑" v-model="editVisible" width="30%" :before-close="dialogBeforeClose">
      <el-form label-width="90px" :model="form" :rules="rules" ref="formRef">
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="form.materialName"></el-input>
        </el-form-item>
        <el-form-item label="品牌">
          <el-input v-model="form.brand"></el-input>
        </el-form-item>
        <el-form-item label="规格型号">
          <el-input v-model="form.space"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number v-model="form.price" :min="0" precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
                <span class="dialog-footer">
                    <el-button @click="editVisible = false">取 消</el-button>
                    <el-button type="primary" @click="saveEdit">确 定</el-button>
                </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {reactive, ref} from "vue";
import {queryMaterialList,deleteMaterial,saveMaterial,selectMaterial} from "../api";
import {ElMessage, ElMessageBox} from "element-plus";
import moment from "moment";

export default {
  name: "MaterialList",
  setup() {
    const query = reactive({
      materialNo: null,
      materialName: null,
      pageIndex: 1,
      pageSize: 10,
    });
    const tableData = ref([]);
    const pageTotal = ref(0);
    const rules = {
      materialName: [
        {
          required: true,
          message: '请输入物料名称',
          trigger: 'blur',
        },
        {
          max: 10,
          message: '物料名称长度不能大于10',
          trigger: 'blur',
        },
      ],
      price:[
        {
          required: true,
          message: '请输入价格',
          trigger: 'blur',
        }
      ],
      unit: [
        {
          required: true,
          message: '请输入单位',
          trigger: 'blur',
        },
        {
          max: 5,
          message: '单位长度不能大于5',
          trigger: 'blur',
        },
      ],
      remark:[
        {
          max: 100,
          message: '备注长度不能大于100',
          trigger: 'blur',
        },
      ]
    }
    let formRef=ref(null);
    // 获取表格数据
    const getData = () => {
      queryMaterialList(query).then((res) => {
        ElMessage.success("查询成功");
        tableData.value = res.data.records;
        pageTotal.value = res.data.total;
      });
    };
    getData();

    // 查询操作
    const handleSearch = () => {
      selectMaterial({}).then((res) => {
        ElMessage.success("删除成功");
      });
      query.pageIndex = 1;
      getData();
    };
    // 分页导航
    const handlePageChange = (val) => {
      query.pageIndex = val;
      getData();
    };

    // 删除操作
    const handleDelete = (index,row) => {
      // 二次确认删除
      ElMessageBox.confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
          .then(() => {
            tableData.value.splice(index, 1);
            deleteMaterial({id:row.id}).then((res) => {
              ElMessage.success("删除成功");
            });
          })
          .catch(() => {});
    };

    // 表格编辑时弹窗和保存
    const editVisible = ref(false);
    let form = reactive({
      id:null,
      materialName: null,
      brand:null,
      space:null,
      price:null,
      unit:null,
      remark:null,
      version:null
    });
    const handleAddEdit = (index, row) => {
      if (row!==null){
        //编辑
        Object.keys(form).forEach((item) => {
          form[item] = row[item];
        });
      }else {
        form.id=null;
        form.materialName=null;
        form.brand=null;
        form.space=null;
        form.price=null;
        form.unit=null;
        form.remark=null;
        form.version=null;
      }
      editVisible.value = true;
    };
    const saveEdit = () => {
      formRef.value.validate((valid) => {
        if (valid) {
          editVisible.value = false;
          saveMaterial(form).then((res) => {
            ElMessage.success("保存成功");
            getData();
          });
        }
      })
    };
    const dialogBeforeClose=()=>{
      formRef.value.resetFields()
      editVisible.value = false;
    };
    const operatingTime=(timeDate)=>{
      return  moment(timeDate).format('yyyy-MM-DD HH:mm:ss');
    };
    return {
      query,
      tableData,
      pageTotal,
      editVisible,
      form,
      rules,
      formRef,
      operatingTime,
      dialogBeforeClose,
      handleSearch,
      handlePageChange,
      handleDelete,
      handleAddEdit,
      saveEdit,
    };
  },
}
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}
.red {
  color: #ff0000;
}
.mr10 {
  margin-right: 10px;
}
.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
</style>