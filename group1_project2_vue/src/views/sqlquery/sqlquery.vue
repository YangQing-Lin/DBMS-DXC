<template>
  <el-container>
    <exportfile ref="exportfile"></exportfile>
    <el-header>
      <div class="sqlfind">
        <el-form :inline="true">
          <el-form-item label="数据库类型">
            <el-select
              v-model="selectData.com1"
              placeholder="请选择"
              clearable
              @change="change1"
            >
              <el-option
                v-for="x in optionData.com1"
                :key="x.value"
                :value="x.value"
                :label="x.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="系统名称">
            <el-select
              v-model="selectData.com2"
              placeholder="请选择"
              clearable
              @change="change2"
            >
              <el-option
                v-for="x in optionData.com2[selectData.com1]"
                :key="x.value"
                :value="x.value"
                :label="x.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="数据库名">
            <el-select
              v-model="selectData.com3"
              placeholder="请选择"
              clearable
              @change="change3"
            >
              <el-option
                v-for="x in optionData.com3"
                :key="x.value"
                :value="x.value"
                :label="x.value"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-dropdown split-button type="primary" @click="handleClick">
            <i class="el-icon-caret-right"></i>
            运行
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button type="text" @click="handleClick"
                  >运行所有语句</el-button
                >
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button type="text" @click="handleClick1"
                  >运行当前语句</el-button
                >
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-form>
      </div>
    </el-header>
    <el-container>
      <el-aside width="80%">
        <div>
          <textarea
            class="sqlwrite"
            style="resize: none"
            name=""
            id=""
            cols="30"
            rows="10"
            v-model="notedata"
            @select="testselect"
          ></textarea>
        </div>
        <div class="forbutton">
          <div v-if="!show">
            <el-button class="leftbutton" type="info" plain disabled
              >请先查询</el-button
            >
          </div>
          <div class="sc" v-else>
            <div class="ss">
              <div
                class="mybutton"
                v-for="item in Object.keys(tableData1.data)"
                :key="item"
              >
                <el-button
                  class="maxwidth"
                  type="info"
                  @click="writeinfo(item)"
                  plain
                  >{{ item }}</el-button
                >
              </div>
            </div>

            <el-dropdown
              split-button
              type="primary"
              class="rightbutton"
              @click="
                downinfo('xlsx', Object.keys(tableData1.data)[i], '#legacy')
              "
            >
              导出数据
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="
                      downinfo(
                        'xlsx',
                        Object.keys(tableData1.data)[i],
                        '#legacy'
                      )
                    "
                    >导出xlsx数据</el-button
                  >
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="
                      downinfo(
                        'xls',
                        Object.keys(tableData1.data)[i],
                        '#legacy'
                      )
                    "
                    >导出xls数据</el-button
                  >
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="
                      downinfo(
                        'txt',
                        Object.keys(tableData1.data)[i],
                        '#legacy'
                      )
                    "
                    >导出txt数据</el-button
                  >
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
        <div class="selectshow">
          <el-table
            :data="tableData[this.i]"
            height="193"
            id="legacy"
            border
            :show-overflow-tooltip="true"
            style="width: 100%"
          >
            <div v-if="show">
              <div v-for="item in Object.keys(tableData[i][0])" :key="item">
                <el-table-column
                  :show-overflow-tooltip="true"
                  :prop="item"
                  :label="item"
                  width="120"
                >
                </el-table-column>
              </div>
            </div>
            <div v-if="!show"></div>
          </el-table>
        </div>
      </el-aside>
      <el-main>
        <div class="sqlshow">
          <div v-if="optionData.com4.data">
            <div v-for="item in tablelist.length" :key="item">
              <el-button @click="getinfo(tablelist[item-1])" type="text">{{tablelist[item-1]}}</el-button>
            </div>
          </div>
           
          
          
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import dbsourceapi from "@/api/dbsource";
import sqlqueryeapi from "@/api/sqlquery";

import exportfile from "@/components/exportfile/exportfile.vue";
export default {
  components: { exportfile },
  data() {
    return {
      i: 0,
      show: 0,
      id: '',
      tablelist: [],
      listname: [],
      tableData: [],
      selectData: {
        com1: "",
        com2: "",
        com3: "",
        com4: "",
      },
      defaultProps: {
        children: "children",
        label: "value",
      },
      optionData: {
        com1: [
          { value: "mysql" },
          { value: "oracle" },
          { value: "sqlserver" },
          { value: "postgresql" },
        ],
        com2: {
          mysql: [],
          oracle: [],
          sqlserver: [],
          postgresql: [],
        },
        com3: [],
        com4: [],
      },
      notedata: "",
      txt: "",
      tableData1: [],
    };
  },
  created: function () {
    this.getall();
    console.log(this.$store.state.dbsource.dbsource);
  },

  methods: {
    async getinfo(e) {
      let id = 1;
      let list = this.$store.state.dbsource.dbsource;
      list.forEach((item) => {
        if (
          item.dbtype === this.selectData.com1 &&
          item.sysname === this.selectData.com2 &&
          item.dbname === this.selectData.com3 
        ){
          console.log(item.dbtype, item.sysname, item.dbname);
          id=item.id 
        }
      });
      let note='select * from ' + e +';';
      let obj = { id: id, note: note };
      sqlqueryeapi.usesql(obj);
      this.tableData1 = await sqlqueryeapi.usesql(obj);

      let i = 0;
      for (let key in this.tableData1.data) {
        this.tableData[i] = this.tableData1.data[key];
        i++;
      }
      this.show = 1;
      console.log(e)
    },

    async handleClick() {
      let id = 0;
      let list = this.$store.state.dbsource.dbsource;
      list.forEach((item) => {
        if (
          item.dbtype === this.selectData.com1 &&
          item.sysname === this.selectData.com2 &&
          item.dbname === this.selectData.com3 
        ){
          console.log(item.dbtype, item.sysname, item.dbname);
          id=item.id 
        }
      });
      let obj = { id: id, note: this.notedata };
      if (!obj.note || obj.note.length === 0) {
        return ;
      }
      sqlqueryeapi.usesql(obj);
      this.tableData1 = await sqlqueryeapi.usesql(obj);

      let i = 0;
      for (let key in this.tableData1.data) {
        this.tableData[i] = this.tableData1.data[key];
        i++;
      }

      // console.log(this.tableData1.data);
      // let arr = Object.entries(this.tableData1.data)
      // let last = arr.pop()
      // console.log(last)
      // arr.unshift(last)
      // console.log(arr)
      // this.tableData1.data = Object.fromEntries(arr)
      // console.log(this.tableData1.data)
      
      // console.log(this.tableData)
      // last = this.tableData.pop()
      // this.tableData.unshift(last)

      this.show = 1;
      // this.tableData.reverse()
      // console.log(this.tableData);
    },

    async handleClick1() {
      let id = 0;
      let list = this.$store.state.dbsource.dbsource;
      list.forEach((item) => {
        if (
          item.dbtype === this.selectData.com1 &&
          item.sysname === this.selectData.com2 &&
          item.dbname === this.selectData.com3 
        ){
          console.log(item.dbtype, item.sysname, item.dbname);
          id=item.id 
        }
      });
      let obj = { id: id, note: this.txt };
      sqlqueryeapi.usesql(obj);
      this.tableData1 = await sqlqueryeapi.usesql(obj);

      let i = 0;
      for (let key in this.tableData1.data) {
        this.tableData[i] = this.tableData1.data[key];
        i++;
      }
      this.show = 1;
    },

    change1: function (val) {
      this.selectData.com2='';
      this.selectData.com3='';
      this.selectData.com4='';
      if(!val)
      {
        this.selectData.com2 = ""; 
        this.change2();
      }
      
      let list = this.$store.state.dbsource.dbsource;
      list.forEach((item) => {
        if(this.selectData.com1===''){
          return 
        }
        if (
          item.dbtype === this.selectData.com1 &&
          this.optionData.com2[this.selectData.com1].length === 0
        ) {
          let newlist = { value: item.sysname };
          this.optionData.com2[this.selectData.com1].push(newlist);
        } else if (
          item.dbtype === this.selectData.com1 &&
          this.optionData.com2[this.selectData.com1].length > 0
        ) {
          let newlist = { value: item.sysname };
          let count = 0;
          for (
            let i = 0;
            i < this.optionData.com2[this.selectData.com1].length;
            i++
          ) {
            if (
              this.optionData.com2[this.selectData.com1][i].value !==
              item.sysname
            ) {
              count++;
            }
            if (count === this.optionData.com2[this.selectData.com1].length) {
              this.optionData.com2[this.selectData.com1].push(newlist);
            }
          }
        }
      });
      if (val) {
        this.selectData.com2 = this.optionData.com2[val][0].value; 
        this.change2();
      } else {
        this.selectData.com2 = ""; 
        this.change2();
      }
      console.log(val)
    },

    change2: function () {
      let val = this.selectData.com2;
      this.optionData.com3 = [];
      let list = this.$store.state.dbsource.dbsource;
      list.forEach((item) => {
        let newlist = { value: item.dbname };
        if (
          item.dbtype === this.selectData.com1 &&
          item.sysname === this.selectData.com2 &&
          this.optionData.com3.length === 0
        ) {
          this.optionData.com3.push(newlist);
        } else if (
          item.dbtype === this.selectData.com1 &&
          item.sysname === this.selectData.com2 &&
          this.optionData.com3.length > 0
        ) {
          let count = 0;
          for (let i = 0; i < this.optionData.com3.length; i++) {
            if (this.optionData.com3[i].value !== item.dbname) {
              count++;
            }
            if (count === this.optionData.com3.length) {
              this.optionData.com3.push(newlist);
            }
          }
        }
      });
      if (val) {
        this.selectData.com3 = this.optionData.com3[0].value;
        this.change3();
      } else {
        this.selectData.com3 = "";
        this.change3();
      }
      
    },

    async change3() {
      let val = this.selectData.com3;
      this.optionData.com4 = [];
      this.tablelist=[]
      // this.id = null;
      let list = this.$store.state.dbsource.dbsource;
      list.forEach((item) => {
        if (
          item.dbtype === this.selectData.com1 &&
          item.sysname === this.selectData.com2 &&
          item.dbname === this.selectData.com3 
        ){
          console.log(item.dbtype, item.sysname, item.dbname);
          this.id=item.id 
        }
      });
      this.optionData.com4 = await sqlqueryeapi.gettabel(this.id);
      for(let i=0;i<this.optionData.com4.data.length;i++){
        this.tablelist.push(this.optionData.com4.data[i].tableName)
      }
      console.log(this.tablelist)
      
      if (!val) {
        this.optionData.com4 = "";
      }
    },

    async getall() {
      this.$store.commit("dbsource/CLEAR_DBSOURCE");
      let p = dbsourceapi.getdball();
      p.then((response) => {
        this.$store.dispatch("dbsource/setdbsource", response.data);
      });
      this.getresult = await p;
    },

    testselect(e) {
      let start = e.target.selectionStart;
      let end = e.target.selectionEnd;
      let value = e.target.value;

      let selectVal = "";
      if (value && end != 0) {
        //防止 没选中
        selectVal = value.slice(start, end);
      }
      console.log(selectVal);
      this.txt = selectVal;
    },

    writeinfo(item) {
      for (let i = 0; i < Object.keys(this.tableData1.data).length; i++) {
        if (item === Object.keys(this.tableData1.data)[i]) {
          this.i = i;
        }
      }

      this.tabletitle = Object.keys(this.tableData[this.i][0]);
      console.log(this.tabletitle);
    },

    downinfo(type, filename, id) {
      this.$refs.exportfile.downinfo(type, filename, id);
    },
  },
};
</script>

<style scoped>
.sc {
  float: left;
  width: 100%;
}

.ss {
  float: left;
  overflow-x: auto;
  overflow: hidden;
  width: 70%;
  overflow-x: scroll; /*添加横向滚动条*/
  white-space: nowrap; /*不换行*/
}
.forbutton {
  width: 100%;
  display: inline-block;
  height: 30px;
  margin: 10px 0;
}
.mybutton {
  display: inline-block;
  overflow-x: auto;
  overflow: hidden;
  flex-shrink: 0;
  margin-left: 10px;
}
.maxwidth {
  float: left;
  max-width: 200px;
  font-size: 10px;
}
.leftbutton {
  float: left;
  margin-left: 10px;
}
.rightbutton {
  float: right;
  margin-right: 30px;
}
.sqlfind {
  width: 90%;
}

.sqlfind label {
  margin: 0 10px;
  font-size: 13px;
}
.sqlfind .el-select {
  width: 120px;
  margin-top: 10px;
}

.sqlfind .el-button {
  margin-left: 10px;
}

.sqlwrite {
  width: 98%;
  height: 340px;
  margin-top: 10px;
  padding: 20px 20px;
  margin-left: 10px;
}

.selectshow {
  margin-left: 10px;
  margin-top: 8px;
  width: 98%;
  height: 195px;
  border: 1px solid #000;
}
.sqlshow {
  width: 99%;
  height: 600px;
  margin-top: 10px;
  border: 1px solid #000;
}
.el-tree {
  margin-top: 5px;
}
.el-dropdown {
  margin-left: 5px;
}

.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}

.el-main {
  color: #333;
  text-align: center;
  padding: 0;
}
</style>