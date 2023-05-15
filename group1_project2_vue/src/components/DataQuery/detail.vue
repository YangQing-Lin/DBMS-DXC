<template>
  <div class="detail">
    <el-dialog title="详情页面" :visible.sync="dialogTableVisible" width="60%" @close='closeDialog'>
      <table class="table">
        <tr>
          <td>表/视图名：{{ tablename }}</td>
          <td>数据量（万）：{{ datacount }}</td>
        </tr>
        <tr>
          <td>字段总数：{{ desgincount }}</td>
          <td>最近采集日期：{{ date }}</td>
        </tr>
      </table>
      <el-table :data="tableData" height="250" border style="width: 100%">
        <el-table-column prop="index" label="序号" width="80">
        </el-table-column>
        <el-table-column prop="columnComment" label="中文名" width="180">
        </el-table-column>
        <el-table-column prop="columnName" label="英文名" width="180"> </el-table-column>
        <el-table-column prop="dataType" label="类型" width="180"> </el-table-column>
        <el-table-column prop="maxLength" label="长度"> </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import dataqueryapi from '@/api/dataquery'
export default {
  props: {
    id: Number,
    name: String
  },
  data() {
    return {
      tableData: [],
      tablename: 'code_gb_city',
      datacount: '0.3181',
      desgincount: '6',
      date: '2020-0-?',
      dialogTableVisible: true,
    };
  },
  created: function() {
    this.init()
  },
  methods: {
    closeDialog(){
      this.$parent.isopen1 = false;//清空数据
    },
    async init() {
      let obj = {
        tablename: this.name,
        linkid: this.id
      }
      let p = await dataqueryapi.getTabelInfo(obj)
      if(p.code === 20000) {
        this.datacount = p.data.dataCount * 0.0001
        this.desgincount = p.data.columnCount
        this.tablename = p .data.tableName
        this.tableData = p.data.columnList
        let i = 1
        this.tableData.forEach(item => {
          item.index = i
          i++
        });
      } else {
        console.log('请求失败')
      }
      // console.log(p)
      p = await dataqueryapi.getHistory(obj)
      if(p.code === 20000) {
        p.data.forEach(item => {
          item.gatherTime = item.gatherTime.replace('T',' ')
            if(this.date < item.gatherTime){
              this.date = item.gatherTime
            }
        });
      }
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
</style>
