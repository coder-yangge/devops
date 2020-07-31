layui.config({
    base: '../../js/'
}).use(['sliderVerify', 'jquery', 'form'], function() {
    var sliderVerify = layui.sliderVerify,
        $ = layui.jquery,
        form = layui.form;
    var slider = sliderVerify.render({
        elem: '#slider',
        onOk: function(){//当验证通过回调
            layer.msg("滑块验证通过");
        }
    })
    $('#reset').on('click',function(){
        slider.reset();
    })
    //监听提交
    form.on('submit(login)', function(data) {
        if(slider.isOk()){
            // layer.msg(JSON.stringify(data.field));
            $.ajax({
                type: "POST",
                url: "http://localhost:8888/devops/account/login",
                data: JSON.stringify(data.field),
                contentType: 'application/json',
                success: function (data) {
                    if (data.success) {
                        window.location.href="/devops/"
                    }
                }
            });
        }else{
            layer.msg("请先通过滑块验证");
        }
        return false;
    });

});

layui.use(['form','layer','jquery'],function(){

    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer
    $ = layui.jquery;

    $(".loginBody .seraph").click(function(){
        layer.msg("这只是做个样式，至于功能，你见过哪个后台能这样登录的？还是老老实实的找管理员去注册吧",{
            time:5000
        });
    })

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    })
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    })
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    })
})
