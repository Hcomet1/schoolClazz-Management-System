<script setup>
import {
  Document,
  Menu as IconMenu,
  HomeFilled,
  EditPen,
  SwitchButton,
  UserFilled,
  DataAnalysis,
} from "@element-plus/icons-vue";
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import { updatePasswordApi } from "@/api/user";

const router = useRouter();
const loginName = ref("");
const dialogVisible = ref(false);
const passwordFormRef = ref();
const passwordForm = ref({
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
});

const getLoginUser = () => {
  try {
    return JSON.parse(localStorage.getItem("loginUser") || "null");
  } catch (error) {
    console.warn("invalid loginUser in localStorage, cleared", error);
    localStorage.removeItem("loginUser");
    return null;
  }
};

onMounted(() => {
  const loginUser = getLoginUser();
  if (loginUser?.name) {
    loginName.value = loginUser.name;
  }
});

const passwordRules = {
  oldPassword: [{ required: true, message: "请输入当前密码", trigger: "blur" }],
  newPassword: [
    { required: true, message: "请输入新密码", trigger: "blur" },
    { min: 6, message: "密码至少 6 位", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, message: "请确认新密码", trigger: "blur" },
    {
      validator: (_, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
};

const openChangePasswordDialog = () => {
  dialogVisible.value = true;
  passwordForm.value = {
    oldPassword: "",
    newPassword: "",
    confirmPassword: "",
  };
  passwordFormRef.value?.resetFields();
};

const changePassword = async () => {
  if (!passwordFormRef.value) return;
  passwordFormRef.value.validate(async (valid) => {
    if (!valid) return;
    const result = await updatePasswordApi(passwordForm.value);
    if (result.code) {
      ElMessage.success("密码修改成功");
      dialogVisible.value = false;
    } else {
      ElMessage.error(result.msg);
    }
  });
};

const logout = () => {
  ElMessageBox.confirm("确认退出当前账号吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      localStorage.removeItem("loginUser");
      ElMessage.success("已退出登录");
      router.push("/login");
    })
    .catch(() => {});
};
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <span class="title">JerryMouse Campus Class Management</span>
        <span class="right-tool">
          <a href="javascript:void(0)" @click="openChangePasswordDialog">
            <el-icon><EditPen /></el-icon>
            修改密码
          </a>
          <span class="divider">|</span>
          <a href="javascript:void(0)" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            退出登录 {{ loginName }}
          </a>
        </span>
      </el-header>

      <el-container class="body-container">
        <el-aside width="220px" class="aside">
          <el-menu router default-active="/index">
            <el-menu-item index="/index">
              <el-icon><HomeFilled /></el-icon>
              首页
            </el-menu-item>

            <el-sub-menu index="/school">
              <template #title>
                <el-icon><IconMenu /></el-icon>
                班级与学生
              </template>
              <el-menu-item index="/clazz">班级管理</el-menu-item>
              <el-menu-item index="/stu">学生管理</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="/staff">
              <template #title>
                <el-icon><UserFilled /></el-icon>
                教职工管理
              </template>
              <el-menu-item index="/emp">教职工档案</el-menu-item>
            </el-sub-menu>

            <el-sub-menu index="/report">
              <template #title>
                <el-icon><DataAnalysis /></el-icon>
                统计报表
              </template>
              <el-menu-item index="/empReport">教职工统计</el-menu-item>
              <el-menu-item index="/stuReport">学生统计</el-menu-item>
            </el-sub-menu>

            <el-menu-item index="/log">
              <el-icon><Document /></el-icon>
              操作日志
            </el-menu-item>
          </el-menu>
        </el-aside>

        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>

    <el-dialog v-model="dialogVisible" title="修改密码" width="420px">
      <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="110px">
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="changePassword">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.common-layout {
  height: 100%;
  width: 100%;
}

.body-container {
  min-height: calc(100vh - 60px);
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(90deg, #0f4c81 0%, #2f7d32 100%);
}

.title {
  color: #fff;
  font-size: 28px;
  font-family: "Microsoft YaHei", sans-serif;
  font-weight: 700;
}

.right-tool {
  color: #fff;
  display: flex;
  align-items: center;
  gap: 12px;
}

.divider {
  opacity: 0.7;
}

a {
  color: #fff;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.aside {
  border-right: 1px solid #dcdfe6;
  background: #fff;
}

.main {
  background: #f7f9fc;
}
</style>
