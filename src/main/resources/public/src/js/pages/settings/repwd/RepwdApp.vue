<!-- 重置密码 -->

<template>
	<SettingsContainer>
		<div class="settings-repwd">
			<div class="top">
				<span class="title">修改密码</span>
			</div>
			<div class="formview">
				<el-form ref="form" :model="formData" :rules="formRules" labelWidth="100px" 
					label-position="left" @submit.native.prevent>
					<el-form-item prop="pwd" label="原密码" required>
						<input v-model="formData.pwd" class="u-input" type="password" />
					</el-form-item>
					<el-form-item prop="pwd_new" label="新密码" required>
						<input v-model="formData.pwd_new" class="u-input" type="password" />
					</el-form-item>
					<el-form-item prop="pwd_confirm" label="重复密码" required>
						<input v-model="formData.pwd_confirm" class="u-input" type="password" />
					</el-form-item>
				</el-form>
			</div>
			<div class="btnbar">
				<button class="submitbtn u-btn primary" :disabled="loadingFlag" @click="onSubmitBtnHandler">保存</button>
			</div>
		</div>
	</SettingsContainer>
</template>

<script>
	import QueryString from "querystring";
	import SettingsContainer from "../SettingsContainer";

	export default {
		data () {
			return {
				formData: {
					pwd: "",
					pwd_new: "",
					pwd_confirm: ""
				},
				formRules: {
					pwd: [
						{required: true, message: "密码不能为空", trigger: "blur"}
					],
					pwd_new: [
						{required: true, message: "密码不能为空", trigger: "blur"},
						{validator: (rule, value, callback) => {
							callback((value && value.length >= 8) ? "" : "密码不能少于8位");
						}, trigger: "blur"}
					],
					pwd_confirm: [
						{required: true, message: "密码不能为空", trigger: "blur"},
						{validator: (rule, value, callback) => {
							callback((this.formData.pwd_new && this.formData.pwd_new != value) ? "两次密码不一致" : "");
						}, trigger: "blur"}
					]
				},
				loadingFlag: false
			};
		},
		methods: {
			reset () {
				this.formData.pwd = "";
				this.formData.pwd_new = "";
				this.formData.pwd_confirm = "";
			},

			onSubmitBtnHandler () {
				this.$refs["form"].validate((valid) => {
					if (!!valid) {
						this.loadingFlag = true;
						var params = {};
						params.oldpsw = this.formData.pwd;
						params.newpsw = this.formData.pwd_new;
						this._$post("/user/updatepassword", QueryString.stringify(params), () => {
							this.loadingFlag = false;
							this._$showMsg("修改成功", "success");
							this.reset();
						}, () => {
							this.loadingFlag = false;
						});
					}
				});
			}
		},
		components: {
			SettingsContainer
		}
	}
</script>

<style lang="scss">
.settings-repwd {
	.top {
		margin-bottom: 20px;
	    line-height: 32px;

	    .title {
	    	display: inline-block;
    		font-size: 18px;
		}
	}

	.btnbar {
		padding-left: 100px;
	}
}
</style>