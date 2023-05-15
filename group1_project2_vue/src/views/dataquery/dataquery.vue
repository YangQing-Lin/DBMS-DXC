<template>
  <div>
    <detail v-if="isopen1" ref="detail" :id="id" :name="tname"></detail>
    <increment
      v-if="isopen2"
      ref="increment"
      :id="id"
      :name="tname"
    ></increment>
    <select-field
      v-if="isopen3"
      ref="selectfield"
      :id="id"
      :name="tname"
    ></select-field>
    <fullload v-if="isopen4" ref="fullload" :id="id" :name="tname"></fullload>
    <history v-if="isopen5" ref="history" :id="id" :name="tname"></history>
    <historydetail
      v-if="isopen6"
      ref="historydetail"
      :hid="id"
      :detail="hdobj"
    ></historydetail>

    <el-col :span="4">
      <el-input
        placeholder="输入关键字进行过滤"
        v-model="filterText"
      ></el-input>
      <el-tree
        :data="treeData"
        node-key="id"
        :props="defaultProps"
        :filter-node-method="filterNode"
        default-expand-all
        @node-click="handleNodeClick"
        ref="tree"
      >
      </el-tree>
    </el-col>

    <el-col :span="20">
      <div v-if="selectedTable">
        <div class="top">
          <el-col :span="8">
            <el-col :span="8">
              <label>数据库名</label>
            </el-col>
            <el-col :span="8">
              <label>{{ selectedTable }}</label>
            </el-col>

          </el-col>
          <el-col :span="8">
            <el-col :span="6">
              <label>数据表共</label>
            </el-col>
            <el-col :span="6">
              <label >{{ tables.length }}</label>
            </el-col>

          </el-col>
          <el-col :span="8">
            <el-col :span="6">
              <label >视图共</label>
            </el-col>
            <el-col :span="6">
              <label > 0 </label>
            </el-col>

          </el-col>
        </div>
        <br>
        <br>
        <el-divider>查询</el-divider>
        
       <div class="search">
        <el-col :span="8">
          <el-col :span="8">
            <label class="fontsize">表/视图中文名</label>
          </el-col>
          <el-col :span="14">
            <el-input v-model="cname" placeholder="请输入内容"></el-input>
          </el-col>
        </el-col>
        <el-col :span="8">
          <el-col :span="8">
            <label class="fontsize">表/视图英文名</label>
          </el-col>
          <el-col :span="14">
            <el-input v-model="yname" placeholder="请输入内容"></el-input>
          </el-col>
        </el-col>
        <el-col :span="8">
            <el-button class="buttonleft" type="primary" @click="search()">查询</el-button>
            <el-button @click="reset()">重置</el-button>
          
        </el-col>

       </div>

       <br>
        <el-table class="table" :data="tables" :key="selectedTable">
          <el-table-column
            :key="'tableComment'"
            prop="tableComment"
            label="表/视图中文名"
          ></el-table-column>
          <el-table-column
            :key="'tableName'"
            prop="tableName"
            label="表/视图英文名"
          ></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <div class="test" style="margin-top: 3px">
                <!-- id为查询的linkid，name为查询表 -->
                <el-button type="primary" @click="detail(scope.row.tableName)"
                  >详细</el-button
                >
                <el-button
                  type="primary"
                  @click="increment(scope.row.tableName)"
                  >增量</el-button
                >
                <el-button type="primary" @click="infull(scope.row.tableName)"
                  >全量</el-button
                >
                <el-button type="primary" @click="history(scope.row.tableName)"
                  >历史记录</el-button
                >
              </div>
            </template>
          </el-table-column>
        </el-table>

        <!-- <div class="resource-page">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[5, 9, 15]"
        :page-size="pagesize"
        :pager-count="5"
        layout=" prev, pager, next,sizes, jumper"
        :total="total"
      >
      </el-pagination>
    </div> -->
      </div>
    </el-col>
  </div>
</template>
  
<script>
import axios from "axios";
import dbsourceapi from "@/api/dbsource";

import sqlqueryeapi from "@/api/sqlquery";
import dataqueryapi from "@/api/dataquery";

import detail from "@/components/DataQuery/detail.vue";
import Increment from "@/components/DataQuery/increment.vue";
import SelectField from "@/components/DataQuery/selectField.vue";
import Fullload from "@/components/DataQuery/fullload.vue";
import History from "@/components/DataQuery/history.vue";
import Historydetail from "@/components/DataQuery/historydetail.vue";


export default {
  components: {
    detail,
    Increment,
    SelectField,
    Fullload,
    History,
    Historydetail,
  },
  data() {
    return {
      filterText: "",
      id: "",
      tname: "",
      cname:'',
      yname:'',
      isopen1: false,
      isopen2: false,
      isopen3: false,
      isopen4: false,
      isopen5: false,
      isopen6: false,
      filterDataShow: [], //储存符合条件的数据
     
      total: 0,
      loading: true,
      reload: 0,
      multipleSelection: [],

      show: 0,
      tablelist: [],
      list: [],
      treeData: [], // 树形结构数据
      defaultProps: {
        // 配置节点属性
        label: "tableName",
        children: "children",
      },
      selectedTable: "", // 当前选中的表名
      tables: [], // 当前选中表的数据

      root: {
        tableName: "数据库",
        children: [
          { tableName: "mysql", children: [] },
          { tableName: "oracle", children: [] },
          { tableName: "sqlserver", children: [] },
          { tableName: "postgresql", children: [] },
        ],
      },

      hdobj: {
        type: "txt",
        path: "D:/",
        filter: "test",
      },
    };
  },

  computed: {
    //showTable计算属性通过slice方法计算表格当前应显示的数据
    // showTable() {
    //   return this.filterDataShow.slice(
    //     (this.currentPage - 1) * this.pagesize,
    //     this.currentPage * this.pagesize
    //   );
    // },
  },

  created() {
    this.getall();
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
  },

  watch: {},

  mounted() {
    this.getdbname();
    // this.searchResource();
  },
  methods: {
    async getall() {
      this.$store.commit("dbsource/CLEAR_DBSOURCE");
      let p = dbsourceapi.getdball();
      p.then((response) => {
        this.$store.dispatch("dbsource/setdbsource", response.data);
      });
      this.getresult = await p;
      this.list = this.$store.state.dbsource.dbsource;
    },

    // async getTables() {
    //   try {
    //     const response = await axios.get(
    //       "http://localhost:8081/dblink/get_tables?dbLinkId=1"
    //     );
    //     if (response.status === 200) {
    //       const tables = response.data.data;
    //       console.log("tables:", tables);
    //       // 调试输出
    //       const root = {
    //         tableName: "数据库",
    //         children: [
    //           { tableName: "mysql", children: [] },
    //           { tableName: "Oracle", children: [] },
    //           { tableName: "SQL Server", children: [] },
    //           { tableName: "PostgreSQL", children: [] },
    //         ],
    //       };
    //       const mysqlNode = root.children.find(
    //         (node) => node.tableName === "mysql"
    //       );
    //       if (mysqlNode) {
    //         tables.forEach((table) => {
    //           const tableNode = { tableName: table.tableName };
    //           this.children.push(tableNode);
    //         });
    //         this.treeData.push(root);
    //         console.log(this.root.children[0].tableName);
    //       }
    //     } else {
    //       console.log(response.data.msg);
    //     }
    //   } catch (error) {
    //     console.error(error);
    //   }
    // },

    async getdbname() {
      //   let list = this.$store.state.dbsource.dbsource;
      //   const root = {
      //     tableName: "数据库",
      //     children: [
      //       { tableName: "mysql", children: [] },
      //       { tableName: "Oracle", children: [] },
      //       { tableName: "SQL Server", children: [] },
      //       { tableName: "PostgreSQL", children: [] },
      //     ],
      //   };

      //   const mysqlNode = root.children.find(
      //     (node) => node.tableName === "mysql"
      //   );

      setTimeout(() => {
        let a = 0;
        let b = 0;
        let c = 0;
        let d = 0;
        this.list.forEach((item) => {
          let newlist = { tableName: item.dbname, id: item.id };

          if (item.dbtype === "mysql" && a === 0) {
            this.root.children[0].children.push(newlist);
            a++;
          } else if (item.dbtype === "mysql" && a > 0) {
            let count = 0;
            for (let i = 0; i < a; i++) {
              if (this.root.children[0].children[i].tableName !== item.dbname) {
                count++;
              }
              if (count === a) {
                this.root.children[0].children.push(newlist);
                a++;
              }
            }
          }

          if (item.dbtype === "oracle" && b === 0) {
            this.root.children[1].children.push(newlist);
            b++;
          } else if (item.dbtype === "oracle" && b > 0) {
            let count = 0;
            for (let i = 0; i < b; i++) {
              if (this.root.children[1].children[i].tableName !== item.dbname) {
                count++;
              }
              if (count === b) {
                this.root.children[1].children.push(newlist);
                b++;
              }
            }
          }

          if (item.dbtype === "sqlserver" && c === 0) {
            this.root.children[2].children.push(newlist);
            c++;
          } else if (item.dbtype === "sqlserver" && c > 0) {
            let count = 0;
            for (let i = 0; i < c; i++) {
              if (this.root.children[2].children[i].tableName !== item.dbname) {
                count++;
              }
              if (count === c) {
                this.root.children[2].children.push(newlist);
                c++;
              }
            }
          }

          if (item.dbtype === "postgresql" && d === 0) {
            this.root.children[3].children.push(newlist);
            d++;
          } else if (item.dbtype === "postgresql" && d > 0) {
            let count = 0;
            for (let i = 0; i < d; i++) {
              if (this.root.children[3].children[i].tableName !== item.dbname) {
                count++;
              }
              if (count === d) {
                this.root.children[3].children.push(newlist);
                d++;
              }
            }
          }
        });
      }, 1000);

      this.treeData.push(this.root);
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },

    async handleNodeClick(data) {
      if (!data.id) {
        return;
      }
      this.isLoading = true;
      try {
        // const response = await axios.get(
        //   "http://localhost:8081/dblink/get_tables?dbLinkId=2"
        // );
        const response = await sqlqueryeapi.gettabel(data.id);
        console.log("re = ", response)
        if (response.code === 20000) {
          console.log(response.data);
          if (
            response.data &&
            response.data.length > 0
          ) {
            this.selectedTable = data.tableName;
            this.id = data.id;
            this.tablesFormatted = JSON.stringify(data.children);
            this.tables = response.data;
            console.log(`选中表格：${this.selectedTable}`);
          } else {
            console.log("无法获取表格数据");
          }
        } else {
          console.log(response.message);
        }
      } catch (error) {
        console.error(error);
      } finally {
        this.isLoading = false;
      }

      this.show = 1;
      // this.tablelist = await sqlqueryeapi.gettabel(this.id);
      // this.total = this.tablelist.data.length;
      // console.log(this.total);
    },

    async search() {
        let search = {'cname':this.cname,'yname':this.yname}
        console.log("search =",search)
        let a = await sqlqueryeapi.gettabel(this.id);
        if(this.cname || this.yname){
          let filerReasource = a.data.filter(item => { //过滤全部数据
            if (!item.tableComment) {
              item.tableComment = "";
            }
            if (item.tableComment.includes(this.cname) && item.tableName.includes(this.yname)) {
              return item
            }
          })  
          this.tables = filerReasource;
        } else{
          this.tables=a.data
        }  
      },
        

      
      async reset() {
        this.cname = ''
        this.yname = ''
        let a = await sqlqueryeapi.gettabel(this.id);
        this.tables=a.data
        
      },

    detail(name) {
      this.tname = name;
      this.isopen1 = true;
      // this.$refs.detail.dialogTableVisible = true;
    },
    increment(name) {
      this.tname = name;
      this.isopen2 = true;
    },
    infull(name) {
      this.tname = name;
      this.isopen4 = true;
    },
    choose() {
      this.$refs.selectfield.dialogTableVisible = true;
    },
    history(name) {
      this.tname = name;
      this.isopen5 = true;
    },
    historydetail() {
      this.$refs.historydetail.dialogTableVisible = true;
    },
  },
};
</script>
  
<style>
.test {
  width: 600px;
}

.columsize1 {
  width: 300px;
}

.top{
  margin: 10px 100px;
}

.search{
  margin: 10px 50px;
  height: 20px;
}

/* .top .el-col lable{
  height: 18px;
} */

.fontsize{
  display: flex;
  margin-top: 10px;
  line-height: 15px;
  font-size: 15px;
  align-items: center;
}

.buttonleft{
  margin-left: 20px;
  margin-right: 10px;
}

.table{
  margin: 10px 20px;
}
/* .searchbar {
  width: 100%;
}

.searchbar ul, li {
  padding: 0;
  margin: 0;
  list-style: none;
  display: inline-block;
}
.searchbar ul {
  display: flex;
  flex-direction: row;
  justify-content:space-around;
}
.searchbar li {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}
.searchbar label{
  font-weight: normal;
  width: 6em;
  padding: 0 4px 0 0 ;
  text-align: right;
}
.searchbar input {
  width: 5em;
  height: 2.5em;
  margin: 0 10px;
  color: white;
  font-weight: bold;
  border: 0;
  border-radius: 10px;
}

.searchbar input:nth-child(1){
  background-color: rgb(0, 140, 255);
}
.searchbar input:nth-child(2){
  background-color: rgb(126, 126, 126);
}
.el-select-dropdown__item{
  display: block;
  padding: 0 0 0 1em;
} */
</style>
    