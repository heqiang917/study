import BaseModule from "../../baseModule";
import App from './App';
import '../../../scss/pages/index.scss';

class Main extends BaseModule {};

new Main().init({
    el: ".app-container",
    template: "<app :options='options'></app>",
    data() {
        return {
            options: {}
        };
    },
    components: { App }
});