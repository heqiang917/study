<!-- 文件上传视图 -->

<template>
	<div v-if="uploadingFlag" class="file-upload-viewer" :minimize="minimize" :showing="showingFlag">
		<div class="box">
			<div class="title">
				<div v-if="minimize" class="progress">
					<div class="bar" :style="`width:${this.totalPercent}%`"></div>
				</div>
				<div v-if="minimize" class="status">{{statusInfo}}</div>
				<div v-else>上传文件列表</div>
				<div class="expandbtn" @click="onExpandBtnHandler">&nbsp;</div>
			</div>
			<div class="filelist">
				<div v-if="!fileCount" class="empty">没有文件信息</div>
				<template v-else>
					<div v-for="model in models" class="item" :key="model.localId" :type="model.type" :state="model.state">
						<div class="img"><i></i></div>
						<div class="infos">
							<div class="name">{{model.name}}</div>
							<div class="size">{{model._size}}（{{model._send}} / {{model._percent}}）</div>
						</div>
						<div class="progress">
							<div class="bar" :style="`width:${model.percent}%`"></div>
						</div>
						<div class="ops">
							<span class="op start" @click="onOperatorHandler(model, 'start')">&nbsp;</span>
							<span class="op stop" @click="onOperatorHandler(model, 'stop')">&nbsp;</span>
							<span class="op delete" @click="onOperatorHandler(model, 'delete')">&nbsp;</span>
						</div>
					</div>
				</template>
			</div>
		</div>
	</div>
</template>

<script>
	import _ from "lodash";
	import QueryString from "querystring";
	import EventBus from "../../eventBus";
	import FileUploader from "./FileUploader";

	var maxUploadFiles = 3;
	var fileChunkSize = 2 * 1024 * 1024; // 2M

	// fileChunkSize = 100 * 1024; // 100K 测试
	fileChunkSize = 5 * 1024 * 1024; // 5M

	export default {
		data () {
			return {
				models: [],
				minimize: true,
				uploadingFlag: false,
				showingFlag: false
			};
		},
		mounted () {
			EventBus.$on("file-selected", this.onFileSelectedHandler.bind(this));
		},
		computed: {
			fileCount () {
				// console.log("fileCount");
				return this.models.length;
			},
			fileComplete () {
				// console.log("fileComplete");
				return this.completeFiles.length;
			},
			uploadingFiles () {
				return _.filter(this.models, (model) => {
					return model.state == 1;
				});
			},
			beUploadFiles () {
				return _.filter(this.models, (model) => {
					return model.state == 1 || model.state == 2;
				});
			},
			completeFiles () {
				return _.filter(this.models, (model) => {
					return model.state == 3;
				});
			},
			totalSize () {
				// console.log("totalSize");
				var value = 0;
				_.each(this.models, (model) => {
					value += model.size;
				});
				return value;
			},
			totalSend () {
				// console.log("totalSend");
				var value = 0;
				_.each(this.models, (model) => {
					value += model.send;
				});
				return value;
			},
			totalPercent () {
				// console.log("totalPercent");
				return this.totalSize ? (this.totalSend * 100 / this.totalSize) : 0;
			},
			statusInfo () {
				// console.log("statusInfo");
				if (this.fileCount > 0) {
					if (this.fileCount > this.fileComplete) {
						var items = this.beUploadFiles;
						var fileName = (items && items.length > 0) ? items[0].name : "";
						if (items && items.length > 1)
							fileName += "等";
						var percent = this.totalPercent.toFixed(2) + "%";
						return `正在上传(${this.fileComplete}/${this.fileCount})：${fileName} ${percent}`;
					}
					else {
						return "文件上传已完成";
					}
				}
				return "没有上传任务";
			}
		},
		methods: {
			onFileSelectedHandler (files, params) {
				if (!files || files.length == 0)
					return ;

				var localId = Date.now();
				_.each(files, (file) => {
					var model = {file: file, params: params, send: 0, percent: 0};
					model.state = 0; // 0-未处理 1-上传中 2-暂停 3-完成 4-删除动画 9-异常
					model.id = params.fileId || 0;
					model.localId = localId++;
					model.name = file.name;
					model.size = file.size;
					model.type = this._$fileExt(file.name);

					model._size = this._$fileSize(model.size);
					model._send = "0B";
					model._percent = "0%";

					if (file.webkitRelativePath) {
						model.path = file.webkitRelativePath.split("/");
						model.path = model.path.slice(0, model.path.length - 1).join("/");
					}

					model.chunkCount = Math.ceil(model.size / fileChunkSize);

					this.models.push(model);
				});

				this.tryUpload();
				this.cancelHideView();

				this.uploadingFlag = true;
				if (!this.showingFlag) {
					setTimeout(() => {
						this.showingFlag = true;
					}, 100);
				}
			},

			onExpandBtnHandler () {
				this.minimize = !this.minimize;
				if (this.minimize) {
					if (this.totalSize <= 0 || this.totalPercent >= 100) {
						this.tryHideView();
					}
				}
				else {
					this.cancelHideView();
				}
			},

			onOperatorHandler (data, type) {
				if (type == "start") {
					this.doStart(data);
				}
				else if (type == "stop") {
					this.doStop(data);
				}
				else if (type == "delete") {
					this.doRemove(data);
				}
			},

			onFileProgressHandler (fileId, progressValue) {
				var model = _.find(this.models, (model) => {
					return model.localId == fileId;
				});
				// console.log("progress", fileId, model.size, progressValue);
				model.send = Math.min(model.size, progressValue);
				model._send = this._$fileSize(model.send);
				model.percent = model.send * 100 / model.size;
				model._percent = model.percent.toFixed(2) + "%";

				var model = _.find(this.models, (model) => (model.localId == fileId));
				EventBus.$emit("file-progress", model.id, model.percent);
			},

			onFileLoadedHandler (fileId) {
				// console.log("loaded", fileId);
				var model = _.find(this.models, (model) => {
					return model.localId == fileId;
				});
				model.state = 3; // 上传完成

				this.tryUpload();

				if (this.totalPercent >= 100) {
					window.g_fileuploading = false;
					if (this.minimize) {
						this.tryHideView();
					}
				}
			},

			onFileFailedHandler (fileId, message) {
				console.error("failed", fileId, message);
				this._$showMsg(message);
				var model = _.find(this.models, (model) => {
					return model.localId == fileId;
				});
				model.state = 9;
			},

			///////////////////////////////////////////////
			doStart (data) {
				if (this.uploadingFiles.length >= maxUploadFiles) {
					this._$showMsg("已达到文件上传上限，请稍候！", "warning");
				}
				else {
					this.doFileUpload(data);
				}
			},

			doStop (data) {
				data.state = 2;
				data.uploader && data.uploader.stop();
			},

			doRemove (data) {
				data.state = 4; // 删除动画
				setTimeout(() => {
					for (var i = this.models.length - 1; i >= 0; i--) {
						if (this.models[i].localId == data.localId) {
							this.models.splice(i, 1);
							break;
						}
					}
					if (this.models.length == 0) {
						window.g_fileuploading = false;
					}
				}, 220);
			},

			///////////////////////////////////////////////
			tryUpload () {
				if (this.uploadingFiles.length < maxUploadFiles) {
					var data = _.find(this.models, (model) => {
						return model.state == 0;
					});
					if (data) {
						this.doFileCreate(data, (err) => {
							if (!err) {
								this.doFileUpload(data);
							}
							setTimeout(() => {
								this.tryUpload();
							}, 500);
						});
					}
				}
			},

			doFileCreate (data, callback) {
				data.state = 1; // 上传中（先标记一下）
				var params = {};
				params.fileName = data.name;
				params.fileSize = data.size;
				params.chunkSize = fileChunkSize;
				params.chunkNumber = data.chunkCount;
				if (data.id) {
					params.fileId = data.id;
					this._$post("/file/reset", QueryString.stringify(params), (result) => {
						console.log(result);
						callback(false);
					}, () => {
						callback(true);
					});
				}
				else {
					params.projectId = data.params.projectId;
					params.moduleId = data.params.moduleId;
					params.dir = data.params.dirId || 0;
					params.path = data.path || "";
					this._$post("/file/create", QueryString.stringify(params), (result) => {
						var directs = result || [];
						var newFile = directs.pop();
						// var newFile = result && result.length && result[result.length - 1];
						data.id = newFile && newFile.id;
						EventBus.$emit("file-created", newFile, directs);
						callback(false);
					}, (error) => {
						callback(true);
					});
				}
			},

			doFileUpload (data) {
				data.state = 1; // 上传中
				if (!data.uploader) {
					data.uploader = new FileUploader({
						context: this,
						fileId: data.localId,
						file: data.file,
						chunkSize: fileChunkSize,
						chunkCount: data.chunkCount,
						params: {fileId: data.id},
						progressHandler: this.onFileProgressHandler.bind(this),
						loadedHandler: this.onFileLoadedHandler.bind(this),
						errorHandler: this.onFileFailedHandler.bind(this)
					});
				}
				data.uploader.start();
				window.g_fileuploading = true;
			},

			tryHideView () {
				this.cancelHideView();
				this.t_hide = setTimeout(() => {
					this.t_hide = false;
					this.showingFlag = false;
					setTimeout(() => {
						this.models = [];
						this.uploadingFlag = false;
					}, 300);
				}, 10000);
			},

			cancelHideView () {
				if (this.t_hide) {
					clearTimeout(this.t_hide);
					this.t_hide = false;
				}
			}
		}
	}
</script>

<style lang="scss">
.file-upload-viewer {
	position: fixed;
    z-index: 1000;
    left: 0px;
    right: 0px;
    top: 0px;
    bottom: 0px;
    background-color: rgba(255, 255, 255, 0.1);

    .box {
    	position: absolute;
    	overflow: hidden;
	    width: 700px;
	    top: 50%;
	    left: 50%;
	    transform: translate(-50%, -50%);
	    border: 1px solid rgba(6, 137, 234, 0.25);
	    border-radius: 5px;
	    background-color: #fff;
	    box-shadow: 0px 0px 20px 0px #ddd;

	    .title {
	    	padding: 0px 32px 0px 12px;
		    color: #fff;
		    font-size: 16px;
		    line-height: 40px;
		    background-color: #0689ea;

		    .expandbtn {
		    	position: absolute;
			    width: 30px;
			    height: 30px;
			    top: 5px;
			    right: 5px;
			    background-image: url(/images/mini.png);
			    background-size: 20px;
			    background-repeat: no-repeat;
			    background-position: center;
			    cursor: pointer;
			    opacity: 0.5;
			}

			.expandbtn:hover {
				opacity: 1;
			}
		}
	}

	.filelist {
		padding: 20px 15px;
		overflow: auto;
		max-height: 500px;

		.item {
			position: relative;
		    height: 64px;
		    left: 0px;
		    padding: 10px 10px 0px 70px;
		    margin-top: 10px;
		    border-radius: 3px;
		    background-color: #f4f5f9;

			.img {
				position: absolute;
			    width: 60px;
			    left: 0px;
			    top: 0px;
			    bottom: 0px;
			    border-right: 1px solid #ececec;
			    background-color: #f0f0f0;

			    i {
			    	position: absolute;
				    width: 28px;
				    height: 28px;
				    top: 50%;
				    left: 50%;
				    transform: translate(-50%, -50%);
				    background-image: url(/images/files.png);
				    background-size: 28px;
				    background-repeat: no-repeat;
				    background-position: center;
				}
			}

			.infos {
				padding-right: 100px;

				.name {
				    font-size: 14px;
				    white-space: nowrap;
				    overflow: hidden;
				    text-overflow: ellipsis;
				}

				.size {
					color: #999;
					line-height: 16px;
				    -webkit-font-smoothing: antialiased;
				}

				.size:after {
					content: "";
				    display: inline-block;
				    vertical-align: top;
				    width: 16px;
				    height: 16px;
				    background-size: 12px;
				    background-repeat: no-repeat;
				    background-position: center;
				}
			}

			.progress {
				margin-top: 5px;
    			background-color: #f0f0f0;

    			.bar {
    				height: 3px;
    				background-color: #0689ea;
    			}
			}

			.ops {
				position: absolute;
			    top: 50%;
			    right: 5px;
			    margin-top: -10px;

			    .op {
			    	display: none;
				    vertical-align: top;
				    width: 20px;
				    height: 20px;
				    margin: 0px 5px;
				    background-size: 16px;
				    background-repeat: no-repeat;
				    background-position: center;
				    opacity: 0.5;
				    cursor: pointer;
				}

				.op:hover {
					opacity: 1;
				}

				.op.start {
					background-image: url(/images/play.png);
				}

				.op.stop {
					background-image: url(/images/stop.png);
				}

				.op.delete {
					background-image: url(/images/remove.png);
				}
			}
		}

		.item:first-child {
			margin-top: 0px;
		}

		.item[state='0'] {
			.op.start, .op.delete {
				display: inline-block;
			}
		}

		.item[state='1'] {
			.op.stop {
				display: inline-block;
			}
		}

		.item[state='2'] {
			.op.start, .op.delete {
				display: inline-block;
			}
		}

		.item[state='3'] {
			.size:after {
			    background-image: url(/images/success.png);
			}

			.op.delete {
				display: inline-block;
			}
		}

		.item[state='4'] {
			left: -110%;
			transition: left 0.2s;
		}

		.item[state='9'] {
			.size:after {
			    background-image: url(/images/error.png);
			}

			.op.delete {
				display: inline-block;
			}
		}

		.item[type=def] .img i {
    		background-image: url(/images/file_default.png);
    	}

    	.item[type=xls] .img i {
    		background-position: 0px -89px;
    	}

    	.item[type=doc] .img i {
    		background-position: 0px 0px;
    	}

    	.item[type=txt] .img i {
    		background-position: 0px -268px;
    	}

    	.item[type=ppt] .img i {
    		background-position: 0px -119px;
    	}

    	.item[type=zip] .img i {
    		background-position: 0px -59px;
    	}

    	.item[type=dir] .img i {
    		background-position: 0px -239px;
    	}

    	.item[type=pdf] .img i {
    		background-position: 0px -30px;
    	}

    	.item[type=img] .img i {
    		background-position: 0px -149px;
    	}

    	.item[type=audio] .img i {
    		background-position: 0px -179px;
    	}

    	.item[type=video] .img i {
    		background-position: 0px -209px;
    	}

    	.empty {
    		font-size: 16px;
    		text-align: center;
    	}
	}
}
.file-upload-viewer[minimize] {
	left: auto;
    top: auto;

    .box {
    	position: relative;
    	width: 0px;
	    top: auto;
	    left: auto;
	    border: 1px solid rgba(255, 255, 255, 0.5);
	    border-radius: 0px;
	    background-color: transparent;
	    box-shadow: none;
	    transform: none;
	    transition: width 0.25s;

	    .title {
	    	padding: 0px;
	    	background-color: rgba(6, 137, 234, 0.8);

	    	.progress .bar {
	    		height: 40px;
    			background-color: rgba(6, 137, 234, 0.5);
	    	}

	    	.status {
	    		position: absolute;
	    		left: 10px;
	    		right: 40px;
	    		top: 0px;
	    		font-size: 14px;
	    		white-space: nowrap;
			    overflow: hidden;
			    text-overflow: ellipsis;
			    -webkit-font-smoothing: antialiased;
	    	}

	    	.expandbtn {
	    		background-image: url(/images/maxi.png);
	    	}
		}
	}

	.filelist {
		height: 0px;
		padding: 0px;
	}
}
.file-upload-viewer[minimize][showing] {
	.box {
		width: 600px;
	}
}
</style>