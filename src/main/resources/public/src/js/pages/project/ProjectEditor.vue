<!-- 项目编辑 -->

<template>
	<el-dialog v-model="dialogVisible" class="project-editor" :title="dialogTitle" :close-on-click-modal="false">
		<div class="form">
			<el-form label-width="100px" label-position="left" @submit.native.prevent>
				<el-form-item label="项目名称" required>
					<div>
						<input v-model="formData.name" class="u-input" type="text"/>
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
	export default {
		props: ["value", "model"],
		data () {
			return {
				dialogVisible: !!this.value,
				loadingFlag: false,

				formData: {
					name: ""
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
				this.formData.name = newval && newval.name || "";
			}
		},
		computed: {
			dialogTitle () {
				return (this.model && this.model.id) ? "编辑项目" : "创建项目";
			}
		},
		methods: {
			onSubmitHandler () {
				if (!this.formData.name)
					return this._$showMsg("项目名称不能为空");
				this.loadingFlag = true;
				this.doSave((data) => {
					this.loadingFlag = false;
					if (data) {
						this._$showMsg("操作成功", "success");
						this.$emit("submit", data);
						this.dialogVisible = false;
					}
				});
			},
			onCancelHandler () {
				this.dialogVisible = false;
			},

			doSave (callback) {
				var params = {};
				params.name = this.formData.name;
				if (this.model && this.model.id) {
					params.id = this.model.id;
					params.status = 1;
					this._$post("/project/project/update", params, (data) => {
						callback({id: params.id, name: params.name});
					}, () => {
						callback();
					});
				}
				else {
					params.numberId = this.model.lineId;
					this._$get("/project/project/creat", params, callback, () => {
						callback();
					});
				}
			}
		}
	}
</script>

<style lang="scss">
</style>