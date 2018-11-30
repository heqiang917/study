// import WangeEitor from 'wangeditor';
import WangeEitor from '../wangeditor/dist/js/wangEditor.js';
import $ from 'jquery';
// 用 createMenu 方法创建菜单
let menu = null;
let menuId = 'fontsizeX';
WangeEitor.createMenu(function(check) {

    // 定义菜单id，不要和其他菜单id重复。编辑器自带的所有菜单id，可通过『参数配置-自定义菜单』一节查看


    // check将检查菜单配置（『参数配置-自定义菜单』一节描述）中是否该菜单id，如果没有，则忽略下面的代码。
    if (!check(menuId)) {
        return;
    }

    // this 指向 editor 对象自身
    var editor = this;


    editor.commandHooks.fontsizeX = (value) => {
        //    var range= editor.currentRange();
        var rangeElem = editor.getRangeElem();

        // console.log(value);
        var targetElem = editor.getSelfOrParentByName(rangeElem, 'p,span,h1,h2,h3,h4,h5,pre', function(elem) {

            $(elem).css('font-size', value + 'px');
            // console.log(elem);
        });
        if (!targetElem) {
            $(rangeElem).children("p").each(function(index, item) {
                if (window.getSelection) {
                    let selection = window.getSelection();
                    if (selection.containsNode(item, true)) {
                        $(item).css('font-size', value + 'px');
                    };
                }
                // console.log(range.isPointInRange(item));
            });

            return;
        }

    };


    // 创建 menu 对象
    menu = new WangeEitor.Menu({
        editor: editor, // 编辑器对象
        id: menuId, // 菜单id
        title: '字号', // 菜单标题
        commandName: 'fontsizeX', // 命令名称

        // 正常状态和选中装下的dom对象，样式需要自定义
        $domNormal: $('<a href="#" tabindex="-1"><i class="wangeditor-menu-img-text-height"></i></a>'),
        $domSelected: $('<a href="#" tabindex="-1" class="selected"><i class="wangeditor-menu-img-text-height"></i></a>')
    });
    // 数据源
    let data = {
        // 格式： 'value' : 'title'
        12: '12px',
        14: '14px',
        16: '16px',
        18: '18px',
        24: '24px',
        32: '32px',
        48: '48px'
    };



    // 为menu创建droplist对象
    menu.dropList = new WangeEitor.DropList(editor, menu, {
        data: data, // 传入数据源
        tpl: '<span style="font-size:{#commandValue}px">{#title}</span>' // 传入模板
    });

    // 增加到editor对象中
    editor.menus[menuId] = menu;
});
export default menu;