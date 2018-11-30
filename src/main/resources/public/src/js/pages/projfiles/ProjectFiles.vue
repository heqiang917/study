<!-- 项目文件 -->

<template>
	<div class="project-files">
		<div class="btnbar">
			<div class="upload-file u-btn primary">
				<i></i><span>上传文件</span>
				<FileBrowser :project-module="projectModule" :dirs="folders" :is-multiple="true"
					@submit="onFileSubmitHandler"></FileBrowser>
			</div>
			<div class="upload-folder u-btn primary">
				<i></i><span>上传文件夹</span>
				<FileBrowser :project-module="projectModule" :dirs="folders" :is-multiple="true" :is-folder="true"
					@submit="onFileSubmitHandler"></FileBrowser>
			</div>
			<button class="new-folder u-btn" @click="onFolderBtnHandler"><i></i>新建文件夹</button>
		</div>
		<div class="searchbar">
			<div class="u-searchipt">
				<input v-model="formData.name" type="text" placeholder="输入标题关键字查询" 
					@keydown.enter="onSearchBtnHandler" />
				<button class="searchbtn" @click="onSearchBtnHandler">&nbsp;</button>
			</div>
		</div>
		<div class="navbar">
			<div class="project">
				<span class="backbtn" @click="onBackBtnHandler">&nbsp;</span>
				<span class="title">{{projectTitle}}</span>
			</div>
			<div class="items">
				<a v-for="item in parentFolders" class="item" :key="item.id" :disabled="item.id<0"
					@click="onNavItemHandler(item)">{{item.name}}</a>
			</div>
		</div>
		<div class="filelist">
			<FileList :project="project" :project-module="projectModule" :dir="currentFolder" 
				:keyword="formData.keyword" @dir-changed="onDirChangeHandler"></FileList>
		</div>
		<FolderEditor v-model="folderDialogVisible" :project="project" :project-module="projectModule"
			:dirs="folders" @submit="onFolderSubmitHandler"></FolderEditor>
	</div>
</template>

<script>
	import EventBus from "../../eventBus";
	import FileBrowser from "../../components/upload/FileBrowser";
	import FileList from "./FileList";
	import FolderEditor from "./FolderEditor";

	export default {
		props: ["project", "projectModule"],
		data () {
			return {
				folders: [],

				formData: {
					name: "",
					keyword: ""
				},

				folderDialogVisible: false
			};
		},
		watch: {
			projectModule (newval) {
				if (newval) {
					if (this.currentFolder) {
						if (this.currentFolder.moduleId != newval.id)
							this.folders = [];
					}
					this.formData.name = "";
					this.formData.keyword = "";
				}
				else {
					this.folders = [];
				}
			}
		},
		computed: {
			projectTitle () {
				if (this.project) {
					var p = this.project;
					return `${p.city.province}-${p.city.city}（${p.number.number}号线）：${p.name}`;
				}
				return "无";
			},
			currentFolder () {
				if (this.folders && this.folders.length > 0)
					return this.folders[this.folders.length - 1];
				return null;
			},
			parentFolders () {
				var datas = [];
				datas.push({id: 0, name: "全部文件"});
				if (this.folders && this.folders.length > 0) {
					for (var i = this.folders.length - 1; i >= 0; i--) {
						datas.splice(1, 0, this.folders[i]);
						// if (datas.length > 6)
						// 	break;
					}
					// if (this.folders.length > 6)
					// 	datas.splice(1, 0, {id: -1, name: "..."});
				}
				return datas;
			}
		},
		methods: {
			onFileSubmitHandler (files, moduleId) {
				var params = {projectId: this.project.id, moduleId: moduleId};
				params.dirId = this.currentFolder && this.currentFolder.id;
				EventBus.$emit("file-selected", files, params);
			},

			onFolderBtnHandler () {
				if (!this.currentFolder) {
					if (!this.projectModule) {
						return this._$showMsg("请选择模块", "warning");
					}
					else if (this.projectModule.children) {
						return this._$showMsg("请选择最低层模块", "warning");
					}
				}
				this.folderDialogVisible = true;
			},

			onBackBtnHandler () {
				if (this.folders.length > 0) {
					this.folders.pop();
				}
				else {
					// window.location = "/project";
				}
			},

			onNavItemHandler (data) {
				for (var i = this.folders.length - 1; i >= 0; i--) {
					if (this.folders[i].id == data.id)
						break;
					this.folders.pop();
				}
			},

			onFolderSubmitHandler (data) {
				EventBus.$emit("file-created", data);
			},

			onDirChangeHandler (data) {
				this.formData.name = "";
				this.formData.keyword = "";
				EventBus.$emit("dir-changed", data);
				this._$get("/file/folder", {fileId: data.id}, (datas) => {
					this.folders = _.map(datas, (data) => {
						return {id: data.id, name: data.fileName, 
							moduleIds: [data.modelLId, data.modelPId, data.modelId]};
					});
					this.folders.reverse();
				});
			},

			onSearchBtnHandler () {
				this.folders = [];
				this.$emit("module-reset");
				this.formData.keyword = this.formData.name;
			}
		},
		components: {
			FileList,
			FileBrowser,
			FolderEditor
		}
	}
</script>

<style lang="scss">
.project-files {
	position: relative;

	> .btnbar {
		padding: 15px 0px 15px 20px;

		> .u-btn {
			display: inline-block;
		    position: relative;
		    height: 36px;
		    margin-right: 5px;
		    line-height: 36px;

		    i {
		    	display: inline-block;
			    vertical-align: -3px;
			    width: 16px;
			    height: 16px;
			    margin-right: 3px;
			    background-size: 16px;
			    background-repeat: no-repeat;
			    background-position: center;
			}
		}

		> .u-btn:hover {
			box-shadow: 0px 0px 5px 0px #ccc;
		}

		> .u-btn:active {
			box-shadow: 0px 0px 10px 0px #aaa;
		}

		.new-folder {
			color: #0689ea;
			border: 1px solid #0689ea;
    		background-color: #fff;
		}

		.upload-file i {
			background-image: url(/images/upload_file.png);
		}

		.upload-folder i {
			background-image: url(/images/upload_folder.png);
		}

		.new-folder i {
			background-image: url(/images/create.png);
		}
	}

	> .searchbar {
		position: absolute;
	    top: 15px;
	    right: 30px;

	    .u-searchipt {
	    	input {
	    		width: 200px;
	    	}

	    	button {
	    		pointer-events: initial;
	    		opacity: 0.5;
	    	}

	    	button:hover {
	    		opacity: 1;
	    	}
		}
	}

	> .navbar {
		overflow: hidden;
	    padding: 3px 0px 3px 30px;
	    color: #2196F3;
	    font-size: 13px;
	    line-height: 24px;
    	background-color: #fafafa;

		.project {
			display: inline-block;
			padding: 0px 5px 0px 0px;
			margin-left: -30px;
		    color: #333;
		    font-size: 14px;
		    text-decoration: none;
		    border: 1px solid #f3f3f3;
		    border-radius: 3px;
		    background-color: #f4f5f9;
		    cursor: auto;

		    .backbtn {
		    	display: inline-block;
			    width: 24px;
			    height: 24px;
			    text-decoration: none;
			    background-image: url(/images/back.png);
			    background-size: 20px;
			    background-repeat: no-repeat;
			    background-position: center;
			    opacity: 0.5;
			}

			.backbtn:hover {
				opacity: 1;
			}

			.title {
				display: inline-block;
			    vertical-align: middle;
			    max-width: 800px;
			    white-space: nowrap;
			    overflow: hidden;
			    text-overflow: ellipsis;
			}
		}

		.items {
			display: inline-block;

			a {
		    	display: inline-block;
		    	position: relative;
	    		padding-right: 20px;
		    	vertical-align: bottom;
	    		max-width: 600px;
	    		color: inherit;
	    		white-space: nowrap;
			    overflow: hidden;
			    text-overflow: ellipsis;
			}

			a:after {
				content: ">";
			    position: absolute;
			    right: 6px;
			    color: #c7e3f9;
			    font-size: 12px;
			}

			a:last-child {
				color: #999;
	    		text-decoration: none;
	    		cursor: auto;
			}

			a:last-child:after {
				display: none;
			}

			a[disabled] {
				pointer-events: none;
			}
		}
	}

	> .filelist {
		margin-top: 10px;
	}
}
</style>