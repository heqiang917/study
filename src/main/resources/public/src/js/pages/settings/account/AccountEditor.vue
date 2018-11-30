<!-- 帐户编辑 -->

<template>
	<el-dialog v-model="dialogVisible" class="account-editor" :title="dialogTitle" :close-on-click-modal="false">
		<div class="form">
			<el-form label-width="100px" label-position="left" @submit.native.prevent>
				<el-form-item label="邮箱" required>
					<div>
						<input v-model="formData.email" class="u-input" type="text"/>
					</div>
				</el-form-item>
				<el-form-item label="姓名" required>
					<div>
						<input v-model="formData.name" class="u-input" type="text"/>
					</div>
				</el-form-item>
				<el-form-item label="角色" required>
					<div>
						<select v-model="formData.role" class="u-select" style="width:220px">
							<option value="1">管理员</option>
							<option value="2">普通成员</option>
						</select>
					</div>
				</el-form-item>
			</el-form>
		</div>
		<div slot="footer" class="dialog-footer">
			<el-button @click="onCancelHandler">取消</el-button>
			<el-button type="primary" :disabled="loadingFlag" @click="onSubmitHandler">确定</el-button>
		</div>
	</el-dialog>
</template>

<script>
	import QueryString from "querystring";
	import Const from "../../../const";

	export default {
		props: ["value", "model"],
		data () {
			return {
				dialogVisible: !!this.value,
				loadingFlag: false,
				formData: {
					email: "",
					name: "",
					role: 1
				}
			};
		},
		watch: {
			value (newval) {
				this.dialogVisible = !!newval;
			},
			dialogVisible (newval) {
				this.$emit("input", newval);
			},
			model (newval) {
				if (newval) {
					this.formData.email = newval.email;
					this.formData.name = newval.realName;
					this.formData.role = newval.roleType || 1;
				}
				else {
					this.formData.email = "";
					this.formData.name = "";
					this.formData.role = 1;
				}
			}
		},
		computed: {
			dialogTitle () {
				return (this.model && this.model.id) ? "修改帐号" : "添加帐号";
			}
		},
		methods: {
			onSubmitHandler () {
				if (!this.formData.email)
					return this._$showMsg("邮箱不能为空");
				if (!Const.PATTERN.email.test(this.formData.email))
					return this._$showMsg("邮箱格式不正确");

				if (!this.formData.name)
					return this._$showMsg("姓名不能为空");
				if (!Const.PATTERN.name.test(this.formData.name))
					return this._$showMsg("姓名不能包含特殊字符");

				this.loadingFlag = true;
				this.doSave((error) => {
					this.loadingFlag = false;
					if (!error) {
						this._$showMsg("操作成功", "success");
						this.$emit("submit");
						this.dialogVisible = false;
					}
				});
			},

			onCancelHandler () {
				this.dialogVisible = false;
			},

			doSave (callback) {
				var params = {};
				if (this.model && this.model.id) {
					params.id = this.model.id;
					params.email = this.formData.email;
					params.realName = this.formData.name;
					params.roleType = this.formData.role;
				}
				else {
					params.account = this.formData.email;
					params.name = this.formData.name;
					params.type = this.formData.role;
					params = QueryString.stringify(params);
				}

				var url = !params.id ? "/user/add" : "/user/update";
				this._$post(url, params, (data) => {
					callback(false);
				}, () => {
					callback(true);
				});
			}
		}
	}
</script>

<style lang="scss">
</style>