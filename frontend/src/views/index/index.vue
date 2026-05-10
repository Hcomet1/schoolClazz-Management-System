<template>
  <div class="home">
    <div class="hero">
      <h1>JerryMouse Campus Class Management</h1>
      <p>面向校园场景的班级、学生、教职工、统计分析与操作日志综合管理平台。</p>
    </div>

    <div class="cards">
      <div class="card">
        <h3>班级管理</h3>
        <p>维护班级名称、学段、教室、班主任与开学结业周期。</p>
      </div>
      <div class="card">
        <h3>学生管理</h3>
        <p>记录学号、年级层次、住校情况、违纪次数与班级归属。</p>
      </div>
      <div class="card">
        <h3>教职工管理</h3>
        <p>管理班主任、任课教师、年级主任、学科组长、副校长与校长岗位。</p>
      </div>
      <div class="card">
        <h3>统计分析</h3>
        <p>可视化展示班级人数、学生层次结构和教职工分布。</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home {
  min-height: 100%;
}

.hero {
  padding: 36px;
  border-radius: 16px;
  color: #fff;
  background: linear-gradient(135deg, #0f4c81 0%, #2f7d32 100%);
  margin-bottom: 24px;
}

.hero h1 {
  margin: 0 0 12px;
  font-size: 34px;
}

.hero p {
  margin: 0;
  font-size: 16px;
  opacity: 0.92;
}

.cards {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18px;
}

.card {
  background: #fff;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 8px 20px rgba(15, 76, 129, 0.08);
}

.card h3 {
  margin: 0 0 10px;
  color: #0f4c81;
}

.card p {
  margin: 0;
  color: #5f6b7a;
  line-height: 1.7;
}
</style>
