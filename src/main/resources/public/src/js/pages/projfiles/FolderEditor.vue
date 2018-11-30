<!-- 文件夹编辑 -->

<template>
	<el-dialog v-model="dialogVisible" class="district-editor" :title="dialogTitle" :close-on-click-modal="false">
		<div class="form">
			<el-form label-width="100px" label-position="left" @submit.native.prevent>
				<el-form-item label="所属模块">
					<div>
						<div>{{parentModuleNames || "无"}}</div>
					</div>
				</el-form-item>
				<el-form-item label="所属目录">
					<div>
						<div><pre>{{parentNames || "无"}}</pre></div>
					</div>
				</el-form-item>
				<el-form-item label="文件夹名称">
					<div>
						<input v-model="formData.name" class="u-input" placeholder="请输入文件夹名称" />
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
	import QueryString from "querystring";

	export default {
		props: ["value", "project", "projectModule", "dirs"],
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
				this.formData.name = "";
				this.dialogVisible = !!newval;
			},
			dialogVisible (newval) {
				this.$emit("input", newval);
			}
		},
		computed: {
			dialogTitle () {
				return "新建文件夹";
			},
			parentModuleNames () {
				var modules = [];
				var moduleId = this.curentModuleId;
				while (true) {
					var module = _.find(window.g_projectModules, function (temp) {
						return temp.id == moduleId;
					});
					if (!module)
						break;
					modules.unshift(module.name);
					moduleId = module.pid;
				}
				return modules.join(" > ");
			},
			parentNames () {
				if (this.dirs) {
					var names = _.map(this.dirs, (tmp) => (tmp.name));
					return names.join(" > ");
				}
				return "";
			},
			curentModuleId () {
				if (this.dirs && this.dirs.length > 0)
					return this.dirs[this.dirs.length - 1].moduleIds[2];
				if (this.projectModule)
					return this.projectModule.id;
				return 0;
			},
			currentDirId () {
				if (this.dirs && this.dirs.length > 0) {
					return this.dirs[this.dirs.length - 1].id;
				}
				return 0;
			}
		},
		methods: {
			onSubmitHandler () {
				if (!this.formData.name)
					return this._$showMsg("文件夹名称不能为空", "warn");
				this.loadingFlag = true;
				this.doSave((data) => {
					this.loadingFlag = false;
					if (data) {
						this._$showMsg("新建成功", "success");
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
				params.projectId = this.project.id;
				params.moduleId = this.curentModuleId;
				params.dir = this.currentDirId || 0;
				params.fileName = this.formData.name;
				params = QueryString.stringify(params);
				this._$post("/file/createdir", params, (data) => {
					callback(data);
				}, () => {
					callback();
				});
			}
		}
	}
</script>

<style lang="scss">
</style>