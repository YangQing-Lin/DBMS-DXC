<template>
    <div class="detail">
      <el-dialog title="选择导出字段" :visible.sync="dialogTableVisible" width="60%">
        <el-table ref="multipleTable" :data="tableData" height="250" border style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column prop="index" label="序号" width="50">
          </el-table-column>
          <el-table-column prop="columnComment" label="中文名" width="180">
          </el-table-column>
          <el-table-column prop="columnName" label="英文名" width="180"> </el-table-column>
          <el-table-column prop="dataType" label="类型" width="180"> </el-table-column>
          <el-table-column prop="maxLength" label="长度"> </el-table-column>
          <el-table-column type="selection">
          </el-table-column>
        </el-table>
        <el-button class="btn" type="primary" @click="confirm()">确定</el-button>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import dataqueryapi from '@/api/dataquery'
import settings from '@/settings';
  export default {
    props: {
    },
    data() {
      return {
        tableData: [],
        multipleSelection: [],
        dialogTableVisible: false,
        id: '',
        name: ''
      };
    },
    created: function() {
      setTimeout(() => {
        this.init()
      }, 200);
    },
    methods: {
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
      },
      handleSelectionChange(val) {
          this.multipleSelection = val;
      },
      confirm() {
        this.dialogTableVisible = false
      },
      getSelected() {
        console.log("tableData: ", this.tableData)
        let array = []
        this.multipleSelection.forEach(item => {
          array.push(item.columnName)
        });
        if (array.length === 0) {
            this.tableData.forEach(item => {
                array.push(item.columnName)
            })
        }
        return array
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
  .btn {
    margin-left: 47.5%;
    margin-top: 3em;
  }
  </style>
  