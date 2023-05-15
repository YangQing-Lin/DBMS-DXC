<template>
  <div class="test" style="margin-top: 100px">
    <el-tree
      class="tree"
      :data="treeList"
      :props="defaultProps"
      default-expand-all
      @node-click="handleNodeClick"
    ></el-tree>
    <!-- id为查询的linkid，name为查询表 -->
    <detail ref="detail" :id="1" name="customers"></detail>
    <increment ref="increment" :id="1" name="customers"></increment>
    <select-field ref="selectfield" :id="1" name="customers"></select-field>
    <fullload ref="fullload" :id="1" name="customers"></fullload>
    <history ref="history" :id="1" name="customers"></history>
    <historydetail ref="historydetail" :hid="1" :detail="hdobj"></historydetail>
    <el-button type="primary" @click="detail()">详细</el-button>
    <el-button type="primary" @click="increment()">增量</el-button>
    <el-button type="primary" @click="infull()">全量</el-button>
    <el-button type="primary" @click="choose()">选择导出</el-button>
    <el-button type="primary" @click="history()">历史记录</el-button>
    <el-button type="primary" @click="historydetail()">历史记录详情</el-button>
  </div>
</template>

<script>
import dbsourceapi from '@/api/dbsource'
import detail from '@/components/DataQuery/detail.vue'
import Increment from '@/components/DataQuery/increment.vue'
import SelectField from '@/components/DataQuery/selectField.vue'
import Fullload from '@/components/DataQuery/fullload.vue'
import History from '@/components/DataQuery/history.vue'
import Historydetail from '@/components/DataQuery/historydetail.vue'
export default {
  components: { detail, Increment, SelectField, Fullload, History, Historydetail },
  data() {
    return {
      hdobj: {
        type: 'txt',
        path: 'D:/',
        filter: 'test',
      },
      treeList: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      getresult: '',
      rawdata: [],
    }
  },
  mounted() {
    this.buildTree()
  },
  methods: {
    async buildTree() {
      this.rawdata = []
      let p = dbsourceapi.getdball()
      p.then(response => {
        this.rawdata = response.data
        this.treeList = [{
          label: '数据库',
          children: [{
            label: 'MySql',
            children: [],
          },{
            label: 'Oracle',
            children: [],
          },{
            label: 'SqlServer',
            children: [],
          },{
            label: 'PostgreSql',
            children: [],
          }]
        }]
        this.rawdata.forEach(item => {
          console.log(item)
          if(item.linkDbType === 'mysql'){
            this.treeList[0].children[0].children.push({label: item.linkDbName})
          } else if(item.linkDbType === 'oracle'){
            this.treeList[0].children[1].children.push({label: item.linkDbName})
          } else if(item.linkDbType === 'sqlserver'){
            this.treeList[0].children[2].children.push({label: item.linkDbName})
          } else if(item.linkDbType === 'postgresql'){
            this.treeList[0].children[3].children.push({label: item.linkDbName})
          }
        });
      })
    },
    detail() {
      this.$refs.detail.dialogTableVisible = true
    },
    increment() {
      this.$refs.increment.dialogTableVisible = true
    },
    infull() {
      this.$refs.fullload.dialogTableVisible = true
    },
    choose() {
      this.$refs.selectfield.dialogTableVisible = true
    },
    history() {
      this.$refs.history.dialogTableVisible = true
    },
    historydetail() {
      this.$refs.historydetail.dialogTableVisible = true
    },
    handleNodeClick(data) {
      console.log(data);
    }
  }
}
</script>

<style scoped>
.tree{
  width: 200px;
  margin: 4px;
  border: 1px solid black;
}
</style>
