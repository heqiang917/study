<!-- 模块信息 -->

<template>
	<div class="project-modules">
		<div class="root" :active="isRootActive" @click="onItemClickHandler(null)">
			<i></i>全部（{{treeData.number}}）
		</div>
		<ul v-if="treeData">
			<li v-for="a in treeData.children" level="1" :leaf="!a.children" :key="a.id" :open="a.open" :active="a.active">
				<div class="item" @click="onItemClickHandler(a)">
					<span class="expand" @click="onExpandHandler($event, a)">&nbsp;</span>
					<span>{{a.name}}（{{a.number}}）</span>
				</div>
				<ul v-if="a.children">
					<li v-for="b in a.children" level="2" :leaf="!b.children" :key="b.id" :open="b.open" :active="b.active">
						<div class="item" @click="onItemClickHandler(b)">
							<span class="expand" @click="onExpandHandler($event, b)">&nbsp;</span>
							<span>{{b.name}}（{{b.number}}）</span>
						</div>
						<ul v-if="b.children">
							<li v-for="c in b.children" level="3" :leaf="true" :key="c.id" :active="c.active">
								<div class="item" @click="onItemClickHandler(c)">
									<span class="expand" @click="onExpandHandler($event, c)">&nbsp;</span>
									<span>{{c.name}}（{{c.number}}）</span>
								</div>
							</li>
						</ul>
					</li>
				</ul>
			</li>
		</ul>
	</div>
</template>

<script>
	import _ from "lodash";
	import EventBus from "../../eventBus";

	export default {
		props: ["project", "value"],
		data () {
			return {
				models: [],
				treeData: {},
				isRootActive: true,
				currentActiveData: null
			};
		},
		mounted () {
			EventBus.$on("file-created", this.onFileCreatedHandler.bind(this));
			EventBus.$on("file-deleted", this.onFileDeletedHandler.bind(this));
			// EventBus.$on("dir-changed", this.onDirChangedHandler.bind(this));
			this.initModels();
		},
		watch: {
			value (newval) {
				if (!newval) {
					this.setActive(null);
				}
				else {
					var data = _.find(this.models, (tmp) => (tmp.id == newval.id));
					this.setActive(data);
				}
			}
		},
		methods: {
			initModels () {
				if (!this.project)
					return ;
				this._$get(`/model/list?projectId=${this.project.id}`, (models) => {
					var root = {id: 0, label: "全部文件", pid: -1, number: 0, children: []};
					
					var format = (item, datas, beOpen) => {
						if (datas && datas.length > 0) {
							var children = item.children = [];
							_.each(datas, (data) => {
								var _item = {id: data.id, name: data.name, pid: item.id};
								_item.number = parseInt(data.number) || 0;
								// _item.label = `${_item.name}（${_item.number}）`;
								_item.open = beOpen;
								_item.active = false;
								children.push(_item);
								this.models.push(_item);
								format(_item, data.list, false);
							});
							item.number = 0;
							_.each(children, (tmp) => {
								item.number += tmp.number;
							});
						}
					};
					format(root, models, true);

					this.treeData = root;

					window.g_projectModules = this.models;
				});
			},

			onExpandHandler (event, data) {
				event.stopPropagation();
				data.open = !data.open;
			},

			onItemClickHandler (data) {
				this.setActive(data);
				this.$emit("input", data);
			},

			onFileCreatedHandler (data) {
				if (data.fileType == "folder")
					return ;
				var moduleId = data.modelId;
				while (true) {
					var model = _.find(this.models, (tmp) => (tmp.id == moduleId));
					if (model) {
						model.number += 1;
						if (model.pid >= 0) {
							moduleId = model.pid;
							continue;
						}
					}
					break ;
				}
				this.treeData.number += 1;
			},

			onFileDeletedHandler (data) {
				if (data.type == "dir") {
					this.initModels();
				}
				else {
					var moduleId = data.moduleId;
					while (true) {
						var model = _.find(this.models, (tmp) => (tmp.id == moduleId));
						if (model) {
							model.number -= 1;
							if (model.pid >= 0) {
								moduleId = model.pid;
								continue;
							}
						}
						break;
					}
					this.treeData.number -= 1;
				}
			},

			onDirChangedHandler (data) {
				if (data) {
					if (this.currentActiveData) {
						if (this.currentActiveData.id == data.moduleId)
							return ;
					}
					var model = _.find(this.models, (tmp) => (tmp.id == data.moduleId));
					if (model) {
						this.setActive(model);
						this.$emit("input", model);
						while (model && model.pid > 0) {
							model = _.find(this.models, (tmp) => (tmp.id == model.pid));
							if (model) {
								model.open = true;
							}
						}
					}
				}
			},

			setActive (data) {
				if (data) {
					if (this.currentActiveData) {
						if (this.currentActiveData.id == data.id)
							return ;
						this.currentActiveData.active = false;
					}
					data.active = true;
					this.currentActiveData = data;
					this.isRootActive = false;
				}
				else if (!this.isRootActive) {
					this.isRootActive = true;
					if (this.currentActiveData) {
						this.currentActiveData.active = false;
						this.currentActiveData = null;
					}
				}
			}
		}
	}
</script>

<style lang="scss">
.project-modules {
	color: #787878;
    font-size: 14px;

    .root {
    	position: relative;
	    padding-left: 60px;
	    font-size: 16px;
	    line-height: 40px;
	    border-bottom: 1px solid #eee;
	    background-color: #f6f6f6;

	    i {
	    	position: absolute;
		    width: 20px;
		    height: 20px;
		    left: 30px;
		    top: 50%;
		    margin-top: -10px;
		    background-image: url(/images/menu.png);
		    background-size: 20px;
		    background-repeat: no-repeat;
		    background-position: center;
		}
	}

	.root[active] {
		color: #0689ea;
		background-color: rgba(6, 137, 234, 0.08);
		cursor: default;
	}

	.item {
		position: relative;
		padding: 6px 10px 6px 30px;
	    line-height: 20px;
	    transition: background-color 0.4s;
	    cursor: default;

	    .expand {
	    	position: absolute;
		    width: 20px;
		    height: 20px;
		    left: 5px;
		    top: 50%;
		    margin-top: -10px;
		    cursor: pointer;
		}

		.expand:before {
			content: "";
		    position: absolute;
		    left: 7px;
		    top: 6px;
		    border: 0px solid transparent;
		    border-width: 4px 0px 4px 6px;
		    border-left-color: #ccc;
    		transition: all 0.25s;
		}

		.expand:hover:before {
			border-left-color: #999;
		}
	}

	.item:hover {
		background-color: #eee;
	}

	li > ul {
		display: none;
	}

	li[open] {
		> .item .expand:before {
			transform: rotate(90deg);
		}

		> ul {
			display: block;
		}
	}

	li[leaf] > .item {
		.expand {
			display: none;
		}
	}

	li[active] > .item {
		color: #0689ea;
		background-color: rgba(6, 137, 234, 0.08);
	}

	li[level='2'] > .item {
		padding-left: 45px;

		.expand {
			left: 20px;
		}
	}

	li[level='3'] > .item {
		padding-left: 60px;

		.expand {
			left: 35px;
		}
	}
}
</style>