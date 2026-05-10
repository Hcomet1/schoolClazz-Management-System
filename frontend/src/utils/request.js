import axios from "axios";
import { ElMessage } from "element-plus";
import router from "@/router";

const request = axios.create({
  baseURL: "/api",
  timeout: 600000,
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

request.interceptors.request.use(
  (config) => {
    const loginUser = getLoginUser();
    if (loginUser?.token) {
      config.headers.token = loginUser.token;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const status = error?.response?.status;
    if (status === 401) {
      ElMessage.error("登录已过期，请重新登录");
      localStorage.removeItem("loginUser");
      router.push("/login");
    } else {
      ElMessage.error(error?.response?.data?.msg || "接口请求失败");
    }
    return Promise.reject(error);
  }
);

export default request;
