import request from "@/utils/request";

export const updatePasswordApi = (data) => request.post("/emps/password", data);
