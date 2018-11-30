<!-- 设置菜单 -->

<template>
	<div class="settings-menu">
		<ul class="menus">
			<li v-if="isManager" class="menu" :active="isActive('account')">
				<a href="/settings/account">帐号管理</a>
			</li>
			<li class="menu" :active="isActive('repwd')">
				<a href="/settings/repwd">修改密码</a>
			</li>
		</ul>
	</div>
</template>

<script>
	export default {
		data () {
			return {
				user: window.g_userinfo
			};
		},
		computed: {
			userType () {
				return this.user && this.user.roleType || 0;
			},
			isManager () {
				return this.userType == 1;
			},
			currentMenu () {
				var paths = window.location.pathname.split("/");
				return paths[2] || "";
			}
		},
		methods: {
			isActive (menu) {
				if (this.currentMenu)
					return menu == this.currentMenu;
				return menu == "account";
			}
		}
	}
</script>

<style lang="scss">
.settings-menu {
    width: 200px;
    padding: 30px 35px;

    &:before {
	    content: "设置";
	    display: block;
	    font-size: 20px;
	    line-height: 30px;
	    margin-bottom: 10px;
	}

	.menu {
		padding: 10px 5px;
	}

	a {
		color: #999;
	    font-size: 16px;
	    line-height: 24px;
	    text-decoration: none;
	}

	a:hover {
		color: #383838;
	}

	.menu[active] a {
		color: #0689ea;
	}
}
</style>