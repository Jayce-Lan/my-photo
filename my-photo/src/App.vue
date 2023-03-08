<template>
  <div id="app">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">相册展示</el-menu-item>
      <el-menu-item index="2">照片查询</el-menu-item>
      <el-menu-item index="3">照片管理</el-menu-item>
    </el-menu>
<!--    <img src="./assets/logo.png">-->
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      activeIndex: '1',
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      this.activeIndex = key;
      if (key == '1') {
        this.$router.push("/");
        return
      }
      if (key == "2") {
        this.$router.push("/queryPhoto");
        return
      }
      if (key == "3") {
        this.$router.push("/photoManage");
        return
      }
    }
  }
}
</script>

<style>
</style>
