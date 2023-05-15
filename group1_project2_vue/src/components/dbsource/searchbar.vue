<template>
  <div class="searchbar">
    <form>
      <ul>
        <li>
          <label>系统名称</label>
          <el-input v-model="sysname" placeholder="请输入内容"></el-input>
        </li>
        <li>
          <label>数据库类型</label>
          <el-select v-model="dbtype" placeholder="请选择">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </li>
        <li>
          <label>数据库名</label>
          <el-input v-model="dbname" placeholder="请输入内容"></el-input>
        </li>
        <li>
          <el-button type="primary" @click="search()">查询</el-button>
          <el-button @click="reset()">重置</el-button>
        </li>
      </ul>
    </form>
  </div>
</template>

<script>
export default {
  data() {
      return {
        options: [{
          value: '',
          label: '全部'
        }, {
          value: 'mysql',
          label: 'Mysql'
        }, {
          value: 'oracle',
          label: 'Oracle'
        }, {
          value: 'sqlserver',
          label: 'SqlServer'
        }, {
          value: 'postgresql',
          label: 'PostgreSql'
        }],
        dbtype: '',
        sysname: '',
        dbname: '',
      }
    },
    methods: {
      search() {
        let search = {'sysname':this.sysname,'dbtype':this.dbtype,'dbname':this.dbname}
        this.$emit("transfer", search);
      },
      reset() {
        this.sysname = ''
        this.dbtype = ''
        this.dbname = ''
        console.log('reset')
        let search = {'sysname':this.sysname,'dbtype':this.dbtype,'dbname':this.dbname}
        this.$emit("transfer", search);
      }
    }
};
</script>

<style scoped>
.searchbar {
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
}
</style>
