<!-- 地区编辑 -->

<template>
	<el-dialog v-model="dialogVisible" class="district-editor" :title="dialogTitle" :close-on-click-modal="false">
		<div class="form">
			<el-form label-width="100px" label-position="left" @submit.native.prevent>
				<el-form-item label="选择地区">
					<div>
						<select v-model="formData.province" class="u-select">
							<option value="" selected="selected" disabled="disabled">选择省</option>
							<option v-for="item in provinces" :value="item.id">{{item.label}}</option>
						</select>
						<select v-model="formData.city" class="u-select">
							<option value="" selected="selected" disabled="disabled">选择市</option>
							<option v-for="item in citys" :value="item.id">{{item.label}}</option>
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
	import _ from "lodash";
	import CHINA_REGION from 'china-area-data';

	export default {
		props: ["value", "model"],
		data () {
			console.log(this.model);
			return {
				dialogVisible: !!this.value,

				formData: {
					province: "",
					city: ""
				},

				provinces: [],
				citys: [],

				loadingFlag: false
			};
		},
		mounted () {
			var provinces = CHINA_REGION["86"];
			for (var code in provinces) {
				this.provinces.push({id: code, label: provinces[code]});
			}
		},
		watch: {
			value (newval) {
				this.dialogVisible = !!newval;
			},
			dialogVisible (newval) {
				this.$emit("input", newval);
			},
			model (newval) {
				if (newval && newval.id) {
					var _province = newval.province;
					var _city = newval.city;
					var province = _.find(this.provinces, (tmp) => (tmp.label == _province));
					this.formData.province = province && province.id || "";
					if (province) {
						this.$nextTick(() => {
							var city = _.find(this.citys, (tmp) => (tmp.label == _city));
							this.formData.city = city && city.id || "";
						});
					}
				}
				else {
					this.formData.province = "";
					// this.formData.city = "";
				}
			},
			"formData.province" (newval) {
				this.formData.city = "";
				this.citys = [];
				if (newval) {
					var citys = CHINA_REGION[newval];
					for (var code in citys) {
						this.citys.push({id: code, label: citys[code]});
					}
				}
			}
		},
		computed: {
			dialogTitle () {
				return (this.model && this.model.id) ? "修改地区" : "新增地区"; 
			}
		},
		methods: {
			onSubmitHandler () {
				if (!this.formData.province)
					return this._$showMsg("请选择省份");
				if (!this.formData.city)
					return this._$showMsg("请选择市");
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
				params.province = _.find(this.provinces, (tmp) => (tmp.id == this.formData.province));
				params.province = params.province.label;
				params.city = _.find(this.citys, (tmp) => (tmp.id == this.formData.city));
				params.city = params.city.label;

				if (this.model && this.model.id) {
					params.id = this.model.id;
					params.status = 1;
				}

				if (params.id) {
					this._$post("/project/city/update", params, (data) => {
						callback({id: params.id, province: params.province, city: params.city});
					}, () => {
						callback();
					});
				}
				else {
					this._$get("/project/city/creat", params, callback, () => {
						callback();
					});
				}
			}
		}
	}
</script>

<style lang="scss">
.district-editor {
	.form {
		padding-top: 20px;
	}

	select {
		vertical-align: top;
		min-width: 100px;
	    margin-right: 10px;
	}
}
</style>