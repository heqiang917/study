<!-- 项目地区和线路 -->

<template>
	<div class="project-menu">
		<div class="menu-grp" v-for="grp in categorys" :open="grp.open">
			<div class="title" @click="onMenuGrpHandler(grp)">
				<span>{{grp.province}}-{{grp.city}}</span>
				<div class="ops">
					<span class="op edit" @click="onGroupEditHandler($event, grp)">&nbsp;</span>
					<span class="op delete" @click="onDeleteHandler($event, grp)">&nbsp;</span>
				</div>
			</div>
			<ul class="menus">
				<li class="menu" v-for="item in grp.children" :active="item.active" 
					@click="onMenuItemHandler(grp, item)">
					<div class="title">{{item.line}}号线</div>
					<div class="ops">
						<span class="op edit" @click="onLineEditHandler($event, grp, item)">&nbsp;</span>
						<span class="op delete" @click="onDeleteHandler($event, grp, item)">&nbsp;</span>
					</div>
				</li>
				<li class="addbtn" @click="onLineEditHandler($event, grp)">新增线路</li>
			</ul>
		</div>
		<div class="addbtn">
			<button class="u-btn primary" @click="onGroupEditHandler($event)">新增地区</button>
		</div>

		<DistrictEditor v-model="groupDialogVisible" :model="currentEditGroup"
			@submit="onGroupSubmitHandler"></DistrictEditor>
		<LineEditor v-model="lineDialogVisible" :model="currentEditLine"
			@submit="onLineSubmitHandler"></LineEditor>
	</div>
</template>

<script>
	import _ from "lodash";
	import DistrictEditor from "./DistrictEditor";
	import LineEditor from "./LineEditor";

	export default {
		data () {
			return {
				categorys: [],

				currentEditGroup: null,
				groupDialogVisible: false,

				currentEditLine: null,
				lineDialogVisible: false
			};
		},
		mounted () {
			this.loadCitys();
		},
		methods: {
			loadCitys () {
				this._$get("/project/city/list", (data) => {
					this.categorys = _.map(data, (temp) => {
						return {id: temp.id, province: temp.province, city: temp.city, open: false, children: null};
					});
					if (this.categorys.length > 0) {
						// this.categorys[0].open = true;
						this.loadLines(this.categorys[0], true);
					}
				});
			},

			loadLines (city, activeFirst) {
				if (city.children)
					return ;
				city.open = true;
				city.children = [];
				this._$get("/project/number/list", {cityId: city.id}, (data) => {
					city.children = _.map(data, (temp) => {
						return {id: temp.id, line: temp.number, active: false, cityId: city.id};
					});
					if (activeFirst && city.children.length > 0) {
						this.setItemActive(city.children[0]);
					}
				});
			},

			///////////////////////////////////////////////
			onMenuGrpHandler (data) {
				if (!data.children) {
					this.loadLines(data);
				}
				else {
					data.open = !data.open
				}
			},

			onMenuItemHandler (group, data) {
				this.setItemActive(data);
			},

			onGroupEditHandler (event, group) {
				event.stopPropagation();
				this.currentEditGroup = Object.assign({}, group);
				this.groupDialogVisible = true;
			},

			onGroupSubmitHandler (data) {
				if (this.currentEditGroup.id) {
					var group = _.find(this.categorys, (tmp) => (tmp.id == this.currentEditGroup.id));
					group.province = data.province;
					group.city = data.city;
				}
				else {
					this.categorys.push({id: data.id, province: data.province, city: data.city, open: true, children: []});
				}
			},

			onLineEditHandler (event, group, line) {
				event.stopPropagation();
				// this.currentEditGroup = group;
				this.currentEditLine = Object.assign({}, line);
				this.currentEditLine.cityId = group.id;
				this.lineDialogVisible = true;
			},

			onLineSubmitHandler (data) {
				var group = _.find(this.categorys, (tmp) => (tmp.id == this.currentEditLine.cityId));
				if (this.currentEditLine.id) {
					var item = _.find(group.children, (tmp) => (tmp.id == this.currentEditLine.id));
					item.line = data.line;
				}
				else {
					group.children.push({id: data.id, line: data.number, cityId: group.id, active: false});
					if (!this.getActiveItem())
						this.setItemActive(group.children[0]);
				}
			},

			onDeleteHandler (event, group, data) {
				event.stopPropagation();
				if (!data) {
					this.$confirm("移除地区将同时删除该地区下所有项目及资料，确认移除该地区吗？", "移除地区").then(() => {
						this._$get("/project/city/delete", {id: group.id}, () => {
							for (var i = this.categorys.length - 1; i >= 0; i--) {
								if (this.categorys[i].id == group.id) {
									this.categorys.splice(i, 1);
									break;
								}
							}
							this.$nextTick(() => {
								if (!this.getActiveItem())
									this.setItemActive(null);
							});
						});
					}).catch(() => {});
				}
				else {
					this.$confirm("删除线路将同时删除该线路下所有项目及资料，确认删除该线路吗？", "删除线路").then(() => {
						this._$get("/project/number/delete", {id: data.id}, () => {
							var group = _.find(this.categorys, (tmp) => (tmp.id == data.cityId));
							var children = group.children || [];
							for (var i = children.length - 1; i >= 0; i--) {
								if (children[i].id == data.id) {
									children.splice(i, 1);
									break;
								}
							}
							if (data.active) {
								this.setItemActive({});
							}
						});
					}).catch(() => {});
				}
			},

			///////////////////////////////////////////////
			getActiveItem () {
				for (var i = 0; i < this.categorys.length; i++) {
					var children = this.categorys[i].children || [];
					for (var j = 0; j < children.length; j++) {
						if (children[j].active)
							return children[j];
					}
				}
				return null;
			},

			setItemActive (data) {
				if (data && data.active) 
					return ;

				var activeItem = this.getActiveItem();
				if (data == activeItem)
					return ;

				if (activeItem)
					activeItem.active = false;

				if (data && data.id) {
					data.active = true;
					var group = _.find(this.categorys, (tmp) => (tmp.id == data.cityId));
					var params = {id: data.id, cityId: data.cityId, province: group.province, 
						city: group.city, line: data.line};
					this.$emit("change", params);
				}
				else {
					this.$emit("change");
				}
			}
		},
		components: {
			DistrictEditor,
			LineEditor
		}
	}
</script>

<style lang="scss">
.project-menu {
	> .addbtn {
    	padding: 20px 30px;
    	button {
	    	width: 100%;
		    height: 36px;
	    }
	}

	.ops {
		display: none;
		position: absolute;
	    height: 20px;
	    top: 50%;
	    right: 5px;
	    margin-top: -10px;
	    line-height: 20px;

	    .op {
	    	display: inline-block;
		    width: 20px;
		    height: 20px;
		    border-radius: 3px;
		    background-color: #fff;
		    background-size: 16px;
		    background-repeat: no-repeat;
		    background-position: center;
		    cursor: pointer;
		    opacity: 0.5;
		}

		.op:hover {
			opacity: 1;
		}

		.op.edit {
			background-image: url(/images/edit.png);
		}

		.op.delete {
			background-image: url(/images/delete.png);
		}
	}

	.menu-grp {
		> .title {
			position: relative;
		    padding-left: 10px;
		    padding: 9px 10px;
		    line-height: 21px;
		    border-bottom: 1px solid #eee;
		    background-color: #fafafa;
		    cursor: default;
		}

		> .title:hover {
			background-color: #eee;

			.ops {
				display: block;
			}
		}

		.menus {
			overflow: auto;
			max-height: 0px;
			transition: max-height 0.3s;

			.menu {
				position: relative;
				color: #787878;
			    font-size: 14px;
			    transition: background-color 0.2s;
			}

			.menu:hover {
				background-color: #f0f0f0;

				.ops {
					display: block;
				}
			}

			.menu:active {
				background-color: #eee;
			}

			.menu[active] {
				color: #fff;
				background-color: #0689ea;
			}

			.title {
				position: relative;
			    padding-left: 25px;
			    line-height: 30px;
			    cursor: pointer;
			}
		}

		.op {
			background-size: 14px;
		}

		.addbtn {
			position: relative;
			padding-left: 40px;
		    color: #37a2f7;
		    font-size: 14px;
		    line-height: 32px;
		    cursor: pointer;
		}

		.addbtn:before {
			content: "";
			position: absolute;
		    width: 20px;
		    height: 20px;
		    left: 20px;
		    top: 50%;
		    margin-top: -10px;
		    background-image: url(/images/add.png);
		    background-size: 10px;
		    background-repeat: no-repeat;
		    background-position: center;
		}

		.addbtn:active {
			background-color: #f0f0f0;
		}
	}

	.menu-grp:first-child {
		border-top: 0px;
	}

	.menu-grp[open] {
		> .menus {
			max-height: 350px;
		}
	}

	.menu-grp[open] + .menu-grp {
		border-top: 1px solid #eee;
	}
}
</style>