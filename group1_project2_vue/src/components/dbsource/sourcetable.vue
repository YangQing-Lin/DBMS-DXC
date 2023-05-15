<template>
  <div class="sourcetable">
    <el-table
      v-loading="loading"
      element-loading-text="拼命加载中"
      ref="multipleTable"
      :data="showTable"
      :border="true"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="40"> </el-table-column>
      <el-table-column prop="index" label="序号" width="60"></el-table-column>
      <el-table-column label="系统名称" width="120">
        <template slot-scope="scope">
          <a href="#" @click="changedb(scope.row.index)">
          {{scope.row.sysname}}
        </a>
        </template>
      </el-table-column>
      <el-table-column prop="dbtype" label="数据库类型"></el-table-column>
      <el-table-column prop="ip" label="数据库IP"></el-table-column>
      <el-table-column prop="dbname" label="数据库名"></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="port" label="端口"></el-table-column>
    </el-table>
    <div class="resource-page">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[5, 9, 15]"
        :page-size="pagesize"
        :pager-count="7"
        layout=" prev, pager, next,sizes, jumper"
        :total="filterDataShow.length"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import dbsourceapi from '@/api/dbsource';
export default {
  props: {
    getresult: Number,
  },
  data() {
    return {
      tableData: this.$store.state.dbsource.dbsource,
      keywords_sys: "", // 搜索的关键字：系统名
      keywords_db: "", // 搜索的关键字：数据库名
      keywords_type: "",
      filterDataShow: [], //储存符合条件的数据
      pagesize: 5,
      currentPage: 1,
      loading: true,
      reload: 0,
      multipleSelection: [],
    };
  },
  methods: {
    pageload(){
      // console.log(this.getresult)
      if(this.getresult === 20000) {
        //使用searchsource重新加载数据
        this.searchResource()
        this.loading = false
      } else {
        this.$message({
          message: '连接失败，请确认网络情况',
          type: 'warning'
        });
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(this.multipleSelection)
    },
    deletedb() {
      this.multipleSelection.forEach(item => {
        dbsourceapi.deletedb(item.id)
      });
    },
    changedb(value) {
      this.$emit("transfer", value);
      this.$parent.openchange();
    },
    getsearch(value) {
      this.keywords_sys = value.sysname
      this.keywords_type = value.dbtype
      this.keywords_db = value.dbname
      // console.log('查询：系统名称：' + this.keywords_sys + '  数据库类型：' + this.keywords_type + '  数据库名称' + this.keywords_db)
      this.$parent.getall().then(response => {
        this.tableData = this.$store.state.dbsource.dbsource
        this.searchResource()
      })
      
    },
    searchResource() {
      this.currentPage = 1; //将当前页设置为1，确保每次都是从第一页开始搜
      let filterKeywords_sys = this.keywords_sys.trim();
      let filterKeywords_db = this.keywords_db.trim();
      let filterKeywords_type = this.keywords_type.trim();
      let filerReasource = this.tableData.filter(item => { //过滤全部数据
        if (item.sysname.includes(filterKeywords_sys) && item.dbtype.includes(filterKeywords_type) && item.dbname.includes(filterKeywords_db)) {
          // console.log(item)
          return item
        }
      })
      //过滤全部数据
      // console.log('查询：系统名称：' + filterKeywords_sys + '  数据库类型：' + filterKeywords_type + '  数据库名称' + filterKeywords_db)
      this.filterDataShow = filerReasource; //将符合条件的内容赋给filterDataShow
    },
    onSubmit() {},
    handleSizeChange(psize) {
      this.pagesize = psize;
    },
    handleCurrentChange(currentPage) {
      this.currentPage = currentPage;
    },
  },
  mounted() {
    this.searchResource(); //在页面挂载后调用此方法，确保数据与页面发生了交互
  },
  watch: {
    //监听搜索框内容，当搜索框内容发生变化时调用searchResource方法
    keywords: {
      handler() {
        this.searchResource();
      },
    },
    getresult: {
      handler() {
        this.pageload()
      }
    }
  },
  computed: {
    //showTable计算属性通过slice方法计算表格当前应显示的数据
    showTable() {
      return this.filterDataShow.slice(
        (this.currentPage - 1) * this.pagesize,
        this.currentPage * this.pagesize
      );
    },
  },
};
</script>

<style scoped>
.sourcetable {
  margin-top: 1em;
}
a {
  text-decoration: underline;
}
</style>
