<!-- 文件列表 -->

<template>
	<div class="file-list">
		<div class="listview">
			<div class="head">
				<div class="fileinfo">文件名</div>
				<div v-if="isDirVisible" class="dir">所在目录</div>
			</div>
			<Empty v-if="isEmpty">没有文件信息</Empty>
			<div v-else class="items">
				<div v-for="model in models" class="item" :key="model.id" :type="model.type" :state="model.state"
					:active="model.active" @click="onItemClickHandler(model)">
					<div v-if="model.percent" class="progress" :style="`width:${model.percent}%`"></div>
					<div class="fileinfo">
						<i></i>
						<div class="name">
							<span v-html="model.title" @click="onNameClickHandler(model)"></span>
						</div>
						<div class="module">模块：{{model.moduleName || "无"}}</div>
					</div>
					<div v-if="isDirVisible" class="dir">
						<a v-if="model.dirId" @click="onDirClickHandler(model)">{{model.dirName}}</a>
						<span v-else>{{model.dirName}}</span>
					</div>
					<div class="user">
						<div>上传：{{model.ownerName}}</div>
						<div>更新：{{model.uploadDate}}</div>
					</div>
					<div class="ops">
						<span v-if="model.state" class="op download" title="下载" 
							@click="onDownloadBtnHandler(model)">&nbsp;</span>
						<span v-else class="op upload" title="重新上传">
							<input type="file" @change="onUploadFileChangeHandler($event, model)"/>
						</span>
						<span class="op delete" title="删除" @click="onDeleteBtnHandler(model)">&nbsp;</span>
					</div>
					<div class="size">{{model.size}}</div>
				</div>
			</div>
		</div>
		<div v-if="pageSize<totalCount" class="more">
			<el-pagination :page-size="pageSize" :current-page="pageNo" :total="totalCount"
					@current-change="onPageChangeHandler"></el-pagination>
		</div>
		<!-- <div v-if="loadingFlag" class="u-loading"></div> -->
	</div>
</template>

<script>
	import _ from "lodash";
	import Empty from "../../components/common/Empty";
	import EventBus from "../../eventBus";

	export default {
		props: ["project", "projectModule", "dir", "keyword"],
		data () {
			return {
				models: [],
				currentDir: this.dir,
				pageInfo: null,
				loadingFlag: false
			};
		},
		mounted () {
			EventBus.$on("file-created", this.onFileCreatedHandler.bind(this));
			EventBus.$on("file-progress", this.onFileProgressHandler.bind(this));
			this.refresh();
		},
		destroyed () {
			console.log("destroyed");
		},
		watch: {
			project (newval) {
				this.currentDir = null;
				this.refresh();
			},
			projectModule (newval) {
				if (newval && this.currentDir) {
					if (this.currentDir.moduleId == newval.id)
						return ;
				}
				this.currentDir = null;
				this.refresh();
			},
			dir (newval) {
				this.currentDir = newval;
				this.refresh();
			},
			keyword (newval) {
				if (newval) {
					this.currentDir = null;
				}
				this.refresh();
			}
		},
		computed: {
			isEmpty () {
				return !this.models || this.models.length == 0;
			},
			isDirVisible () {
				return !!this.keyword;
			},
			projectId () {
				return this.project && this.project.id || 0;
			},
			moduleId () {
				return this.projectModule && this.projectModule.id || 0;
			},
			directId () {
				return this.currentDir && this.currentDir.id || 0;
			},
			pageNo () {
				return parseInt(this.pageInfo && this.pageInfo.page) || 1;
			},
			pageSize () {
				return parseInt(this.pageInfo && this.pageInfo.size) || 10;
			},
			totalCount () {
				return parseInt(this.pageInfo && this.pageInfo.total) || 0;
			}
		},
		methods: {
			refresh () {
				if (this.t_refresh) {
					clearTimeout(this.t_refresh);
				}
				this.t_refresh = setTimeout(() => {
					this.t_refresh = null;
					this.models = [];
					this.pageInfo = null;
					this.loadPageData(1);
				}, 50);
			},

			loadPageData (page) {
				this.loadingFlag = true;
				var params = {page: page, limit: this.pageSize};
				params.projectId = this.projectId
				if (this.keyword) {
					params.name = this.keyword;
				}
				else {
					params.moduleId = this.moduleId || "";
					params.dir = this.directId;
				}
				var url = "/file/" + (this.keyword ? "find" : "list");
				this._$get(url, params, (datas, pageInfo) => {
					this.loadingFlag = false;
					this.models = _.map(datas, (data) => {
						return this.formatData(data);
					});
					this.pageInfo = pageInfo;
				}, () => {
					this.loadingFlag = false;
				});
			},

			formatData (data) {
				var _data = {active: false};
				_data.id = data.id;
				_data.name = data.fileName;
				_data.projectId = data.projectId;
				_data.moduleId = data.modelId;
				_data.moduleName = data.model;
				_data.ownerId = data.userId;
				_data.ownerName = data.userName;
				_data.type = data.fileType == "folder" ? "dir" : this._$fileExt(data.fileName);
				_data.dirId = data.parentId;
				_data.dirName = data.parentFile && data.parentFile.fileName || "无";
				_data.chunkLoaded = parseInt(data.chunkIndex) || 0;
				_data.chunkNumber = parseInt(data.chunkNumber) || 0;
				_data.uploadDate = this._$formatDate(data.creatTime);
				_data.state = data.state || 0; // 0-未完成 1-已完成
				_data.percent = 0;
				_data.size = this._$fileSize(data.fileSize);
				if (_data.chunkNumber && _data.chunkLoaded < _data.chunkNumber) {
					_data.percent = _data.chunkLoaded * 100 / _data.chunkNumber;
				}
				_data.title = _data.name || "";
				if (this.keyword) {
					_data.title = _data.title.replace(this.keyword, `<span class='high'>${this.keyword}</span>`);
				}
				return _data;
			},

			onItemClickHandler (item) {
				if (item.active)
					return ;
				_.each(this.models, (model) => {
					model.active = false;
				});
				item.active = true;
			},

			onNameClickHandler (data) {
				if (data.type == "dir") {
					this.currentDir = data;
					this.refresh();
					this.$emit("dir-changed", data);
				}
			},

			onDirClickHandler (data) {
				this.currentDir = {id: data.dirId, name: data.dirName, moduleId: data.moduleId};
				this.refresh();
				this.$emit("dir-changed", this.currentDir);
			},

			onDownloadBtnHandler (data) {
				if (data.state == 1) {
					window.open(`/file/download?fileId=${data.id}`);
				}
				else {
					this._$showMsg("该文件上传未完成!");
				}
			},

			onUploadFileChangeHandler (e, data) {
				var files = e.target.files;
				var params = {fileId: data.id};
				EventBus.$emit("file-selected", files, params);
			},

			onDeleteBtnHandler (data) {
				this.$confirm("确定删除该文件吗？", "删除").then(() => {
					this._$post(`/file/delete?fileId=${data.id}`, () => {
						this._$showMsg("删除成功", "success");
						this.refresh();
						EventBus.$emit("file-deleted", data);
					});
				}).catch(() => {});
			},

			onFileCreatedHandler (data, directs) {
				data = this.formatData(data);

				if (data.projectId != this.projectId)
					return ;
				if (this.moduleId && this.moduleId != data.moduleId)
					return ;

				if (directs && directs.length > 0) {
					data = this.formatData(directs[0]);
					if (this.directId && this.directId != data.dirId)
						return ;
					var model = _.find(this.models, (tmp) => (tmp.id == data.id));
					if (!model)
						this.models.unshift(data);
				}
				else {
					if (this.directId && this.directId != data.dirId)
						return ;
					this.models.unshift(data);
				}

			},

			onFileProgressHandler (fileId, percent) {
				var data = _.find(this.models, (tmp) => (tmp.id == fileId));
				if (data) {
					data.percent = percent;
					if (data.percent >= 100) {
						data.state = 1;
						setTimeout(() => {
							data.percent = 0;
						}, 1000);
					}
				}
			},

			onPageChangeHandler (page) {
				this.loadPageData(page);
			}
		},
		components: {
			Empty
		}
	}
</script>

<style lang="scss">
.file-list {
	.listview {
		.head {
			display: flex;
		    align-items: center;
		    height: 32px;
		    color: #999;
		    font-size: 13px;
		    border-bottom: 1px solid #eee;

		    > .fileinfo {
		    	flex: auto;
		    	padding-left: 20px;
			}

		    > .dir {
		    	flex: none;
		    	width: 360px;
			}
		}

		.item {
			display: flex;
			position: relative;
		    align-items: center;
		    height: 60px;
		    font-size: 14px;
		    border-bottom: 1px solid #f6f6f6;
		    transition: background-color 0.5s;

			.progress {
				flex: none;
				position: absolute;
				height: 1px;
			    left: 0px;
			    bottom: 0px;
			    background-color: #04b72b;
			    pointer-events: none;
			}
			
			.fileinfo {
				flex: auto;
				position: relative;
    			padding: 0px 20px 0px 60px;

				i {
	    			position: absolute;
				    width: 32px;
				    height: 32px;
				    left: 20px;
				    top: 50%;
				    margin-top: -16px;
				    background-image: url(/images/files.png);
				    background-size: 32px;
				    background-repeat: no-repeat;
				    background-position: center;
	    		}

	    		.name {
	    			max-width: 420px;
	    			margin-bottom: 5px;
				    font-size: 15px;
				    line-height: 22px;
				    white-space: nowrap;
				    overflow: hidden;
				    text-overflow: ellipsis;

				    .high {
				    	color: #f00;
					}
	    		}

	    		.module {
	    			max-width: 420px;
	    			color: #999;
				    font-size: 13px;
				    white-space: nowrap;
				    overflow: hidden;
				    text-overflow: ellipsis;
				    -webkit-font-smoothing: antialiased;
	    		}
			}

			.dir {
				flex: none;
				width: 120px;
				padding-right: 10px;
				word-wrap: break-word;
    			word-break: break-word;
			}

			.user {
				flex: none;
				width: 160px;
			    color: #999;
			    font-size: 13px;
			}

			.size {
				position: absolute;
			    right: 5px;
			    bottom: 0px;
			    color: #999;
			    font-size: 12px;
			    -webkit-font-smoothing: antialiased;
			}

			.ops {
				flex: none;
				width: 80px;
				padding-right: 10px;
			    text-align: right;

			    .op {
			    	display: inline-block;
			    	position: relative;
				    vertical-align: top;
    				overflow: hidden;
				    width: 20px;
				    height: 20px;
				    margin-left: 5px;
				    background-repeat: no-repeat;
				    background-position: center;
				    cursor: pointer;
				    opacity: 0.35;
				}

				.op:hover {
					opacity: 1;
				}

				.op.download {
					background-image: url(/images/download.png);
					background-size: 22px;
				}

				.op.upload {
					display: none;
					background-image: url(/images/upload.png);
					background-size: 16px;

					input {
						position: absolute;
					    left: 0px;
					    top: 0px;
					    opacity: 0;
					}
				}

				.op.delete {
					background-image: url(/images/delete.png);
	    			background-size: 16px;
				}
			}
		}

		.item:hover {
			background-color: #f6f6f6;
		}

		.item[active]:after {
			content: "";
			position: absolute;
		    left: 0px;
		    right: 0px;
		    top: 0px;
		    bottom: 0px;
		    border: 1px solid rgba(6, 137, 234, 0.65);
		    border-radius: 2px;
		    pointer-events: none;
		}

		.item[active] .progress {
			height: 2px;
		}

		.item[state='0'] {
			.fileinfo {
	    		opacity: 0.5;
			}

			.op.download {
				display: none;
			}

			.op.upload {
				display: inline-block;
			}
		}

		.item[type=def] .fileinfo i {
    		background-image: url(/images/file_default.png);
    	}

    	.item[type=xls] .fileinfo i {
    		background-position: 0px -102px;
    	}

    	.item[type=doc] .fileinfo i {
    		background-position: 0px 0px;
    	}

    	.item[type=txt] .fileinfo i {
    		background-position: 0px -306px;
    	}

    	.item[type=ppt] .fileinfo i {
    		background-position: 0px -136px;
    	}

    	.item[type=zip] .fileinfo i {
    		background-position: 0px -68px;
    	}

    	.item[type=dir] {
    		.fileinfo {
    			i {
    				background-position: 0px -272px;
    			}

    			.name span:hover {
    				cursor: pointer;
    				text-decoration: underline;
    			}
    		}

    		.size {
    			display: none;
    		}

    		.ops {
    			.download {
    				display: none;
    			}
    		}
    	}

    	.item[type=pdf] .fileinfo i {
    		background-position: 0px -33px;
    	}

    	.item[type=img] .fileinfo i {
    		background-position: 0px -170px;
    	}

    	.item[type=audio] .fileinfo i {
    		background-position: 0px -204px;
    	}

    	.item[type=video] .fileinfo i {
    		background-position: 0px -238px;
    	}

    	.empty {
    		flex: auto;
		    padding-top: 200px;
		    color: #999;
		    text-align: center;
		    background-image: url(/images/empty.png);
		    background-size: 100px;
		    background-repeat: no-repeat;
		    background-position: center 80px;
    	}
	}

	.more {
		padding: 30px 10px 20px;
	}
}
</style>