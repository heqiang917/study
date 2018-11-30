<!-- 资料查询 -->

<template>
	<MainFrame class="m-files-query">
		<div class="files-container">
			<div class="searchbar">
				<div class="area">
					<dt>地区</dt>
					<dd>
						<select v-model="formData.province" class="u-select">
							<option value="" selected="selected">选择省</option>
							<option v-for="item in provinces" :value="item.id">{{item.label}}</option>
						</select>
						<select v-model="formData.city" class="u-select">
							<option value="" selected="selected">选择市</option>
							<option v-for="item in citys" :value="item.id">{{item.label}}</option>
						</select>
					</dd>
				</div>
				<div class="line">
					<dt>线路</dt>
					<dd>
						<input v-model="formData.line" class="u-input" type="text" @keydown.enter="doSearch" />
					</dd>
				</div>
				<div class="module">
					<dt>模块</dt>
					<dd>
						<select v-model="formData.module1" class="u-select">
							<option value="" selected="selected">选择阶段</option>
							<option v-for="item in modules1" :value="item.id">{{item.name}}</option>
						</select>
						<select v-model="formData.module2" class="u-select">
							<option value="" selected="selected">选择环节</option>
							<option v-for="item in modules2" :value="item.id">{{item.name}}</option>
						</select>
						<select v-model="formData.module3" class="u-select">
							<option value="" selected="selected">选择子环节</option>
							<option v-for="item in modules3" :value="item.id">{{item.name}}</option>
						</select>
					</dd>
				</div>
				<div class="keyword">
					<dt>关键字</dt>
					<dd>
						<div class="u-searchipt">
							<input v-model="formData.keyword" type="text" @keydown.enter="doSearch" />
							<button>&nbsp;</button>
						</div>
						<button class="searchbtn u-btn primary" @click="doSearch">查询</button>
						<button class="resetbtn u-btn" @click="onResetHandler">重置</button>
					</dd>
				</div>
			</div>
			<FileList ref="fileList"></FileList>
		</div>
	</MainFrame>
</template>

<script>
	import _ from "lodash";
	import CHINA_REGION from 'china-area-data';
	import MainFrame from "../../components/frame/MainFrame";
	import FileList from "./FileList";

	export default {
		props: ["options"],
		data () {
			var pageData = window.g_pageData || {};
			return {
				formData: {
					province: "",
					city: "",
					line: "",
					module1: "",
					module2: "",
					module3: "",
					keyword: pageData.searchText || ""
				},

				provinces: [],
				citys: [],

				modules: [],
				modules1: [],
				modules2: [],
				modules3: []
			};
		},
		mounted () {
			var provinces = CHINA_REGION["86"];
			for (var code in provinces) {
				this.provinces.push({id: code, label: provinces[code]});
			}

			this.initModules();

			this.doSearch();
		},
		watch: {
			"formData.province" (newval) {
				this.citys = [];
				this.formData.city = "";
				if (newval) {
					var citys = CHINA_REGION[newval];
					for (var code in citys) {
						this.citys.push({id: code, label: citys[code]});
					}
				}
				this.doSearch();
			},
			"formData.city" (newval) {
				this.doSearch();
			},
			"formData.line" (newval) {
				if (newval) {
					var value = parseInt(newval) || 0;
					if (value > 0) {
						if (value != newval)
							this.formData.line = value.toFixed(0);
					}
					else {
						this.formData.line = "";
					}
				}
			},
			"formData.module1" (newval) {
				this.modules2 = [];
				this.modules3 = [];
				this.formData.module2 = "";
				this.formData.module3 = "";
				if (newval) {
					this.modules2 = _.filter(this.modules, (tmp) => (tmp.pid == newval));
				}
				this.doSearch();
			},
			"formData.module2" (newval) {
				this.modules3 = [];
				this.formData.module3 = "";
				if (newval) {
					this.modules3 = _.filter(this.modules, (tmp) => (tmp.pid == newval));
				}
				this.doSearch();
			},
			"formData.module3" (newval) {
				this.doSearch();
			}
		},
		methods: {
			initModules () {
				this._$get(`/model/list?projectId=0`, (datas) => {
					var loop = (_datas, _parent) => {
						_.each(_datas, (data) => {
							var _data = {id: data.id, name: data.name, pid: 0};
							this.modules.push(_data);
							if (_parent) {
								_data.pid = _parent.id;
							}
							else {
								this.modules1.push(_data);
							}
							if (data.list) {
								loop(data.list, data);
							}
						});
					};
					loop(datas);
				});
			},

			doSearch () {
				var params = Object.assign({}, this.formData);
				if (params.province) {
					params.province = _.find(this.provinces, (tmp) => (tmp.id == params.province));
					params.province = params.province.label;
				}
				if (params.city) {
					params.city = _.find(this.citys, (tmp) => (tmp.id == params.city));
					params.city = params.city.label;
				}
				this.$refs["fileList"].refresh(params);
			},

			onResetHandler () {
				this.formData.province = "";
				this.formData.city = "";
				this.formData.line = "";
				this.formData.module1 = "";
				this.formData.module2 = "";
				this.formData.module3 = "";
				this.formData.keyword = "";
				this.doSearch();
			}
		},
		components: {
			MainFrame,
			FileList
		}
	}
</script>

<style lang="scss">
.m-files-query {
	.main-header {
		.searchbar {
			display: none;
		}
	}
}
.files-container {
	.searchbar {
		padding: 0px 0px 10px;
		border-bottom: 1px solid #eee;
		background-color: #fafafa;

		> div {
			display: inline-block;
    		padding: 0px 0px 0px 10px;
    		vertical-align: top;
		}

		dt {
			padding-top: 5px;
			color: #999;
		    font-size: 14px;
		    line-height: 30px;
		}

		.u-select {
			background-color: #fff;
		}

		.area select {
			width: 100px;
		}

		.line input {
			width: 80px;
		    height: 36px;
		    padding: 0px 10px;
		    font-size: 16px;
		    border: 1px solid #ddd;
		    border-radius: 3px;
		    outline: none;
		}

		.module select {
			width: 180px;
		}

		.keyword {
			dd {
				> button {
					vertical-align: middle;
				    width: 65px;
				    height: 36px;
				}
			}
		}
	}
}
</style>