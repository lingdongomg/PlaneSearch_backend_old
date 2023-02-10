let app=new Vue({
    el:'#app',
    data:{
        username:'',
        password:'',
        nickname:'',
        age:'',
        msg:''
    },methods:{
        reg:function () {
            console.log(this.username);
            console.log(this.password);
            console.log(this.nickname);
            console.log(this.age);
            let data ={
                username:this.username,
                password:this.password,
                nickname:this.nickname,
                age:this.age,
                user:{
                    username1:'11',
                    age1:20
                }
            }
            console.log("11")
            $.ajax({
                url:'/hello',
                method:'POST',
                data:data,
                success:function (r) {
                    console.log("ajax注册成功");
                    app.msg="略略略";
                }
            })
        }
    }
});