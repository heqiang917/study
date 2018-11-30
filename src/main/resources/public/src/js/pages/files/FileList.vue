<!-- 文件列表 -->

<template>
	<div class="file-list">
		<div class="title">查询结果（共{{totalCount}}个文件）</div>
		<Empty v-if="isEmpty">
			<div>没有文件信息</div>
		</Empty>
		<div v-else class="listview">
			<div v-for="model in models" class="item" :key="model.id" :type="model.type">
				<div class="fileinfo">
					<i></i>
					<div class="name" v-html="model.title"></div>
					<div class="module">模块：{{model.moduleName || "无"}}</div>
				</div>
				<div class="project">
					<div>{{model.province}}</div>
					<div>{{model.city}}</div>
					<div>{{model.number}}号线</div>
				</div>
				<div class="user">
					<div>上传：{{model.ownerName}}</div>
					<div>更新：{{model.uploadDate}}</div>
				</div>
				<div class="ops">
					<span class="op download" title="下载" @click="onDownloadBtnHandler(model)">&nbsp;</span>
					<span class="op delete" title="删除" @click="onDeleteBtnHandler(model)">&nbsp;</span>
				</div>
			</div>
		</div>
		<div v-if="pageSize<totalCount" class="more">
			<el-pagination :page-size="pageSize" :current-page="pageNo" :total="totalCount" 
				layout="prev, pager, next"
				@current-change="onPageChangeHandler"></el-pagination>
		</div>
	</div>
</template>

<script>
	import _ from "lodash";
	import Empty from "../../components/common/Empty";

	export default {
		data () {
			return {
				models: [],
				pageInfo: null
			};
		},
		computed: {
			isEmpty () {
				return !this.models || this.models.length == 0;
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
			refresh (params) {
				this.lastParams = params || {};
				if (this.t_refresh) {
					clearTimeout(this.t_refresh);
				}
				this.t_refresh = setTimeout(() => {
					this.t_refresh = null;
					this.loadPageData(1);
				}, 100);
			},

			loadPageData (page) {
				var params = {page: page, limit: this.pageSize};
				params.province = this.lastParams.province || "";
				params.city = this.lastParams.city || "";
				params.number = this.lastParams.line || null;
				params.AmodelId = this.lastParams.module1 || null;
				params.BmodelId = this.lastParams.module2 || null;
				params.CmodelId = this.lastParams.module3 || null;
				params.name = this.lastParams.keyword || "";
				this._$get("/data/find", params, (datas, pageInfo) => {
					this.models = _.map(datas, (data) => {
						var _data = {id: data.id};
						_data.name = data.fileName;
						_data.type = (data.fileType == "folder") ? "dir" : this._$fileExt(data.fileName);

						_data.province = data.province;
						_data.city = data.city;
						_data.moduleName = data.model;
						_data.number = data.number;

						_data.ownerName = data.userName;
						_data.uploadDate = this._$formatDate(data.creatTime);

						_data.title = _data.name;
						if (params.name) {
							_data.title = _data.title.replace(params.name, `<span class='high'>${params.name}</span>`);
						}

						return _data;
					});
					this.pageInfo = pageInfo;
					if ((!datas || datas.length == 0) && page > 1) {
						this.loadPageData(page - 1);
					}
				});
			},

			onDownloadBtnHandler (data) {
				window.open(`/file/download?fileId=${data.id}`);
			},

			onDeleteBtnHandler (data) {
				this.$confirm("确定删除该文件吗？", "删除").then(() => {
					this._$post(`/file/delete?fileId=${data.id}`, () => {
						this._$showMsg("删除成功", "success");
						this.loadPageData(this.pageInfo && this.pageInfo.page || 1);
					});
				}).catch(() => {});
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
	> .title {
		padding-left: 12px;
	    color: #999;
	    font-size: 12px;
	    line-height: 32px;
	    background-color: #fafafa;
	}

	> .listview {
		.item {
			display: flex;
			align-items: center;
			font-size: 14px;
    		border-bottom: 1px solid #f0f0f0;
		}

		.item:hover {
			background-color: #f9f9f9;
		}

		.item > * {
			flex: none;
		}

		.fileinfo {
			flex: auto;
			position: relative;
    		padding: 10px 0px 10px 60px;

    		i {
    			position: absolute;
			    width: 36px;
			    height: 36px;
			    left: 15px;
			    top: 50%;
			    margin-top: -18px;
			    background-image: url(/images/files.png);
    			background-size: 36px;
    			background-repeat: no-repeat;
    		}

    		.name {
    			font-size: 16px;

    			.high {
    				color: #f00;
    			}
    		}

    		.module {
    			margin-top: 5px;
			    color: #aaa;
			    font-size: 14px;
			    -webkit-font-smoothing: antialiased;
    		}
		}

		.project {
			width: 220px;
    		padding: 10px;
    		font-size: 12px;
		}

		.user {
			width: 220px;
		    padding-left: 10px;
		    color: #787878;
		}

		.ops {
			width: 120px;
		    padding-right: 15px;
		    text-align: right;

		    .op {
		    	display: inline-block;
			    width: 20px;
			    height: 20px;
			    background-repeat: no-repeat;
			    background-position: center;
			    cursor: pointer;
			    opacity: 0.5;
			}

			.op:hover {
				opacity: 1;
			}

			.op.download {
				background-image: url(/images/download.png);
				background-size: 20px;
			}

			.op.delete {
				background-image: url(/images/delete.png);
				background-size: 16px;
			}
		}

		.item[type=def] .fileinfo i {
    		background-image: url(/images/file_default.png);
    	}

    	.item[type=dir] .fileinfo i {
    		background-position: 0px -307px;
    	}

    	.item[type=dir] .ops {
    		.op.download {
    			display: none;
    		}
    	}

    	.item[type=xls] .fileinfo i {
    		background-position: 0px -115px;
    	}

    	.item[type=doc] .fileinfo i {
    		background-position: 0px 0px;
    	}

    	.item[type=txt] .fileinfo i {
    		background-position: 0px -345px;
    	}

    	.item[type=ppt] .fileinfo i {
    		background-position: 0px -154px;
    	}

    	.item[type=zip] .fileinfo i {
    		background-position: 0px -77px;
    	}

    	.item[type=pdf] .fileinfo i {
    		background-position: 0px -38px;
    	}

    	.item[type=img] .fileinfo i {
    		background-position: 0px -192px;
    	}

    	.item[type=audio] .fileinfo i {
    		background-position: 0px -230px;
    	}

    	.item[type=video] .fileinfo i {
    		background-position: 0px -269px;
    	}
	}

	> .more {
		padding: 20px 0px 30px 15px;
	}
}
</style>