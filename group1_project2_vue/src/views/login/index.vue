<template>
  <div class="login-container">
    <!--                              登录界面                              -->

    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
      v-show="islogin"
    >
      <div class="title-container">
        <h3 class="title">欢迎使用管理系统</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="用户名"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="密码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon
            :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"
          />
        </span>
      </el-form-item>

      <el-form-item prop="verification">
        <span class="svg-container">
          <svg-icon icon-class="verification" />
        </span>
        <!-- 
          把v-model="loginForm.inputIdentifyCode"
          换成v-model="loginForm.identifyCode"
          切换是否自动输入验证码
          开发期间自动输入，之后调回
        -->
        <el-input
          ref="verification"
          v-model="loginForm.identifyCode"
          placeholder="验证码"
          name="verification"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
          style="width: 260px"
        />
        <span class="verification-img" @click="refreshCode">
          <s-identify :identifyCode="loginForm.identifyCode"></s-identify>
        </span>
      </el-form-item>

      <el-form-item prop="type">
        <div>
          <span class="svg-container">
            <svg-icon icon-class="sex" />
          </span>
          <label class="sex">用户权限选择</label>
          <el-radio-group
            v-model="powerradio"
            @change="getpowerradio()"
            size="medium"
          >
            <el-radio-button label="用户"></el-radio-button>
            <el-radio-button label="管理员"></el-radio-button>
          </el-radio-group>
        </div>
      </el-form-item>

      <el-button
        :loading="loading"
        type="primary"
        style="width: 100%; margin-bottom: 5px"
        @click.native.prevent="handleLogin"
        >登 录</el-button
      >
      <el-button style="width: 100%; margin-bottom: 30px" @click="toSignIn"
        >去注册</el-button
      >
    </el-form>

    <!--                              注册界面                              -->

    <el-form
      ref="signinForm"
      :model="signinForm"
      :rules="signinRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
      v-show="issignin"
    >
      <div class="title-container">
        <h3 class="title">正在注册管理系统</h3>
      </div>

      <el-form-item prop="name">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="name"
          v-model="signinForm.name"
          placeholder="用户名"
          name="name"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="signinForm.username"
          placeholder="账号"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="email">
        <span class="svg-container">
          <svg-icon icon-class="email" />
        </span>
        <el-input
          ref="email"
          v-model="signinForm.email"
          placeholder="邮箱"
          name="email"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="signinForm.password"
          :type="passwordType"
          placeholder="密码：数字+英文+特殊字符+至少8位"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon
            :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"
          />
        </span>
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="confirmpwd"
          v-model="signinForm.confirmpwd"
          :type="passwordType"
          placeholder="确认密码"
          name="confirmpwd"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon
            :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"
          />
        </span>
      </el-form-item>

      <el-form-item prop="phone">
        <span class="svg-container">
          <svg-icon icon-class="phone" />
        </span>
        <el-input
          ref="phone"
          v-model="signinForm.phone"
          placeholder="电话"
          name="phone"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="sex">
        <div>
          <span class="svg-container">
            <svg-icon icon-class="sex" />
          </span>
          <label class="sex">性别</label>
          <el-radio-group v-model="radio" @change="getradio()" size="medium">
            <el-radio-button label="男"></el-radio-button>
            <el-radio-button label="女"></el-radio-button>
          </el-radio-group>
        </div>
      </el-form-item>

      <el-button
        :loading="loading"
        type="primary"
        style="width: 100%; margin-bottom: 5px"
        @click.native.prevent="handleSignin"
        >注 册</el-button
      >

      <el-button style="width: 100%; margin-bottom: 30px" @click="toLogIn"
        >返回登录</el-button
      >
    </el-form>
  </div>
</template>

<script>
import { validUsername } from "@/utils/validate";
import SIdentify from "@/layout/components/verification/SIdentify.vue";
import userApi from "@/api/userManage";

export default {
  components: { SIdentify },
  name: "Login",
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error("请输入正确的用户名"));
      } else {
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (value.length < 8) {
        callback(new Error("密码不能少于8位"));
      } else {
        callback();
      }
    };
    return {
      loginForm: {
        username: "",
        password: "",
        inputIdentityCode: "",
        identifyCode: "",
        identifyCodes: "1234567890abcdefghijklmnopqrstvuwxyz",
      },
      signinForm: {
        url: "https://project-static-file.oss-cn-hangzhou.aliyuncs.com/avatar/7.jpeg",
        name: "",
        username: "",
        email: "",
        password: "",
        confirmpwd: "",
        phone: "",
        sex: "男",
        type: 0,
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", validator: validateUsername },
        ],
        password: [
          { required: true, trigger: "blur", validator: validatePassword },
        ],
      },
      //validate验证表单合法性
      signinRules: {
        username: [
          {
            required: true,
            trigger: "blur",
            message: "用户名不能为空",
          },
        ],
        email: [
          {
            required: true,
            pattern:
              /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/,
            trigger: "blur",
            message: "邮箱格式不正确",
          },
        ],
        password: [
          {
            required: true,
            pattern: /(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{8,16}/,
            trigger: "blur",
            message: "密码格式不正确,数字+英文+特殊字符+至少8位",
          },
        ],
        confirmpwd: [
          {
            required: true,
            trigger: "blur",
            message: "再次输入密码不同",
          },
        ],
        phone: [
          {
            required: true,
            pattern: /(13|14|15|18|17)[0-9]{9}/,
            trigger: "blur",
            message: "电话格式不正确",
          },
        ],
      },
      loading: false,
      passwordType: "password",
      redirect: undefined,
      islogin: true,
      issignin: false,
      powerradio: "用户",
      radio: "男",
    };
  },
  mounted() {
    this.makeCode(this.identifyCodes, 4);
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true,
    },
  },
  methods: {
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    getpowerradio() {
      if (this.powerradio == "管理员") this.loginForm.type = 1;
      else this.loginForm.type = 0;
    },
    handleLogin() {
      //登录前验证验证码是否正确
      //注释掉的内容为正常代码，为方便测试绑定自动填充
      // if (this.loginForm.identifyCode !== this.loginForm.inputIdentifyCode) {
      this.getpowerradio();
      console.log("用户权限：" + this.loginForm.type);
      if (this.loginForm.identifyCode == null) {
        this.$message({
          message: "验证码错误！",
          type: "warning",
        });
        this.refreshCode();
      } else {
        this.$refs.loginForm.validate((valid) => {
          if (valid) {
            console.log("login form: ", this.loginForm);
            this.loading = true;
            this.$store
              .dispatch("user/login", this.loginForm)
              .then(() => {
                this.$router.push({ path: this.redirect || "/" });
                this.loading = false;
              })
              .catch(() => {
                this.loading = false;
              });
              console.log("store", this.$store.state);
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      }
    },
    //验证码图片切换
    refreshCode() {
      this.loginForm.identifyCode = "";
      this.makeCode(this.loginForm.identifyCodes, 4);
    },
    makeCode(o, l) {
      for (let i = 0; i < l; i++) {
        this.loginForm.identifyCode +=
          this.loginForm.identifyCodes[
            this.randomNum(0, this.loginForm.identifyCodes.length)
          ];
      }
    },
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },
    //获取注册页性别
    getradio() {
      this.signinForm.sex = this.radio;
    },
    //注册页内容获取
    handleSignin() {
      this.$refs.signinForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          console.log(
            "头像" + this.signinForm.url +
            "用户名" + this.signinForm.name +
            "账号" + this.signinForm.username +
            "密码" + this.signinForm.password +
            "确认密码" + this.signinForm.confirmpwd +
            "邮箱" + this.signinForm.email +
            "电话" + this.signinForm.phone +
            "性别" + this.signinForm.sex
          );
          //验证两次密码是否相同，其他验证使用validate在上方
          if (this.signinForm.password === this.signinForm.confirmpwd) {
              // 提交请求给后台
              userApi.saveUser(this.$store.state.user.token, this.signinForm).then(response => {
                console.log("sign in user:", this.signinForm);
                // 成功提示
                this.$message({
                  message: response.message,
                  type: 'success'
                });
                // 关闭对话框
                this.dialogFormVisible = false;
                // 刷新表格
                this.getUserList();
              });
            this.$message({
              message: "注册成功,即将返回登录！",
              type: "success",
            });
            this.issignin = false;
            this.islogin = true;
          } else {
            this.$message({
              message: "注册失败，两次密码不同！",
              type: "warning",
            });
          }
          this.loading = false;
        } else {
          //注册失败后，在输入框下方显示提示，由validate控制，此处仅弹窗提示
          this.$message({
            message: "注册失败，请留意提示！",
            type: "warning",
          });
          console.log("error submit!!");
          this.loading = false;
          return false;
        }
      });
    },
    //切换登录、注册
    toSignIn() {
      this.islogin = false;
      this.issignin = true;
    },
    toLogIn() {
      this.islogin = true;
      this.issignin = false;
    },
  },
};
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
    margin: 0;
    margin-bottom: 20px;
  }

  .el-button + .el-button {
    margin-left: 0px;
  }
  .el-form-item__error {
    color: rgb(255, 81, 0);
    margin-left: 60px;
  }
  .login-container {
    display: inline-block;
    margin: 10px 0;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  display: flex;
  align-items: center;
  background-image: url("../../assets/LoginPage/bg.jpg");
  background-size: cover;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 35px 35px 0;
    margin: 0 auto;
    overflow: hidden;

    background-color: #daa27dd5;
    border-radius: 20px;
    backdrop-filter: blur(5px);
  }

  span {
    &:first-of-type {
      margin-right: 16px;
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .sex {
    color: rgb(196, 196, 196);
    margin: 0 20px 0 0;
    padding: 12px 5px 12px 15px;
    font-weight: 500;
  }
  .signin-tip {
    color: #685d52;
    font-size: 10px;
  }
  .verification-img {
    display: inline-block;
  }
  .s-canvas {
    position: absolute;
    margin-top: -20px;
  }
}
</style>
