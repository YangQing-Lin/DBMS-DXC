<template>
  <div class="detail">
    <el-dialog title="历史记录" :visible.sync="dialogTableVisible" width="60%">
      <table class="table">
        <tr>
          <td>导出类型：{{ detail.type }}</td>
          <td>导出路径：{{ detail.path }}</td>
        </tr>
        <tr>
          <td rowspan="2">过滤条件：{{ detail.filter }}</td>
        </tr>
      </table>
      <el-divider>采集字段</el-divider>
      <el-table :data="tableData" height="250" border style="width: 100%">
        <el-table-column prop="index" label="序号" width="80">
        </el-table-column>
        <el-table-column prop="fieldComment" label="中文名">
        </el-table-column>
        <el-table-column prop="fieldName" label="英文名"> </el-table-column>
        <el-table-column prop="fieldType" label="类型"> </el-table-column>
        <el-table-column prop="fieldLength" label="长度"> </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import dataqueryapi from '@/api/dataquery'
export default {
  props: {
    hid: Number,
    detail: Object
  },
  data() {
    return {
      tableData: [],
      type: this.detail.type,
      path: this.detail.path,
      filter: this.detail.filter,
      dialogTableVisible: false,
    };
  },
  created: function() {
    // this.init()
  },
  methods: {
    async init() {
      let obj = {
        historyid: this.hid
      }
      let p = await dataqueryapi.getHistoryDetail(obj)
      if(p.code === 20000) {
        this.tableData = p.data
        console.log("p.data: ", p.data);
        let i = 1
        this.tableData.forEach(item => { 
          item.index = i
          i++
        });
      } else {
        console.log('请求失败')
      }
      // console.log(p)
    },
    select() {

    },
    showdetail(id) {
      console.log("show detail", id)
    }
  }
};
</script>

<style scoped>
.table {
  width: 100%;
  height: 4em;
  font-size: 1.2em;
}
a {
  text-decoration: underline;
}
</style>
