<!-- 设置 -->

<template>
	<MainFrame class="m-settings">
		<div class="settings-container">
			<SettingsMenu></SettingsMenu>
			<div class="settings-content">
				<slot></slot>
			</div>
		</div>
	</MainFrame>
</template>

<script>
	import MainFrame from "../../components/frame/MainFrame";
	import SettingsMenu from "./SettingsMenu";

	export default {
		data () {
			return {};
		},
		components: {
			MainFrame,
			SettingsMenu
		}
	}
</script>

<style lang="scss">
.settings-container {
	display: flex;
    align-items: flex-start;

    > div {
    	flex: none;
	}

	.settings-content {
		flex: auto;
		padding: 30px 30px 50px;
	}
}
</style>