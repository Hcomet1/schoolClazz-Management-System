<script setup>
import { onMounted, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { addApi, deleteApi, handleViolationApi, queryInfoApi, queryPageApi, updateApi } from "@/api/stu";
import { queryAllClazzApi } from "@/api/clazz";

const stageOptions = [
  { label: "小学", value: 1 },
  { label: "初中", value: 2 },
  { label: "高中", value: 3 },
];

const genderOptions = [
  { label: "男", value: 1 },
  { label: "女", value: 2 },
];

const boardingOptions = [
  { label: "住校", value: 1 },
  { label: "走读", value: 0 },
];

const searchForm = ref({
  clazzId: "",
  gradeLevel: "",
  name: "",
});

const clazzOptions = ref([]);
const studentList = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const dialogVisible = ref(false);
const dialogTitle = ref("新增学生");
const studentFormRef = ref();

const student = ref({
  name: "",
  no: "",
  gender: 1,
  phone: "",
  idCard: "",
  isBoarding: 0,
  address: "",
  gradeLevel: null,
  enrollmentDate: "",
  clazzId: null,
  violationCount: 0,
  violationScore: 0,
});

const rules = {
  name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  no: [{ required: true, message: "请输入学号", trigger: "blur" }],
  gender: [{ required: true, message: "请选择性别", trigger: "change" }],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "请输入有效手机号", trigger: "blur" },
  ],
  idCard: [{ required: true, message: "请输入身份证号", trigger: "blur" }],
  gradeLevel: [{ required: true, message: "请选择年级层次", trigger: "change" }],
  clazzId: [{ required: true, message: "请选择班级", trigger: "change" }],
};

onMounted(() => {
  loadClazzOptions();
  search();
});

const loadClazzOptions = async () => {
  const result = await queryAllClazzApi("", "", "", 1, 1000);
  if (result.code) {
    clazzOptions.value = result.data.rows || [];
  }
};

const search = async () => {
  const result = await queryPageApi(
    searchForm.value.clazzId,
    searchForm.value.gradeLevel,
    searchForm.value.name,
    currentPage.value,
    pageSize.value
  );
  if (result.code) {
    studentList.value = result.data.rows;
    total.value = result.data.total;
  }
};

const clear = () => {
  searchForm.value = { clazzId: "", gradeLevel: "", name: "" };
  search();
};

const handleSizeChange = () => search();
const handleCurrentChange = () => search();

const openAddDialog = () => {
  dialogTitle.value = "新增学生";
  dialogVisible.value = true;
  student.value = {
    name: "",
    no: "",
    gender: 1,
    phone: "",
    idCard: "",
    isBoarding: 0,
    address: "",
    gradeLevel: null,
    enrollmentDate: "",
    clazzId: null,
    violationCount: 0,
    violationScore: 0,
  };
  studentFormRef.value?.resetFields();
};

const editStudent = async (id) => {
  const result = await queryInfoApi(id);
  if (result.code) {
    dialogTitle.value = "编辑学生";
    dialogVisible.value = true;
    student.value = result.data;
  }
};

const save = async () => {
  if (!studentFormRef.value) return;
  studentFormRef.value.validate(async (valid) => {
    if (!valid) return;
    const request = student.value.id ? updateApi : addApi;
    const result = await request(student.value);
    if (result.code) {
      ElMessage.success("保存成功");
      dialogVisible.value = false;
      search();
    } else {
      ElMessage.error(result.msg);
    }
  });
};

const deleteStudents = async (ids) => {
  const targetIds = Array.isArray(ids) ? ids : [ids];
  ElMessageBox.confirm("确认删除所选学生记录吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      const result = await deleteApi(targetIds.join(","));
      if (result.code) {
        ElMessage.success("删除成功");
        search();
      } else {
        ElMessage.error(result.msg);
      }
    })
    .catch(() => {});
};

const addViolation = async (id, score) => {
  const result = await handleViolationApi(id, score);
  if (result.code) {
    ElMessage.success("违纪记录已更新");
    search();
  } else {
    ElMessage.error(result.msg);
  }
};

const stageLabel = (value) => stageOptions.find((item) => item.value === value)?.label || "其他";
const genderLabel = (value) => genderOptions.find((item) => item.value === value)?.label || "未知";
const boardingLabel = (value) => boardingOptions.find((item) => item.value === value)?.label || "未知";
</script>

<template>
  <h1>学生管理</h1>

  <div class="container">
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="姓名">
        <el-input v-model="searchForm.name" placeholder="请输入学生姓名" clearable />
      </el-form-item>
      <el-form-item label="年级层次">
        <el-select v-model="searchForm.gradeLevel" placeholder="请选择" clearable>
          <el-option v-for="item in stageOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="班级">
        <el-select v-model="searchForm.clazzId" placeholder="请选择班级" clearable style="width: 220px">
          <el-option v-for="item in clazzOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="info" plain @click="clear">清空</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="container">
    <el-button type="primary" @click="openAddDialog">+ 新增学生</el-button>
  </div>

  <div class="container">
    <el-table :data="studentList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="name" label="姓名" width="100" align="center" />
      <el-table-column prop="no" label="学号" width="120" align="center" />
      <el-table-column prop="gender" label="性别" width="80" align="center">
        <template #default="{ row }">{{ genderLabel(row.gender) }}</template>
      </el-table-column>
      <el-table-column prop="gradeLevel" label="年级" width="110" align="center">
        <template #default="{ row }">{{ stageLabel(row.gradeLevel) }}</template>
      </el-table-column>
      <el-table-column prop="clazzName" label="班级" min-width="150" />
      <el-table-column prop="isBoarding" label="住校情况" width="100" align="center">
        <template #default="{ row }">{{ boardingLabel(row.isBoarding) }}</template>
      </el-table-column>
      <el-table-column prop="violationCount" label="违纪次数" width="90" align="center" />
      <el-table-column prop="violationScore" label="违纪扣分" width="80" align="center" />
      <el-table-column label="操作" width="260" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="editStudent(row.id)">编辑</el-button>
          <el-button link type="danger" @click="deleteStudents(row.id)">删除</el-button>
          <el-button link type="warning" @click="addViolation(row.id, 2)">违纪 +2</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div class="container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20, 30, 50]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="620px">
    <el-form ref="studentFormRef" :model="student" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input v-model="student.name" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学号" prop="no">
            <el-input v-model="student.no" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="性别" prop="gender">
            <el-select v-model="student.gender" style="width: 100%">
              <el-option v-for="item in genderOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="student.phone" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="student.idCard" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="住校情况">
            <el-select v-model="student.isBoarding" style="width: 100%">
              <el-option v-for="item in boardingOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="年级层次" prop="gradeLevel">
            <el-select v-model="student.gradeLevel" style="width: 100%">
              <el-option v-for="item in stageOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="班级" prop="clazzId">
            <el-select v-model="student.clazzId" style="width: 100%">
              <el-option v-for="item in clazzOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="入学日期">
            <el-date-picker
              v-model="student.enrollmentDate"
              type="date"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="家庭住址">
            <el-input v-model="student.address" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.container {
  margin: 12px 5px;
}
</style>
