import request from "@/utils/request";

export const queryEmpJobDataApi = () => request.get("/report/empJobData");

export const queryEmpGenderDataApi = () => request.get("/report/empGenderData");

export const queryStudentCountDataApi = () => request.get("/report/studentCountData");

export const queryStudentStageDataApi = () => request.get("/report/studentStageData");
