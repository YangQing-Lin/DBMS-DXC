<template>
  <div class="addsource">
    <el-dialog
      title="修改数据源"
      :visible.sync="dialogFormVisible"
      :before-close="handleClose"
      width="50%"
      top="30vh"
    >
      <el-form :model="form" :rules="formRules" ref="form">
        <el-col :span="12">
          <el-form-item
            label="系统名称"
            :label-width="formLabelWidth"
            prop="sysname"
          >
            <el-input v-model="form.sysname" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="数据库类型"
            :label-width="formLabelWidth"
            prop="region"
          >
            <el-select v-model="form.dbtype" placeholder="请选择数据库类型">
              <el-option label="Mysql" value="mysql"></el-option>
              <el-option label="Oracle" value="oracle"></el-option>
              <el-option label="SqlServer" value="sqlserver"></el-option>
              <el-option label="PostgreSql" value="postgresql"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="数据库IP"
            :label-width="formLabelWidth"
            prop="ip"
          >
            <el-input v-model="form.ip" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="数据库名"
            :label-width="formLabelWidth"
            prop="dbname"
          >
            <el-input v-model="form.dbname" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="用户名"
            :label-width="formLabelWidth"
            prop="username"
          >
            <el-input v-model="form.username" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="密码" :label-width="formLabelWidth" prop="pwd">
            <el-input
              v-model="form.pwd"
              autocomplete="off"
              show-password
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="端口" :label-width="formLabelWidth" prop="port">
            <el-input v-model="form.port" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.addon" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="save()">保存</el-button>
        <el-button @click="cancel()">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import dbsourceapi from '@/api/dbsource';
export default {
  data() {
    return {
      dialogFormVisible: false,
      id: '',
      form: {
        sysname: '',
        dbtype: '',
        ip: "",
        dbname: "",
        username: "",
        pwd: "",
        port: "",
        addon: "",
      },
      formRules: {
        sysname: [{ required: true, trigger: "blur", message: "该项为必填项" }],
        dbtype: [{ required: true, trigger: "blur", message: "该项为必填项" }],
        ip: [{ required: true, trigger: "blur", message: "该项为必填项" }],
        dbname: [{ required: true, trigger: "blur", message: "该项为必填项" }],
        username: [
          { required: true, trigger: "blur", message: "该项为必填项" },
        ],
        pwd: [{ required: true, trigger: "blur", message: "该项为必填项" }],
        port: [{ required: true, trigger: "blur", message: "该项为必填项" }],
      },
      formLabelWidth: "100px",
    };
  },
  mounted: function() {
  },
  methods: {
    show(value){
      let index = value - 1
      console.log('正在读取数据源id：' + index)
      this.form.id = this.$store.state.dbsource.dbsource[index].id
      this.form.sysname = this.$store.state.dbsource.dbsource[index].sysname
      this.form.dbtype = this.$store.state.dbsource.dbsource[index].dbtype
      this.form.ip = this.$store.state.dbsource.dbsource[index].ip
      this.form.dbname = this.$store.state.dbsource.dbsource[index].dbname
      this.form.username = this.$store.state.dbsource.dbsource[index].username
      this.form.pwd = this.$store.state.dbsource.dbsource[index].pwd
      this.form.port = this.$store.state.dbsource.dbsource[index].port
      this.form.addon = this.$store.state.dbsource.dbsource[index].addon
      this.dialogFormVisible = true
    },
    //添加页面关闭提示
    handleClose(done) {
        this.$confirm('确认关闭？您所做的操作不会保存！')
          .then(_ => {
            this.dialogFormVisible = false
          })
          .catch(_ => {});
      },
    //保存按钮
    save() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.$store.commit('dbsource/SET_TEMPSOURCE', this.form)
          this.changedbsource()
        }
      });
    },
    async changedbsource() {
      console.log(this.$store.state.dbsource.tempsource)
      let p = dbsourceapi.changedbsource(this.$store.state.dbsource.tempsource)
      p.then(response => {
        if(response.code !== 20000){
          this.$message({
            message: '修改失败，请检网络是否联通',
            type: 'warning'
          });
        } else {
          this.$message({
            message: '修改成功',
            type: 'success'
          });
          this.$parent.reset()
          this.dialogFormVisible = false;
        }
      })
    },
    cancel() {
      this.$confirm('确认关闭？您所做的操作不会保存！').then(_ => {
        this.dialogFormVisible = false
      }).catch(_ => {});
    }
  },
};
</script>

<style scoped>
.dialog-footer {
}
</style>
