<script setup>
import { ElMessage } from "element-plus";
import { ref } from "vue";
import { loginApi } from "@/api/login";
import { useRouter } from "vue-router";

const router = useRouter();
const loginForm = ref({ username: "", password: "" });

const login = async () => {
  const result = await loginApi(loginForm.value);
  if (result.code) {
    ElMessage.success("登录成功");
    localStorage.setItem("loginUser", JSON.stringify(result.data));
    router.push("/index");
  } else {
    ElMessage.error(result.msg);
  }
};

const clear = () => {
  loginForm.value.username = "";
  loginForm.value.password = "";
};
</script>

<template>
  <div id="container">
    <div class="login-form">
      <el-form label-width="90px">
        <p class="title">JerryMouse Campus Class Management</p>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>

        <el-form-item>
          <el-button class="button" type="primary" @click="login">登录</el-button>
          <el-button class="button" type="info" @click="clear">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  height: 100%;
}
</style>

<style scoped>
#container {
  padding: 10%;
  min-height: 100vh;
  display: flex;
  align-items: center;
  background-image: url("../../assets/bg1.jpg");
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
}

.login-form {
  max-width: 420px;
  width: 100%;
  padding: 30px;
  margin: 0 auto;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.35);
  background-color: rgba(255, 255, 255, 0.95);
}

.title {
  font-size: 28px;
  font-family: "Microsoft YaHei", sans-serif;
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold;
  color: #0f4c81;
}

.button {
  margin-top: 30px;
  width: 120px;
}
</style>
