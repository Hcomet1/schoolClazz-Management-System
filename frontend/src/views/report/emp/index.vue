<script setup>
import { onMounted } from "vue";
import * as echarts from "echarts";
import { queryEmpGenderDataApi, queryEmpJobDataApi } from "@/api/report";

onMounted(() => {
  loadJobChart();
  loadGenderChart();
});

const loadJobChart = async () => {
  const result = await queryEmpJobDataApi();
  initJobChart(result.data.jobList, result.data.dataList);
};

const loadGenderChart = async () => {
  const result = await queryEmpGenderDataApi();
  initGenderChart(result.data);
};

function initJobChart(jobList, dataList) {
  const myChart = echarts.init(document.getElementById("container1"));
  myChart.setOption({
    title: {
      text: "教职工岗位统计",
      left: "center",
      textStyle: { fontSize: 20 },
    },
    grid: { left: "3%", right: "4%", bottom: "3%", containLabel: true },
    tooltip: {},
    xAxis: { data: jobList },
    yAxis: {},
    series: [
      {
        name: "人数",
        type: "bar",
        data: dataList,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 1, 1, [
            { offset: 0, color: "#4e79a7" },
            { offset: 1, color: "#59a14f" },
          ]),
        },
      },
    ],
  });
}

function initGenderChart(genderDataList) {
  const myChart = echarts.init(document.getElementById("container2"));
  myChart.setOption({
    title: {
      text: "教职工性别统计",
      left: "center",
      textStyle: { fontSize: 20 },
    },
    tooltip: { trigger: "item" },
    legend: { top: "10%", left: "center" },
    series: [
      {
        name: "性别",
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
        data: genderDataList,
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
