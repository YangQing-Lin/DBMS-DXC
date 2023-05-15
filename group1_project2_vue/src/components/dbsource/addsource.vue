<template>
  <div class="addsource">
    <el-dialog
      title="添加数据源"
      :visible.sync="dialogFormVisible"
      :before-close="handleClose"
      center
      width="50%"
      top="30vh"
    >
      <el-form :model="form" :rules="formRules" ref="form">
        <el-col :span="11">
          <el-form-item
            label="系统名称"
            :label-width="formLabelWidth"
            prop="sysname"
          >
            <el-input v-model="form.sysname" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
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
        <el-col :span="11">
          <el-form-item
            label="数据库IP"
            :label-width="formLabelWidth"
            prop="ip"
          >
            <el-input v-model="form.ip" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item
            label="数据库名"
            :label-width="formLabelWidth"
            prop="dbname"
          >
            <el-input v-model="form.dbname" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item
            label="用户名"
            :label-width="formLabelWidth"
            prop="username"
          >
            <el-input v-model="form.username" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="密码" :label-width="formLabelWidth" prop="pwd">
            <el-input
              v-model="form.pwd"
              autocomplete="off"
              show-password
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="端口" :label-width="formLabelWidth" prop="port">
            <el-input v-model="form.port" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="11">
          <el-form-item label="备注" :label-width="formLabelWidth">
            <el-input v-model="form.addon" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" @click="dbconnecttest()">测试连接</el-button>
        <el-button type="primary" @click="save()">保存</el-button>
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
      form: {
        sysname: "test3",
        dbtype: "",
        ip: "47.99.103.79",
        dbname: "order_system",
        username: "group1_p2_link",
        pwd: "88888888",
        port: "3306",
        addon: "",
        uid: this.$store.state.user.id,
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
    show(){
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
        if (valid && this.form.dbtype !== undefined) {
            this.$store.commit('dbsource/SET_TEMPSOURCE', this.form)
            dbsourceapi.testdb(this.$store.state.dbsource.tempsource).then((res) => {
                console.log(res)
                if(res.code !== 20000) {
                    this.$message({
                        message: '连接检测失败',
                        type: 'warning'
                    });
                } else {
                    this.$store.commit('dbsource/SET_TEMPSOURCE', this.form)
                    this.adddbsource()
                }
            })
            
        } else if(this.form.dbtype === undefined){
          this.$message({
            message: '添加失败，数据库类型未选择',
            type: 'warning'
          });
        }
      });
    },
    async adddbsource() {
      // console.log(this.$store.state.dbsource.tempsource)
      let p = dbsourceapi.adddbsource(this.$store.state.dbsource.tempsource)
      p.then(response => {
        if(response.code !== 20000){
          this.$message({
            message: '添加失败，请检网络是否联通',
            type: 'warning'
          });
        } else {
          this.$message({
            message: '添加成功',
            type: 'success'
          });
          this.$parent.reset()
          this.dialogFormVisible = false;
        }
      })
    },
    async dbconnecttest() {
      this.$store.commit('dbsource/SET_TEMPSOURCE', this.form)
      let p = dbsourceapi.testdb(this.$store.state.dbsource.tempsource)
      p.then(response => {
        // console.log(response)
        // console.log(typeof response.code, response.code)
        // console.log(response.code === 20000)
        if(response.code !== 20000){
          this.$message({
            message: '连接失败，请检查输入是否正确',
            type: 'warning'
          });
          return false
        } else {
          this.$message({
            message: '测试成功',
            type: 'success'
          });
          return true
        }
      })
    }
  },
};
</script>

<style scoped>
.dialog-footer {
  align-items: center;
}
</style>
