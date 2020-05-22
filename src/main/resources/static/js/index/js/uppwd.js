layui.use(['form','element','jquery','upload'], function(){
  var form = layui.form;
  var element = layui.element; 
  var $ = layui.jquery;
  var upload = layui.upload;
  
  //监听提交
  form.on('submit(changePwd)', function(data){
	  if(data.field.newpwd == data.field.newpwds){
		  $.ajax({
		 		 url:"/login/editpwd",
		 		 data:data.field,
		 		 type:'post',
		 		 dataType:'json',
		 		 success:function(value){
		 			if(value == "0"){
		 				$("#pwd").val("");
		 				$("#newpwd").val("");
		 				$("#newpwds").val("");
		 				layer.msg("密码修改成功！", {icon: 1});
		 			}else{
		 				layer.msg(value, {icon: 2});
		 			}
		 		 }
		   }); 
	  }else{
		  layer.msg('两次密码输入不一致！', {icon: 2});
	  }
    return false;
  });
})