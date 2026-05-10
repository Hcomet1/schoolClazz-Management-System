<script setup>
import { onMounted, reactive, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  queryAllClazzApi,
  addClazzApi,
  queryInfoApi,
  updateClazzApi,
  deleteClazzApi,
} from "@/api/clazz";
import { queryMasterListApi } from "@/api/emp";

const stageOptions = {
  1: "小学",
  2: "初中",
  3: "高中",
};

const searchForm = reactive({
  name: "",
  begin: "",
  end: "",
  date: [],
});

const clazzList = ref([]);
const masterList = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const dialogFormVisible = ref(false);
const formTitle = ref("");
const clazzFormRef = ref();
const formLabelWidth = "96px";

const newClazz = ref({
  name: "",
  room: "",
  beginDate: "",
  endDate: "",
  masterId: null,
  stage: null,
});

const rules = {
  name: [
    { required: true, message: "请输入班级名称", trigger: "blur" },
    { min: 2, max: 30, message: "长度应为 2 到 30 个字符", trigger: "blur" },
  ],
  room: [{ required: true, message: "请输入教室", trigger: "blur" }],
  beginDate: [{ required: true, message: "请选择开学日期", trigger: "change" }],
  endDate: [{ required: true, message: "请选择结业日期", trigger: "change" }],
  masterId: [{ required: true, message: "请选择班主任", trigger: "change" }],
  stage: [{ required: true, message: "请选择学段", trigger: "change" }],
};

onMounted(() => {
  search();
  loadMasters();
});

const loadMasters = async () => {
  const result = await queryMasterListApi();
  if (result.code) {
    masterList.value = result.data || [];
  }
};

const search = async () => {
  const result = await queryAllClazzApi(
    searchForm.name,
    searchForm.begin,
    searchForm.end,
    currentPage.value,
    pageSize.value
  );
  if (result.code) {
    clazzList.value = result.data.rows;
    total.value = result.data.total;
  } else {
    ElMessage.error(result.msg);
  }
};

const clear = () => {
  searchForm.name = "";
  searchForm.begin = "";
  searchForm.end = "";
  searchForm.date = [];
  search();
};

const syncDateRange = () => {
  if (searchForm.date?.length === 2) {
    [searchForm.begin, searchForm.end] = searchForm.date;
  } else {
    searchForm.begin = "";
    searchForm.end = "";
  }
};

const addClazz = () => {
  formTitle.value = "新增班级";
  dialogFormVisible.value = true;
  newClazz.value = {
    name: "",
    room: "",
    beginDate: "",
    endDate: "",
    masterId: null,
    stage: null,
  };
  clazzFormRef.value?.resetFields();
};

const edit = async (id) => {
  const result = await queryInfoApi(id);
  if (!result.code) {
    ElMessage.error(result.msg);
    return;
  }
  newClazz.value = result.data;
  formTitle.value = "编辑班级";
  dialogFormVisible.value = true;
};

const save = async () => {
  if (!clazzFormRef.value) return;
  await clazzFormRef.value.validate(async (valid) => {
    if (!valid) return;
    const request = newClazz.value.id ? updateClazzApi : addClazzApi;
    const result = await request(newClazz.value);
    if (result.code) {
      ElMessage.success("保存成功");
      dialogFormVisible.value = false;
      search();
    } else {
      ElMessage.error(result.msg);
    }
  });
};

const delById = async (id) => {
  ElMessageBox.confirm("确认删除这个班级吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      const result = await deleteClazzApi(id);
      if (result.code) {
        ElMessage.success("删除成功");
        search();
      } else {
        ElMessage.error(result.msg);
      }
    })
    .catch(() => {});
};

const handleSizeChange = () => search();
const handleCurrentChange = () => search();
const stageLabel = (value) => stageOptions[value] || "其他";
</script>

<template>
  <h1>班级管理</h1>

  <div class="container">
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="班级名称">
        <el-input v-model="searchForm.name" placeholder="请输入班级名称" clearable />
      </el-form-item>
      <el-form-item label="在校周期">
        <el-date-picker
          v-model="searchForm.date"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
          @change="syncDateRange"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search">查询</el-button>
        <el-button type="info" plain @click="clear">清空</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="container">
    <el-button type="primary" @click="addClazz">+ 新增班级</el-button>
  </div>

  <div class="container">
    <el-table :data="clazzList" border style="width: 100%">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="name" label="班级名称" min-width="180" />
      <el-table-column prop="stage" label="学段" width="120" align="center">
        <template #default="{ row }">{{ stageLabel(row.stage) }}</template>
      </el-table-column>
      <el-table-column prop="room" label="教室" width="110" align="center" />
      <el-table-column prop="masterName" label="班主任" width="140" align="center" />
      <el-table-column prop="beginDate" label="开学日期" width="120" align="center" />
      <el-table-column prop="endDate" label="结业日期" width="120" align="center" />
      <el-table-column prop="status" label="状态" width="100" align="center" />
      <el-table-column prop="updateTime" label="最后更新时间" min-width="160" align="center" />
      <el-table-column label="操作" width="160" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="edit(row.id)">编辑</el-button>
          <el-button link type="danger" @click="delById(row.id)">删除</el-button>
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

  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="520px">
    <el-form ref="clazzFormRef" :model="newClazz" :rules="rules" :label-width="formLabelWidth">
      <el-form-item label="班级名称" prop="name">
        <el-input v-model="newClazz.name" placeholder="例如：高一（2）班" />
      </el-form-item>
      <el-form-item label="学段" prop="stage">
        <el-select v-model="newClazz.stage" placeholder="请选择学段" style="width: 100%">
          <el-option v-for="(label, value) in stageOptions" :key="value" :label="label" :value="Number(value)" />
        </el-select>
      </el-form-item>
      <el-form-item label="教室" prop="room">
        <el-input v-model="newClazz.room" placeholder="例如：教学楼 A201" />
      </el-form-item>
      <el-form-item label="班主任" prop="masterId">
        <el-select v-model="newClazz.masterId" placeholder="请选择班主任" style="width: 100%">
          <el-option v-for="item in masterList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="开学日期" prop="beginDate">
        <el-date-picker
          v-model="newClazz.beginDate"
          type="date"
          placeholder="选择日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="结业日期" prop="endDate">
        <el-date-picker
          v-model="newClazz.endDate"
          type="date"
          placeholder="选择日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          style="width: 100%"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormVisible = false">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.container {
  margin: 12px 2px;
}
</style>
