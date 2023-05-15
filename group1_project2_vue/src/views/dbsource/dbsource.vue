<template>
  <div class="dbsource">
    <searchbar @transfer="getsearch" ref="reset"></searchbar>
    <el-divider></el-divider>
    <div class="tables">
      <div class="addremovebtn">
        <el-button type="primary" @click="openadd()">添加</el-button>
        <el-button type="danger" @click="deletedb()">删除</el-button>
      </div>
      <sourcetable @transfer="getchangeid" ref="sourcetable" :getresult="getresult.code"></sourcetable>
    </div>
    <addsource ref="adddialog"></addsource>
    <changesource ref="changedialog" :id="changeid"></changesource>
  </div>
</template>

<script>
import dbsourceapi from '@/api/dbsource'
import searchbar from '@/components/dbsource/searchbar.vue'
import Addsource from '@/components/dbsource/addsource.vue'
import Sourcetable from '@/components/dbsource/sourcetable.vue'
import Changesource from '@/components/dbsource/changesource.vue'
export default {
  components: { searchbar, Addsource, Sourcetable, Changesource },
  data() {
    return {
      changeid: '',
      getresult: '',
    }
  },
  created: function() {
    this.getall()
  },
  methods: {
    reset() {
      this.$refs.reset.reset()
    },
    getsearch(value) {
      this.$refs.sourcetable.getsearch(value)
    },
    openadd() {
      this.$refs.adddialog.show()
    },
    getchangeid(id) {
      this.changeid = id
    },
    openchange() {
      this.$refs.changedialog.show(this.changeid)
    },
    deletedb() {
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$refs.sourcetable.deletedb()
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.reset()
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });          
        });
    },
    async getall() {
      this.$store.commit('dbsource/CLEAR_DBSOURCE')
      let p = dbsourceapi.getdball()
      p.then(response => {
        this.$store.dispatch('dbsource/setdbsource', response.data)
      })
      this.getresult = await p
    }
  }
}
</script>

<style scoped>
.dbsource{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.searchbar{
  margin-top: 1.5em;
}
.tables{
  width: 90%;
}
</style>