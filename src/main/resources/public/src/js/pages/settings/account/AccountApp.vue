<!-- 帐户管理 -->

<template>
	<SettingsContainer>
		<div class="settings-account">
			<div class="top">
				<span class="title">帐号管理</span>
				<span>初始密码：zhcpa@123</span>
				<button class="addbtn u-btn primary" @click="onAddbtnHandler">添加</button>
			</div>
			<div class="listview">
				<table class="u-table">
					<thead>
						<tr>
							<th>邮箱</th>
							<th>姓名</th>
							<th>角色</th>
							<th class="col-op">操作</th>
						</tr>
					</thead>
					<tbody v-if="isEmpty">
						<tr>
							<td colspan="4">
								<Empty>没有帐户信息</Empty>
							</td>
						</tr>
					</tbody>
					<tbody v-else>
						<tr v-for="model in models" :key="model.id">
							<td>{{model.email}}</td>
							<td>{{model.realName}}</td>
							<td>{{model.roleName}}</td>
							<td class="col-op">
								<div class="ops">
									<span class="op edit" @click="onEditBtnHandler(model)">编辑</span>
									<span class="op delete" @click="onDeleteBtnHandler(model)">删除</span>
									<span class="op reset" @click="onResetBtnHandler(model)">重置密码</span>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div v-if="pageSize<totalCount" class="pager">
				<el-pagination :page-size="pageSize" :current-page="pageNo" :total="totalCount"
					@current-change="onPageChangeHandler"></el-pagination>
			</div>
		</div>
		<AccountEditor v-model="accountDialogVisible" :model="currentEditAccount"
			@submit="onAccountSubmitHandler"></AccountEditor>
	</SettingsContainer>
</template>

<script>
	import _ from "lodash";
	import Empty from "../../../components/common/Empty";
	import SettingsContainer from "../SettingsContainer";
	import AccountEditor from "./AccountEditor";

	export default {
		data () {
			return {
				models: [],
				pageInfo: null,

				currentEditAccount: null,
				accountDialogVisible: false
			};
		},
		mounted () {
			this.refresh();
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
				this.loadPageData(1);
			},

			loadPageData (page) {
				var params = {};
				this._$get("/user/list", params, (datas) => {
					this.models = _.map(datas, (data) => {
						data.roleName = data.roleType == 1 ? "管理员" : "普通成员";
						return data;
					});
				});
			},

			onAddbtnHandler () {
				this.currentEditAccount = {};
				this.accountDialogVisible = true;
			},

			onPageChangeHandler (page) {
				this.loadPageData(page);
			},

			onAccountSubmitHandler () {
				this.refresh();
			},

			onEditBtnHandler (data) {
				this.currentEditAccount = Object.assign({}, data);
				this.accountDialogVisible = true;
			},

			onDeleteBtnHandler (data) {
				this.$confirm("确认删除该账号吗？", "删除账号").then(() => {
					this._$post(`/user/delete?id=${data.id}`, () => {
						this._$showMsg("删除成功", "success");
						this.refresh();
					});
				}).catch(() => {});
			},

			onResetBtnHandler (data) {
				this.$confirm("重置密码后，密码将更改为初始密码，是否确认重置密码？", "重置密码").then(() => {
					this._$post(`/user/restpassword?id=${data.id}`, () => {
						this._$showMsg("密码已重置", "success");
						// this.refresh();
					});
				}).catch(() => {});
			}
		},
		components: {
			Empty,
			SettingsContainer,
			AccountEditor
		}
	}
</script>

<style lang="scss">
.settings-account {
	> .top {
	    position: relative;
		margin-bottom: 20px;
	    color: #999;
	    font-size: 14px;
    	line-height: 32px;

	    .title {
	    	margin-right: 10px;
		    color: #000;
		    font-size: 18px;
		}

		.addbtn {
			position: absolute;
			width: 80px;
		    top: 0px;
		    right: 0px;
		}
	}

	> .pager {
	    margin: 20px -5px 0px;
	}
}
</style>