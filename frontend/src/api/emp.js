import request from "@/utils/request";

export const queryEmpListApi = (name, gender, begin, end, page, pageSize) =>
  request.get("/emps", {
    params: { name, gender, begin, end, page, pageSize },
  });

export const addEmpApi = (emp) => request.post("/emps", emp);

export const queryInfoApi = (id) => request.get(`/emps/${id}`);

export const updateEmpApi = (emp) => request.put("/emps", emp);

export const deleteEmpsApi = (ids) => request.delete("/emps", { params: { ids } });

export const queryMasterListApi = () => request.get("/emps/list");
