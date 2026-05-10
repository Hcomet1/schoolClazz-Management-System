<script setup>
import { onMounted } from "vue";
import * as echarts from "echarts";
import {
  queryStudentCountDataApi,
  queryStudentStageDataApi,
} from "@/api/report";

onMounted(() => {
  loadStudentCountChart();
  loadStageChart();
});

const loadStudentCountChart = async () => {
  const result = await queryStudentCountDataApi();
  initStudentCountChart(result.data.clazzList, result.data.dataList);
};

const loadStageChart = async () => {
  const result = await queryStudentStageDataApi();
  initStageChart(result.data);
};

function initStudentCountChart(clazzList, dataList) {
  const myChart = echarts.init(document.getElementById("container1"));
  myChart.setOption({
    title: {
      text: "班级学生人数统计",
      left: "center",
      textStyle: { fontSize: 20 },
    },
    grid: { left: "3%", right: "4%", bottom: "3%", containLabel: true },
    tooltip: {},
    xAxis: { data: clazzList },
    yAxis: {},
    series: [
      {
        name: "人数",
        type: "bar",
        data: dataList,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 1, [
            { offset: 0, color: "#f28e2b" },
            { offset: 1, color: "#e15759" },
          ]),
        },
      },
    ],
  });
}

function initStageChart(stageDataList) {
  const myChart = echarts.init(document.getElementById("container2"));
  myChart.setOption({
    title: {
      text: "学生年级层次统计",
      left: "center",
      textStyle: { fontSize: 20 },
    },
    tooltip: { trigger: "item" },
    legend: { top: "10%", left: "center" },
    series: [
      {
        name: "年级层次",
        type: "pie",
        radius: ["40%", "70%"],
        top: "5%",
        itemStyle: {
          borderRadius: 5,
          borderColor: "#fff",
          borderWidth: 2,
        },
        label: { show: false, position: "center" },
        emphasis: {
          label: { show: true, fontSize: 20, fontWeight: "bold" },
        },
        data: stageDataList,
      },
    ],
  });
}
</script>

<template>
  <div class="report_container" id="container1"></div>
  <div class="report_container" id="container2"></div>
</template>

<style scoped>
.report_container {
  width: 49%;
  height: 520px;
  float: left;
  margin-left: 5px;
  background: #fff;
}

#container1 {
  border-right: 1px dashed #ccc;
}
</style>
