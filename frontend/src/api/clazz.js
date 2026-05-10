import request from "@/utils/request";

export const queryAllClazzApi = (name, begin, end, page, pageSize) =>
  request.get("/clazzs", {
    params: { name, begin, end, page, pageSize },
  });

export const addClazzApi = (clazz) => request.post("/clazzs", clazz);

export const queryInfoApi = (id) => request.get(`/clazzs/${id}`);

export const updateClazzApi = (clazz) => request.put("/clazzs", clazz);

export const deleteClazzApi = (id) => request.delete(`/clazzs/${id}`);
