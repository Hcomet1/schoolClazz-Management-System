import request from "@/utils/request";

export const queryPageApi = (clazzId, gradeLevel, name, page, pageSize) =>
  request.get("/students", {
    params: { clazzId, gradeLevel, name, page, pageSize },
  });

export const addApi = (stu) => request.post("/students", stu);

export const queryInfoApi = (id) => request.get(`/students/${id}`);

export const updateApi = (stu) => request.put("/students", stu);

export const deleteApi = (ids) => request.delete(`/students/${ids}`);

export const handleViolationApi = (id, score) =>
  request.put(`/students/violation/${id}/${score}`);
