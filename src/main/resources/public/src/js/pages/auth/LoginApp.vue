<!-- 登录页面 -->

<template>
	<div class="m-login">
		<div class="login-box">
			<h3>轨道交通全作业平台</h3>
			<el-form ref="form" :model="formData" :rules="formRules">
				<el-form-item prop="username">
					<el-input v-model="formData.username" placeholder="请输入帐号" 
						@keyup.enter.native="onSubmit"></el-input>
				</el-form-item>
				<el-form-item prop="password">
					<el-input v-model="formData.password" type="password" placeholder="请输入密码" 
						@keyup.enter.native="onSubmit"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" :loading="isLoading" @click="onSubmit">{{loginBtnLabel}}</el-button>
				</el-form-item>
			</el-form>
			<div class="errmsg">{{formData.errormsg}}</div>
		</div>
	</div>
</template>

<script>
	import QueryString from "query-string";
	import Const from "../../const";

	export default {
		props: ["options"],
		data () {
			return {
				isLoading: false,
				loginBtnLabel: "登录",
				formData: {
					username: "", // "admin@zhcpa.com",
					password: "", // "123456",
					errormsg: ""
				},
				formRules: {
					username: [
						{required: true, message: "帐号不能为空", trigger: "blur"}
					],
					password: [
						{required: true, message: "密码不能为空", trigger: "blur"}
					]
				}
			};
		},
		methods: {
			onSubmit () {
				if (this.isLoading)
					return ;
				this.$refs["form"].validate((valid) => {
					if (valid) {
						var params = {};
						params.account = this.formData.username;
						params.password = this.formData.password;

						if (!/\@/.test(params.account))
							params.account = params.account + "@zhcpa.cn";

						this.isLoading = true;
						this._$post("/user/login/password", QueryString.stringify(params),
							(data) => {
								this.isLoading = false;
								this.loginBtnLabel = "登录成功";
								window.location.href = "/";
							},
							(code, msg) => {
								this.isLoading = false;
								this.showErrorMsg(msg);
								return false;
							});
					}
				});
			},
			showErrorMsg (errmsg) {
				this.formData.errormsg = errmsg;
				setTimeout(() => {
					this.formData.errormsg = "";
				}, 5000);
			}
		}
	};
</script>