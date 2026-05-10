<script setup>
import { Plus } from "@element-plus/icons-vue";
import { ref, onMounted, watch } from "vue";
import {
  queryEmpListApi,
  addEmpApi,
  queryInfoApi,
  updateEmpApi,
  deleteEmpsApi,
} from "@/api/emp";
import { ElMessage, ElMessageBox } from "element-plus";

const token = ref("");
const empList = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const dialogVisible = ref(false);
const dialogTitle = ref("新增教职工");
const selectIds = ref([]);
const empFormRef = ref();

const genderOptions = [
  { name: "男", value: 1 },
  { name: "女", value: 2 },
];

const jobOptions = [
  { name: "班主任", value: 1 },
  { name: "任课教师", value: 2 },
  { name: "年级主任", value: 3 },
  { name: "学科组长", value: 4 },
  { name: "副校长", value: 5 },
  { name: "校长", value: 6 },
];

const empSearch = ref({
  name: "",
  gender: "",
  entryDate: [],
  begin: "",
  end: "",
});

const employee = ref({
  username: "",
  name: "",
  gender: "",
  phone: "",
  job: "",
  salary: "",
  entryDate: "",
  image: "",
  exprList: [],
});

const rules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 2, max: 20, message: "长度应为 2 到 20 个字符", trigger: "blur" },
  ],
  name: [
    { required: true, message: "请输入姓名", trigger: "blur" },
    { min: 2, max: 20, message: "长度应为 2 到 20 个字符", trigger: "blur" },
  ],
  gender: [{ required: true, message: "请选择性别", trigger: "change" }],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "请输入有效手机号", trigger: "blur" },
  ],
  job: [{ required: true, message: "请选择岗位", trigger: "change" }],
  entryDate: [{ required: true, message: "请选择入职日期", trigger: "change" }],
};

const getToken = () => {
  try {
    const loginUser = JSON.parse(localStorage.getItem("loginUser") || "null");
    if (loginUser?.token) token.value = loginUser.token;
  } catch {
    localStorage.removeItem("loginUser");
  }
};

onMounted(() => {
  getToken();
  search();
});

watch(
  () => empSearch.value.entryDate,
  (newVal) => {
    if (newVal.length === 2) {
      empSearch.value.begin = newVal[0];
      empSearch.value.end = newVal[1];
    } else {
      empSearch.value.begin = "";
      empSearch.value.end = "";
    }
  }
);

watch(
  () => employee.value.exprList,
  () => {
    employee.value.exprList.forEach((expr) => {
      if (expr.exprDate?.length === 2) {
        expr.begin = expr.exprDate[0];
        expr.end = expr.exprDate[1];
      }
    });
  },
  { deep: true }
);

const search = async () => {
  const result = await queryEmpListApi(
    empSearch.value.name,
    empSearch.value.gender,
    empSearch.value.begin,
    empSearch.value.end,
    currentPage.value,
    pageSize.value
  );
  if (result.code) {
    empList.value = result.data.rows;
    total.value = result.data.total;
  }
};

const clear = () => {
  empSearch.value = {
    name: "",
    gender: "",
    entryDate: [],
    begin: "",
    end: "",
  };
  search();
};

const handleSizeChange = () => search();
const handleCurrentChange = () => search();

const addEmp = () => {
  dialogTitle.value = "新增教职工";
  dialogVisible.value = true;
  employee.value = {
    username: "",
    name: "",
    gender: "",
    phone: "",
    job: "",
    salary: "",
    entryDate: "",
    image: "",
    exprList: [],
  };
  empFormRef.value?.resetFields();
};

const addExprItem = () => {
  employee.value.exprList.push({
    begin: "",
    end: "",
    exprDate: [],
    schoolName: "",
    job: "",
  });
};

const delExprItem = (index) => {
  employee.value.exprList.splice(index, 1);
};

const handleAvatarSuccess = (response) => {
  employee.value.image = response.data;
};

const handleUploadError = () => {
  ElMessage.error("上传失败，请重新登录");
};

const beforeAvatarUpload = (rawFile) => {
  const isImage = rawFile.type === "image/jpeg" || rawFile.type === "image/png";
  const isLt10M = rawFile.size / 1024 / 1024 < 10;
  if (!isImage) {
    ElMessage.error("只支持 JPG 或 PNG 图片");
    return false;
  }
  if (!isLt10M) {
    ElMessage.error("图片大小不能超过 10MB");
    return false;
  }
  return true;
};

const save = async () => {
  if (!empFormRef.value) return;
  empFormRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error("请正确填写表单信息");
      return;
    }

    const result = employee.value.id ? await updateEmpApi(employee.value) : await addEmpApi(employee.value);
    if (result.code) {
      ElMessage.success("保存成功");
      dialogVisible.value = false;
      search();
    } else {
      ElMessage.error(result.msg);
    }
  });
};

const editEmp = async (id) => {
  const result = await queryInfoApi(id);
  if (result.code) {
    dialogTitle.value = "编辑教职工";
    dialogVisible.value = true;
    employee.value = result.data;
    employee.value.exprList = (employee.value.exprList || []).map((expr) => ({
      ...expr,
      exprDate: expr.begin && expr.end ? [expr.begin, expr.end] : [],
    }));
  }
};

const delEmp = async (id) => {
  ElMessageBox.confirm("确认删除这条教职工记录吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      const result = await deleteEmpsApi([id]);
      if (result.code) {
        ElMessage.success("删除成功");
        search();
      } else {
        ElMessage.error(result.msg);
      }
    })
    .catch(() => {});
};

const delEmps = async () => {
  if (selectIds.value.length === 0) {
    ElMessage.warning("请至少选择一条记录");
    return;
  }

  ElMessageBox.confirm("确认删除选中的教职工记录吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      const result = await deleteEmpsApi(selectIds.value);
      if (result.code) {
        ElMessage.success(`已删除 ${selectIds.value.length} 条记录`);
        search();
      } else {
        ElMessage.error(result.msg);
      }
    })
    .catch(() => {});
};

const handleSelectionChange = (val) => {
  selectIds.value = val.map((emp) => emp.id);
};

const genderLabel = (value) => genderOptions.find((item) => item.value === value)?.name || "未知";
const jobLabel = (value) => jobOptions.find((item) => item.value === value)?.name || "其他";
</script>

<template>
  <h1>教职工管理</h1>

  <div class="container">
    <el-form :inline="true" :model="empSearch">
      <el-form-item label="姓名">
        <el-input v-model="empSearch.name" placeholder="请输入姓名" clearable />
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="empSearch.gender" placeholder="请选择" clearable>
          <el-option v-for="item in genderOptions" :key="item.value" :label="item.name" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="入职日期">
        <el-date-picker
          v-model="empSearch.entryDate"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="info" plain @click="clear">清空</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="container">
    <el-button type="primary" plain @click="addEmp">+ 新增教职工</el-button>
    <el-button type="danger" plain @click="delEmps">- 删除教职工</el-button>
  </div>

  <div class="container">
    <el-table :data="empList" border style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="name" label="姓名" width="100" align="center" />
      <el-table-column prop="gender" label="性别" width="80" align="center">
        <template #default="{ row }">{{ genderLabel(row.gender) }}</template>
      </el-table-column>
      <el-table-column prop="image" label="头像" width="90" align="center">
        <template #default="{ row }">
          <el-image :src="row.image" style="height: 55px" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="job" label="岗位" width="140" align="center">
        <template #default="{ row }">{{ jobLabel(row.job) }}</template>
      </el-table-column>
      <el-table-column prop="salary" label="薪资" width="100" align="center" />
      <el-table-column prop="entryDate" label="入职日期" width="120" align="center" />
      <el-table-column prop="updateTime" label="最后更新时间" align="center" />
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" plain @click="editEmp(row.id)">编辑</el-button>
          <el-button type="danger" plain @click="delEmp(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div class="container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20, 30, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="760px">
    <el-form ref="empFormRef" :model="employee" label-width="90px" :rules="rules">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="employee.username" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="employee.name" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-select v-model="employee.gender" style="width: 100%">
              <el-option v-for="item in genderOptions" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="employee.phone" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="岗位" prop="job">
            <el-select v-model="employee.job" style="width: 100%">
              <el-option v-for="item in jobOptions" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="薪资">
            <el-input v-model="employee.salary" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="入职日期" prop="entryDate">
            <el-date-picker
              v-model="employee.entryDate"
              type="date"
              style="width: 100%"
              placeholder="选择日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="头像">
            <el-upload
              class="avatar-uploader"
              action="/api/upload"
              :headers="{ token }"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :on-error="handleUploadError"
              :before-upload="beforeAvatarUpload"
            >
              <img v-if="employee.image" :src="employee.image" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="履历">
            <el-button type="success" size="small" @click="addExprItem">+ 添加</el-button>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-for="(expr, index) in employee.exprList" :key="index" :gutter="8">
        <el-col :span="10">
          <el-form-item label="时间" label-width="90px">
            <el-date-picker
              v-model="expr.exprDate"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="学校" label-width="70px">
            <el-input v-model="expr.schoolName" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="岗位" label-width="70px">
            <el-input v-model="expr.job" />
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-form-item label-width="0">
            <el-button type="danger" @click="delExprItem(index)">删除</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
.container {
  margin: 12px 5px;
}

.avatar {
  height: 78px;
  width: 78px;
  object-fit: cover;
  border-radius: 10px;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  text-align: center;
  border-radius: 10px;
  border: 1px dashed var(--el-border-color);
}
</style>
