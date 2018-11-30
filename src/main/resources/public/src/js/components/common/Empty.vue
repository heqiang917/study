<template>
	<div class="m-empty">
        <slot>这里还木有内容哦</slot>
    </div>
</template>

<script>
	export default {
        name: 'my-empty',
		props: ["value"],
		data () {
			return {};
		}
	}
</script>

<style lang="scss">
	.m-empty {
	    padding: 160px 20px 80px;
		color: #8798b5;
	    font-size: 14px;
	    text-align: center;
	    background-image: url(/images/empty.png);
	    background-size: 100px;
	    background-repeat: no-repeat;
	    background-position: center 40px;
	    -webkit-font-smoothing: antialiased;
	}
</style>
