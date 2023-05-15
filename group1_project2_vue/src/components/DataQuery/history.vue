<template>
  <div class="detail">
    <el-dialog title="历史记录" :visible.sync="dialogTableVisible" width="60%" @close='closeDialog'>
      <table class="table">
        <tr>
          <td>采集字段（个）：{{ field }}</td>
          <td>采集数据总量（万）：{{ count.toFixed(4) }}</td>
          <td>最近采集日期：{{ date }}</td>
        </tr>
        <tr>
          <td>采集日期：
            <el-date-picker
              value-format="yyyy-MM-dd"
              v-model="setdate"
              type="date"
              placeholder="选择日期时间">
            </el-date-picker>
          </td>
          <td>是否成功：
            <el-select v-model="selectSuccess" placeholder="请选择">
              <el-option
                v-for="item in success"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select> 
          </td>
          <td>
            <el-button type="primary" @click="select()">查询</el-button>
          </td>
        </tr>
      </table>
      <el-table :data="selectedData" height="250" border style="width: 100%">
        <el-table-column prop="index" label="序号" width="80">
        </el-table-column>
        <el-table-column prop="tableComment" label="表/视图中文名" width="180">
        </el-table-column>
        <el-table-column prop="tableName" label="表/视图英文名" width="180"> </el-table-column>
        <el-table-column prop="fieldCount" label="字段总数"> </el-table-column>
        <el-table-column prop="dataCount" label="数据量（万）" width="110"> </el-table-column>
        <el-table-column prop="gatherTime" label="采集日期" width="180"> </el-table-column>
        <el-table-column prop="gatherState" label="状态"> </el-table-column>
        <el-table-column prop="gatherType" label="方式"> </el-table-column>
        <el-table-column prop="detail" label="详情">
          <template slot-scope="scope">
            <a href="#" @click="showdetail(scope.row.index - 1)">
              详情
          </a>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <historydetail ref="historydetail" :hid="hid" :detail="hdobj"></historydetail>
  </div>
</template>

<script>
import dataqueryapi from '@/api/dataquery'
import historydetail from './historydetail.vue';
import sourcetableVue from '../dbsource/sourcetable.vue';
export default {
  components: { historydetail },
  props: {
    id: Number,
    name: String
  },
  data() {
    return {
      selectedData: [],
      tableData: [],
      success: [{value:'成功', label:'成功'}, {value:'失败', label:'失败'}],
      selectSuccess: '',
      field: 0,
      count: 0,
      desgincount: '6',
      setdate: '',
      date: '1999-01-01 00:00:00',
      hid: 1,
      hdobj: {
        type: '',
        path: '',
        filter: ''
      },
      dialogTableVisible: true,
    };
  },
  created: function() {
    this.init()
  },
  methods: {
    closeDialog(){
      this.$parent.isopen5 = false;//清空数据
    },
    async init() {
      let obj = {
        tablename: this.name,
        linkid: this.id
      }
      console.log("hsitory id info", this.name, this.id);
      let p = await dataqueryapi.getHistoryByLinkId(this.id, this.name);
      console.log(p.data)
      if(p.code === 20000) {
        this.tableData = p.data
        if (p.data.historyList !== null) {
            this.tableData = p.data.historyList
        }
        let i = 1
        console.log("table data: ", this.tableData)
        this.tableData.forEach(item => { 
          item.dataCount = item.dataCount*0.0001
          this.count += parseFloat(item.dataCount)
          item.gatherTime = item.gatherTime.replace('T',' ')
          if(this.date < item.gatherTime){
            this.date = item.gatherTime
          }
          item.gatherType = item.gatherType === 1 ? '全量' : '增量'
          item.gatherState = item.gatherState === 1 ? '成功' : '失败'
          item.index = i
          i++
        });
        this.field = this.tableData[this.tableData.length - 1].index
        this.selectedData = this.tableData
      } else {
        console.log('请求失败')
      }
      // console.log(p)
    },
    select() {
      this.selectedData = []
      if(this.selectSuccess === null){
        this.selectSuccess = ''
      }
      if(this.setdate === null){
        this.setdate = ''
      }
      console.log(this.setdate)
      console.log(this.selectSuccess)
      this.tableData.forEach(item => {
        if(this.selectSuccess !== '' && this.setdate !== '') {
          if(item.gatherTime.includes(this.setdate) && item.gatherState === this.selectSuccess) {
            this.selectedData.push(item)
          }
        } else if(this.setdate !== '') {
          if(item.gatherTime.includes(this.setdate)) {
            this.selectedData.push(item)
          }
        } else if(this.selectSuccess !== '') {
          if(item.gatherState === this.selectSuccess) {
            this.selectedData.push(item)
          }
        } else {
          this.selectedData = this.tableData
        }
      });
    },
    showdetail(index) {
    console.log("in history show detail: ", index);
      this.hid = this.selectedData[index].historyId
      console.log("history id: ", this.hid);
      this.hdobj = {
        type: this.selectedData[index].exportType,
        path: this.selectedData[index].exportPath,
        filter: this.selectedData[index].filterCriteria
      }
      console.log("this.hdobj: ", this.hdobj)
      this.$refs.historydetail.dialogTableVisible = true
      this.$refs.historydetail.hid = this.hid
      this.$refs.historydetail.init()
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
