<!-- 项目列表 -->

<template>
	<div class="project-list">
		<Empty v-if="!category">
			<p>没有项目信息</p>
			<p class="sub">请先在左边新增地区和线路</p>
		</Empty>
		<template v-else>
			<div class="topbar">
				<div class="u-searchipt">
					<input v-model="formData.name" placeholder="输入项目名称模糊搜索" @keydown.enter="doSearch"/>
					<button @click="doSearch">&nbsp;</button>
				</div>
				<button class="createbtn u-btn primary" @click="onCreateBtnHandler">创建项目</button>
			</div>
			<div class="listview">
				<table class="u-table">
					<thead>
						<tr>
							<th>项目名称</th>
							<th>创建人</th>
							<th>创建时间</th>
							<th style="text-align:right;">操作</th>
						</tr>
					</thead>
					<tbody v-if="isEmpty">
						<tr>
							<td colspan="4">
								<Empty>没有项目信息</Empty>
							</td>
						</tr>
					</tbody>
					<tbody v-else>
						<tr v-for="item in models" :key="item.id">
							<td>
								<div style="max-width:480px">{{item.name}}</div>
							</td>
							<td>{{item.userName || "无"}}</td>
							<td>{{item.date || "无"}}</td>
							<td class="col-op">
								<div class="ops">
									<span class="op files" @click="onOperationHandler(item, 'files')">资料</span>
									<span class="op edit" @click="onOperationHandler(item, 'edit')">编辑</span>
									<span class="op delete" @click="onOperationHandler(item, 'delete')">删除</span>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="pager">
				<el-pagination :page-size="pageSize" :current-page="pageNo" :total="totalCount"
					@current-change="onPageChangeHandler"></el-pagination>
			</div>
		</template>
		<ProjectEditor v-model="projectDialogVisible" :model="currentEditProject"
			@submit="onProjectSubmitHandler"></ProjectEditor>
		<!-- <div v-if="loadingFlag" class="u-loading"></div> -->
	</div>
</template>

<script>
	import _ from "lodash";
	import Empty from "../../components/common/Empty";
	import ProjectEditor from "./ProjectEditor";

	export default {
		props: ["category"],
		data () {
			return {
				models: [],

				formData: {
					name: ""
				},

				pageInfo: null,
				loadingFlag: false,

				currentEditProject: null,
				projectDialogVisible: false
			};
		},
		mounted () {
			this.refresh();
		},
		watch: {
			category () {
				this.refresh();
			}
		},
		computed: {
			isEmpty () {
				return !this.models || this.models.length == 0;
			},
			pageNo () {
				return parseInt(this.pageInfo && this.pageInfo.page) || 1;
			},
			pageSize () {
				return parseInt(this.pageInfo && this.pageInfo.size) || 20;
			},
			totalCount () {
				return parseInt(this.pageInfo && this.pageInfo.total) || 0;
			}
		},
		methods: {
			refresh () {
				this.models = [];
				this.loadData(1);
			},
			loadData (page) {
				if (!this.category)
					return ;
				this.loadingFlag = true;
				var params = {page: page, limit: 10};
				params.numberId = this.category.id;
				params.name = this.formData.name || "";
				this._$get("/project/project/list", params, (datas, pageInfo) => {
					this.loadingFlag = false;
					this.models = _.map(datas, (data) => {
						data.date = this._$formatDate(data.creatTime);
						return data;
					});
					this.pageInfo = pageInfo;
					if (this.models.length == 0 && page > 1) {
						this.loadData(page - 1);
					}
				}, () => {
					this.loadingFlag = false;
				});
			},

			doSearch () {
				this.refresh();
			},

			onPageChangeHandler (page) {
				this.loadData(page);
			},

			onCreateBtnHandler () {
				this.currentEditProject = {};
				this.currentEditProject.lineId = this.category.id;
				this.projectDialogVisible = true;
			},

			onOperationHandler (data, type) {
				if (type == "edit") {
					this.currentEditProject = Object.assign({}, data);
					this.projectDialogVisible = true;
				}
				else if (type == "files") {
					window.location = `/project/${data.id}/files`;
				}
				else if (type == "delete") {
					this.$confirm("删除项目将同时删除该项目下所有资料，确认删除该项目吗？", "删除项目").then(() => {
						this.loadingFlag = true;
						this._$get("/project/project/delete", {id: data.id}, () => {
							this.loadingFlag = false;
							this._$showMsg("删除成功", "success");
							this.loadData(this.formData.page);
						}, () => {
							this.loadingFlag = false;
						});
					}).catch(() => {});
				}
			},

			onProjectSubmitHandler (data) {
				if (this.currentEditProject && this.currentEditProject.id) {
					var item = _.find(this.models, (tmp) => (tmp.id == this.currentEditProject.id));
					item.name = data.name;
				}
				else {
					this.formData.name = "";
					this.refresh();
				}
			}
		},
		components: {
			Empty,
			ProjectEditor
		}
	}
</script>

<style lang="scss">
.project-list {
	position: relative;
	width: 100%;
    min-height: 400px;
    padding: 24px 0px 0px;

    .topbar {
    	position: relative;
    	margin-bottom: 20px;

    	.u-searchipt {
    		input {
    			width: 220px;
    		}

    		button {
    			pointer-events: initial;
    			opacity: 0.5;
    		}

    		button:hover {
    			opacity: 1;
    		}
    	}

	    .createbtn {
	        position: absolute;
		    width: 100px;
		    height: 36px;
		    top: 0px;
		    right: 0px;
		}
	}

	.listview {
		.m-empty {
			padding: 130px 0px 50px;
    		background-size: 80px;
		}
	}

	.pager {
		padding: 20px 0px 30px;
	}

	> .m-empty {
		color: #ccc;
		font-size: 20px;

		.sub {
	    	color: #ddd;
			font-size: 14px;
		}
	}
}
</style>