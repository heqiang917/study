<!-- 主页面框架 -->

<template>
	<div class="g-doc g-doc-frm">
		<div class="g-hd">
			<div class="main-header">
				<div class="content">
					<div class="slogo">
						<a href="/">轨道交通全作业平台</a>
					</div>
					<div class="menus">
						<ul>
							<li class="menu" name="project" :active="isActive('project')">
								<a href="/project"><span>项目</span></a>
							</li><li class="menu" name="achieve" :active="isActive('achieve')">
								<a href="/achieve"><span>业绩库</span></a>
							</li>
						</ul>
					</div>
					<div class="searchbar">
						<div class="u-searchipt">
							<input v-model="searchText" placeholder="资料查询" @keydown.enter="onSearchHandler"/>
							<button>&nbsp;</button>
						</div>
					</div>
					<div class="user">
						<div class="info">
							<i>&nbsp;</i>
							<span class="name">{{userName}}</span>
						</div>
						<div class="dropdown">
							<div class="item"><a href="/settings">设置</a></div>
							<div class="item"><a @click="onExitBtnHandler">退出</a></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="g-bd">
			<div ref="mainContainer" class="main-container">
				<slot></slot>
			</div>
		</div>
		<UploadViewer></UploadViewer>
	</div>
</template>

<script>
	import UploadViewer from "../upload/UploadViewer";

	export default {
		props: ["options"],
		data () {
			return {
				menus: [],
				searchText: ""
			};
		},
		mounted () {
			this.$refs["mainContainer"].style.minHeight = `${window.innerHeight - 130}px`;

			window.onbeforeunload = this.onWinCloseHandler.bind(this);
		},
		computed: {
			userName () {
				var user = window["g_userinfo"] || {};
				return user.realName || user.email || "未知用户";
			},
			currentMenu () {
				var pathname = window.location.pathname || "";
				return pathname.split("/")[1];
			}
		},
		methods: {
			isActive (name) {
				if (this.currentMenu) {
					return this.currentMenu == name;
				}
				return name == "project";
			},

			onSearchHandler () {
				window.location = `/files/query?t=${this.searchText}`;
			},

			onExitBtnHandler () {
				this._$get("/user/login/out", () => {
					window.location.href = "/login";
				});
			},

			onWinCloseHandler (e) {
				if (window.g_fileuploading) {
					e = window.event || e;
					e.returnValue = "文件正在上传，离开页面将中断上传过程，数据可能丢失";
				}
			}
		},
		components: {
			UploadViewer
		}
	};
</script>