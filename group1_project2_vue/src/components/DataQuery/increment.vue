<template>
  <div class="detail">
    <el-dialog title="增量" :visible.sync="dialogTableVisible" width="50%" @close='closeDialog'>
      <table class="table">
        <tr>
          <td>
            <div class="p">增量字段：</div>
            <el-select v-model="tableName" placeholder="请选择" style="width: 73.5%;">
              <el-option
                v-for="item in field"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </td>
          <td>
            <div class="p">导出类型：</div>
            <el-select v-model="selectType" placeholder="请选择" style="width: 73.5%;">
              <el-option
                v-for="item in type"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </td>
        </tr>
        <tr>
          <td class="half">
            <div class="p">开始值：</div>
            <input v-model="start" style="width: 73.5%;">
          </td>
          <td class="half">
            <div class="p">结束值：</div>
            <input v-model="end" style="width: 73.5%;">
          </td>
        </tr>
        <tr>
          <td colspan="2" class="half">
            <div class="p">导出路径：</div>
            <input v-model="exportPath" style="width: 73%;" @click="selectPath()">
            <el-button type="primary" @click="select()">选择导出字段</el-button>
          </td>
        </tr>
        <tr>
          <td colspan="2" class="half">
            <div class="p">过滤条件：</div>
            <input type="textarea" :rows="2" v-model="filter" style="width: 87%;">
          </td>
        </tr>
        <tr>
          <td colspan="2" class="export">
            <el-button type="primary" @click="exportFile()">导出</el-button>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <el-divider>导出详情</el-divider>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <div class="p">进度</div>
            <el-progress :percentage="process"></el-progress>
          </td>
        </tr>
        <tr>
          <td colspan="2" class="half">
            <div class="p">导出日志：</div>
            <el-card class="box-card">
              <div v-for="item in cards" :key="item.index" class="text item">
                {{ item.index + ':  ' + item.value }}
              </div>
            </el-card>
          </td>
        </tr>
      </table>
    </el-dialog>
    <select-field ref="selectfield" :id="1" name="customers"></select-field>
    <!-- <input type="file" name="filename" id="open" style="display: none;" @change="changeFile" webkitdirectory> -->
    <exportfile ref="exportfile"></exportfile>
    <el-table :data="result" height="193" id="inc" border :show-overflow-tooltip="true" style="width: 100%" v-show="false">
      <div v-for="item in Object.keys(result[0])" :key="item">
        <el-table-column :show-overflow-tooltip="true" :prop="item" :label="item" width="120">
        </el-table-column>
      </div>
    </el-table>
  </div>
</template>

<script>
import dataqueryapi from "@/api/dataquery";
import selectField from './selectField.vue';
import Exportfile from '../exportfile/exportfile.vue';
export default {
  components: { selectField, Exportfile },
  props: {
    id: Number,
    name: String,
  },
  data() {
    return {
      field: [],
      type: [
        {value:'txt',label:'txt'},
        // {value:'binary',label:'binary'},
        {value:'xlsx',label:'xlsx'},
        {value:'xls',label:'xls'},
      ],
      tableName: '',
      selectType: '',
      start: '10001',
      end: '10003',
      exportPath: 'test',
      filter: 'cust_name="John"',
      process: 0,
      dialogTableVisible: true,
      dataCount: 0,
      data: [],
      selectFields: [],
      result: [{'null':'null'}],
      cards: []
    };
  },
  created: function () {
    this.init();
  },
  methods: {
    closeDialog(){
      this.$parent.isopen2 = false;//清空数据
    },
    async init() {
      let obj = {
        tablename: this.name,
        linkid: this.id,
      };
      setTimeout(() => {
        this.$refs.selectfield.id = this.id
        this.$refs.selectfield.name = this.name
      }, 100);
      let p = await dataqueryapi.getTabelInfo(obj);
      if (p.code === 20000) {
        p.data.columnList.forEach(item => {
          this.field.push({
            'value': item.columnName,
            'label': item.columnComment
          })
        });
      } else {
        this.$message({
          message: '信息获取失败，请重试',
          type: 'warning'
        });
      }
      // console.log(p);
    },
    async exportFile() {
      this.cards = []
      this.cardPush1()
      this.selectFields = this.$refs.selectfield.getSelected()
      if(this.exportPath === '') {
        this.exportFile = 'test'
      }
      let obj = {
        'path': 'dbLinkId=' + this.id + '&tableName=' + this.name + '&columnName=' + 'cust_id' + '&gatherState=' + 1 + '&exportType=' + this.selectType + '&exportPath=' + this.exportPath + '&gatherType=' + 0 + '&filterCriteria=' + this.filter + '&start=' + this.start + '&end=' + this.end,
        'body': {
          "columnNames": this.selectFields
        }
      }
      console.log(obj)
      let p = await dataqueryapi.getExportInfo(obj)
      console.log(p)
      if(p.code === 20000) {
        console.log(typeof(this.selectFields.length))
        if(this.selectFields.length !== 0) {
          this.cardPush2(true, this.selectFields.length + '个')
        } else {
          this.cardPush2(true, '全部')
        }
        setTimeout(() => {
          this.result = p.data.requestData
          this.cardPush3(true, p.data.requestData.length)
        }, 3000);
        setTimeout(() => {
          this.$refs.exportfile.downinfo(this.selectType,this.name + '表增量导出', '#inc')
          if(this.selectFields.length !== 0) {
            this.cardPush4(true, this.selectFields.length + '个',  p.data.requestData.length)
          } else {
            this.cardPush4(true, '全部',  p.data.requestData.length)
          }
        }, 6000);
        // console.log(p)
      } else {
        this.cardPush2(false, 0)
        this.$message({
          message: '信息获取失败，请重试',
          type: 'warning'
        });
      }
      // console.log(
      //   '表字段：',
      //   this.tableName,
      //   this.selectType,
      //   this.start,
      //   this.end,
      //   this.exportPath,
      //   this.selectFields,
      //   this.filter,
      //   '导出返回内容：',
      //   this.data,
      // )
    },
    select() {
      this.$refs.selectfield.dialogTableVisible = true
    },
    selectPath() {
      // document.getElementById('open').click()
      // let path = document.getElementById('open').then(()=>{
      //   console.log(path)
      //   if(path == null) return
      //   this.exportPath = path.files[0].name
      // })
    },
    cardPush1() {
      this.process = 5
      this.cards.push({
        'index': 1,
        'value': '数据导出准备中...'
      })
    },
    cardPush2(flag, count) {
      let date = new Date().toLocaleDateString().split('/').join('-')
      if(flag) {
        this.process = 35
        this.cards.push({
          'index': 2,
          'value': '获取表' + this.name + '中的数据，字段总数' + count + '，导出完成时间' + date
        })
      } else {
        this.process = 100
        this.cards.push({
          'index': 2,
          'value': '获取表' + this.name + '中的数据失败...'
        })
      }
    },
    cardPush3(flag, count) {
      if(flag) {
        this.process = 65
        this.cards.push({
          'index': 3,
          'value': '生成表' + this.name + '数据包，已生成' + count + '条数据包，数据导出中'
        })
      } else {
        this.process = 100
        this.cards.push({
          'index': 3,
          'value': '生成表' + this.name + '中的数据失败...'
        })
      }
    },
    cardPush4(flag, count1, count2) {
      if(flag) {
        this.process = 100
        this.cards.push({
          'index': 4,
          'value': '导出表' + this.name + '中的数据完成，字段总数' + count1 + '；导出'+ count2 +'条数据，导出完成'
        })
      } else {
        this.process = 100
        this.cards.push({
          'index': 4,
          'value': '导出表' + this.name + '中的数据失败...'
        })
      }
    },
  },
};
</script>

<style scoped>
.table {
  width: 100%;
  font-size: 1.2em;
}
.table input {
  padding: 0.8em;
  font-size: 14px;
  border-radius: 4px;
  border: 1px solid rgb(235, 235, 235);
  outline: none;
}
.table .half {
  align-items: center;
}
.table .p {
  display: inline-block;
  width: 90px;
}
.table .export button{
  margin-left: 45.5%;
}
</style>
