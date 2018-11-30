<!-- 线路编辑 -->

<template>
	<el-dialog v-model="dialogVisible" class="line-editor" :title="dialogTitle" :close-on-click-modal="false">
		<div class="form">
			<el-form :model="formData" label-width="100px" label-position="left" @submit.native.prevent>
				<el-form-item label="线路" required>
					<div>
						<input v-model="formData.number" class="u-input" type="text"/>
						<span>号线</span>
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
				dialogVisible: false,

				formData: {
					number: ""
				},

				loadingFlag: false
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
				// console.log(newval);
				this.formData.number = newval && newval.line || "";
			},
			"formData.number" (newval) {
				if (newval) {
					if (newval.length > 6)
						newval = newval.substr(0, 6);
					var value = parseInt(newval) || 0;
					if (value > 0) {
						// if (value != newval)
							this.formData.number = value.toFixed(0);
					}
					else {
						this.formData.number = "";
					}
				}
			}
		},
		computed: {
			dialogTitle () {
				return (this.model && this.model.id) ? "修改线路" : "新增线路";
			}
		},
		methods: {
			onSubmitHandler () {
				if (!this.formData.number)
					return this._$showMsg("线路值不能为空");
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
				params.cityId = this.model.cityId;
				params.number = this.formData.number;

				if (this.model && this.model.id) {
					params.id = this.model.id;
					params.status = 1;
				}

				if (params.id) {
					this._$post("/project/number/update", params, (data) => {
						callback({id: params.id, cityId: params.cityId, line: params.number});
					}, () => {
						callback();
					});
				}
				else {
					this._$get("/project/number/creat", params, callback, () => {
						callback();
					});
				}
			}
		}
	}
</script>

<style lang="scss">
.line-editor {
	input {
		vertical-align: middle;
	    width: 80px;
	    margin-right: 10px;
	    text-align: center;
	}
}
</style>