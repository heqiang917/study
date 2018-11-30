<!-- 文件上传按钮 -->

<template>
	<div class="file-browser">
		<div class="dialogbtn" @click="onClickHandler">
			<input ref="testInput" webkitdirectory/>
		</div>
		<el-dialog v-model="dialogVisible" class="file-browser-dialog" :title="dialogTitle" :close-on-click-modal="false">
			<div class="form">
				<el-form label-width="100px" label-position="left" @submit.native.prevent>
					<el-form-item label="资料分类">
						<div>
							<div>
								<select v-model="formData.module1" class="u-select" :disabled="isModuleFixed(1)">
									<option value="" selected="selected" disabled="disabled">选择阶段</option>
									<option v-for="item in modules1" :value="item.id">{{item.name}}</option>
								</select>
							</div>
							<div>
								<select v-model="formData.module2" class="u-select" :disabled="isModuleFixed(2)">
									<option value="" selected="selected" disabled="disabled">选择环节</option>
									<option v-for="item in modules2" :value="item.id">{{item.name}}</option>
								</select>
							</div>
							<div>
								<select v-model="formData.module3" class="u-select" :disabled="isModuleFixed(3)">
									<option value="" selected="selected" disabled="disabled">选择子环节</option>
									<option v-for="item in modules3" :value="item.id">{{item.name}}</option>
								</select>
							</div>
						</div>
					</el-form-item>
					<el-form-item v-if="!!parentNames" label="所属目录">
						<div>
							<div>{{parentNames || "无"}}</div>
						</div>
					</el-form-item>
					<el-form-item label="选择文件">
						<div>
							<span ref="browserBtn" class="browserbtn" @change="onBrowserHandler">
								<span>浏览..</span>
								<input type="file" :multiple="isMultiple" :webkitdirectory="isFolder"/>
							</span>
							<span class="filelist">{{filesName}}</span>
						</div>
					</el-form-item>
				</el-form>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="onCancelHandler">取消</el-button>
				<el-button type="primary" :disabled="loadingFlag" @click="onSubmitHandler">确定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import _ from "lodash";

	export default {
		props: ["projectModule", "dirs", "isMultiple", "isFolder"],
		data () {
			return {
				dialogVisible: false,
				loadingFlag: false,

				modules1: [],
				modules2: [],
				modules3: [],

				formData: {
					module1: "",
					module2: "",
					module3: ""
				},

				files: []
			};
		},
		watch: {
			"formData.module1" (newval) {
				this.formData.module2 = "";
				this.formData.module3 = "";
				this.modules2 = [];
				this.modules3 = [];
				if (newval) {
					this.modules2 = _.filter(window.g_projectModules, (tmp) => (tmp.pid == newval));
					if (this.modules2.length > 0)
						this.formData.module2 = this.modules2[0].id;
				}
			},
			"formData.module2" (newval) {
				this.formData.module3 = "";
				this.modules3 = [];
				if (newval) {
					this.modules3 = _.filter(window.g_projectModules, (tmp) => (tmp.pid == newval));
					if (this.modules3.length > 0)
						this.formData.module3 = this.modules3[0].id;
				}
			}
		},
		computed: {
			dialogTitle () {
				return this.isFolder ? "上传文件夹" : "上传文件";
			},
			filesName () {
				if (this.files && this.files.length > 0) {
					var name = this.files[0].name;
					if (this.files.length > 1)
						name += "等";
					return name;
				}
				if (this.isFolder)
					return "还未上传文件夹";
				return "还未选择文件";
			},
			currentDir () {
				return this.dirs ? this.dirs[this.dirs.length - 1] : null;
			},
			parentNames () {
				if (this.dirs) {
					var names = _.map(this.dirs, (tmp) => (tmp.name));
					return names.join(" > ");
				}
				return "";
			}
		},
		methods: {
			isModuleFixed (level) {
				if (this.currentDir)
					return true;
				// if (this.projectModule) {
				// 	var form = this.formData;
				// 	if (this.projectModule.id == form.module3)
				// 		return true;
				// 	if (this.projectModule.id == form.module2)
				// 		return level <= 2;
				// 	if (this.projectModule.id == form.module1)
				// 		return level == 1;
				// }
				return false;
			},

			onClickHandler () {
				if (!this.isFolder || this.$refs["testInput"].webkitdirectory) {
					this.dialogVisible = true;
					this.files = [];
					this.modules1 = _.filter(window.g_projectModules, (tmp) => (tmp.pid == 0));
					var parents = null;
					if (this.currentDir) {
						var moduleIds = this.currentDir.moduleIds;
						parents = [{id: moduleIds[0]}];
						if (moduleIds[1])
							parents.push({id: moduleIds[1]});
						if (moduleIds[2] && moduleIds[2] != moduleIds[1])
							parents.push({id: moduleIds[2]});
					}
					else if (this.projectModule) {
						parents = this.getParents(this.projectModule);
					}
					if (parents) {
						this.formData.module1 = parents[0].id;
						if (parents.length > 1) {
							this.$nextTick(() => {
								this.formData.module2 = parents[1].id;
								if (parents.length > 2) {
									this.$nextTick(() => {
										this.formData.module3 = parents[2].id;
									});
								}
							});
						}
					}
					else {
						this.formData.module1 = this.modules1[0].id;
					}
				}
				else {
					this.$alert("当前浏览器不支持文件夹上传功能，请使用较新版本的现代浏览器（如：Chrome, Firefox, Edge, Safari）", "提示")
						.then(() => {}).catch(() => {});
				}
			},

			onBrowserHandler (e) {
				this.files = e.target.files;
				var refreshHTML = this.$refs["browserBtn"].innerHTML;
				this.$refs["browserBtn"].innerHTML = "";
				this.$refs["browserBtn"].innerHTML = refreshHTML;
			},

			onSubmitHandler () {
				if (!this.files || this.files.length == 0)
					return this._$showMsg("请选择上传文件", "warning");
				var form = this.formData;
				var moduleId = form.module3 || form.module2 || form.module1;
				if (moduleId) {
					this.$emit("submit", this.files, moduleId)
					this.dialogVisible = false;
				}
				else {
					this._$showMsg("请选择资料分类", "warning");
				}
			},

			onCancelHandler () {
				this.dialogVisible = false;
			},

			getParentModule (data, limitPid) {
				if (data.pid == 0)
					return data;
				if (data.pid == limitPid)
					return data;
				var _data = _.find(window.g_projectModules, (tmp) => (tmp.id == data.pid));
				if (_data)
					return this.getParentModule(_data, limitPid);
				return data;
			},

			getParents (data) {
				var datas = [data];
				while (data && data.pid) {
					data = _.find(window.g_projectModules, (tmp) => (tmp.id == data.pid));
					if (data) {
						datas.unshift(data);
					}
				}
				return datas;
			}
		}
	}
</script>

<style lang="scss">
.file-browser {
	position: absolute;
    width: 100%;
    height: 100%;
    left: 0px;
    top: 0px;
    text-align: left;
    cursor: auto;

    .dialogbtn {
    	width: 100%;
    	height: 100%;
    	cursor: pointer;

    	input {
    		display: none;
    	}
	}

	.form {
		padding-top: 20px;

		select {
		    vertical-align: top;
			min-width: 400px;
		    margin-bottom: 10px;
		}

		.browserbtn {
			display: inline-block;
			position: relative;
    		vertical-align: top;
		    overflow: hidden;
		    width: 100px;
		    height: 36px;
		    padding: 0px 10px;
		    color: #333;
		    font-size: 14px;
		    border: 1px solid #ddd;
		    border-radius: 5px;
		    background-color: #f8f8f8;
		    cursor: pointer;

		    input {
			    position: absolute;
		    	width: 100%;
		    	height: 100%;
		    	left: 0px;
		    	top: 0px;
		    	opacity: 0;
			}
		}

		.browserbtn:hover {
			background-color: #f0f0f0;
		}

		.browserbtn:active {
			background-color: #eee;
		}

		.filelist {
			display: inline-block;
		    vertical-align: top;
		    max-width: 350px;
		    white-space: nowrap;
		    overflow: hidden;
		    text-overflow: ellipsis;
		}
	}
}
</style>