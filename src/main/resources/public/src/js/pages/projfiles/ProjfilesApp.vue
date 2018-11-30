<!-- 项目文件管理 -->

<template>
	<MainFrame class="m-project-files">
		<div ref="projectContainer" class="project-container">
			<div v-if="!projectInfo" class="empty">
				<p>没有项目信息</p>
			</div>
			<template v-else>
				<div class="modules">
					<ProjectModules v-model="projectModule" :project="projectInfo"></ProjectModules>
				</div>
				<div class="container">
					<ProjectFiles :project="projectInfo" :project-module="projectModule"
						@module-reset="onModuleResetHandler"></ProjectFiles>
				</div>
			</template>
		</div>
	</MainFrame>
</template>

<script>
	import MainFrame from "../../components/frame/MainFrame";
	import ProjectModules from "./ProjectModules";
	import ProjectFiles from "./ProjectFiles";

	export default {
		props: ["options"],
		data () {
			var pageData = window.g_pageData || {};
			return {
				projectInfo: pageData.projectInfo,
				projectModule: null
			};
		},
		mounted () {
			var container = this.$refs["projectContainer"];
			container.style.minHeight = `${container.parentNode.offsetHeight}px`;
		},
		methods: {
			onModuleResetHandler () {
				this.projectModule = null;
			}
		},
		components: {
			MainFrame,
			ProjectModules,
			ProjectFiles
		}
	}
</script>

<style lang="scss">
.project-container {
	display: flex;

	> .empty {
		flex: auto;
	    padding-top: 200px;
	    color: #999;
	    text-align: center;
	    background-image: url(/images/empty.png);
	    background-size: 100px;
	    background-repeat: no-repeat;
	    background-position: center 80px;
	}

	> .modules {
		flex: none;
	    width: 320px;
	    border-right: 1px solid #eee;
	    background-color: #fafafa;
	}

	> .container {
		flex: auto;
	}
}
</style>