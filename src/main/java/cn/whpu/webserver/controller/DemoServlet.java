package cn.whpu.webserver.controller;

import cn.whpu.webserver.vo.Student;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class DemoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        req.setCharacterEncoding("GBK");
        System.out.println(req.getParameter("username"));
        System.out.println(req.getParameter("age"));
        System.out.println(req.getParameter("nickname"));
        System.out.println(req.getParameter("password"));

        // 1.由于请求体数据有可能很大，所以Servlet标准在设计API的时候要求我们通过输入流来读取
        BufferedReader reader = req.getReader();

        // 2.创建StringBuilder对象来累加存储从请求体中读取到的每一行
        StringBuilder builder = new StringBuilder();

        // 3.声明临时变量
        String bufferStr = null;
        System.out.println("1");
        // 4.循环读取
        while((bufferStr = reader.readLine()) != null) {
            builder.append(bufferStr);
            System.out.println(builder);
        }
        System.out.println("2");
        // 5.关闭流
        reader.close();

        // 6.累加的结果就是整个请求体
        String requestBody = builder.toString();
        System.out.println("3");
        // 7.创建Gson对象用于解析JSON字符串
        Gson gson = new Gson();
        System.out.println("4");
        // 8.将JSON字符串还原为Java对象
        Student student = gson.fromJson(requestBody, Student.class);
        System.out.println("5");
        System.out.println("student = " + student);

        System.out.println("requestBody = " + requestBody);

//        response.setContentType("text/html;charset=UTF-8");
//        response.getWriter().write("服务器端返回普通文本字符串作为响应");




    }
}
