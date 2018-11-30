<!-- 主页 -->

<template>
    <MainFrame :show-title="false"></MainFrame>
</template>

<script>
    import MainFrame from "../../components/frame/MainFrame";

    export default {
        props: ["options"],
        data () {
            return {};
        },
        mounted () {
        },
        components: {
            MainFrame
        }
    }
</script>

<style scoped>
</style>
