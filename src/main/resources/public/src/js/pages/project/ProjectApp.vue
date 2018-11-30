<!-- 项目主页 -->

<template>
	<MainFrame class="m-project">
		<div ref="projectContainer" class="project-container">
			<div class="sidemenu">
				<ProjectMenu @change="onMenuChangeHandler"></ProjectMenu>
			</div>
			<div class="container">
				<div v-if="category" class="title">{{projectTitle}}</div>
				<ProjectList :category="category"></ProjectList>
			</div>
		</div>
	</MainFrame>
</template>

<script>
	import MainFrame from "../../components/frame/MainFrame";
	import ProjectMenu from "./ProjectMenu";
	import ProjectList from "./ProjectList";

	export default {
		props: ["options"],
		data () {
			return {
				category: null
			};
		},
		mounted () {
			var container = this.$refs["projectContainer"];
			container.style.minHeight = `${container.parentNode.offsetHeight}px`;
		},
		computed: {
			projectTitle () {
				if (this.category)
					return `${this.category.province}-${this.category.city}（${this.category.line}号线）`;
				return "无";
			}
		},
		methods: {
			onMenuChangeHandler (data) {
				this.category = data;
			}
		},
		components: {
			MainFrame,
			ProjectMenu,
			ProjectList
		}
	}
</script>

<style lang="scss">
.project-container {
	display: flex;
	min-height: 600px;

    > .sidemenu {
	    flex: none;
	    overflow: auto;
	    width: 200px;
	    border-right: 1px solid #eee;
	}

	> .container {
		flex: auto;
		position: relative;
		overflow: auto;
		padding: 55px 30px 0px;

		> .title {
			position: absolute;
		    left: 30px;
		    right: 30px;
		    top: 0px;
			line-height: 55px;
    		border-bottom: 1px solid #eee;
    		background-color: #fff;
		}
	}
}
</style>