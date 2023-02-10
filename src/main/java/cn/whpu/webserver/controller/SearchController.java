package cn.whpu.webserver.controller;

import cn.whpu.webserver.service.SearchService;
import cn.whpu.webserver.service.impl.SearchServiceImpl;
import cn.whpu.webserver.vo.BaseFlightInfo;
import cn.whpu.webserver.vo.SearchInfoVo;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class SearchController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        System.out.println(req.getContextPath());
        System.out.println("111111");
        //service(req, resp);
    }

    public void service(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        System.out.println("============================================");
        System.out.println(request.getParameter("personNum"));
        System.out.println(request.getParameter("agencies"));
        System.out.println(request.getParameter("searchOptionFlightInfos"));
        SearchInfoVo searchInfoVo = new SearchInfoVo();


        searchInfoVo.setPersonNum(Integer.parseInt(request.getParameter("personNum")));
        if(request.getParameter("agencies") == null){
            searchInfoVo.setAgencies(null);
        }else {
//            searchInfoVo.setAgencies((String[]) JSON.parseArray(request.getParametersByGet("agencies"), String.class).toArray());
            searchInfoVo.setAgencies(URLDecoder.decode(request.getParameter("agencies"), "UTF-8"));
        }

        String decodeStr = null;
        try {
            decodeStr = URLDecoder.decode(request.getParameter("searchOptionFlightInfos"), "GBK");
        } catch (UnsupportedEncodingException e) {
            System.out.println("解码失败！！！");
            e.printStackTrace();
        }
        System.out.println("解码:" + decodeStr);

        searchInfoVo.setSearchOptionFlightInfos(JSON.parseArray(decodeStr, BaseFlightInfo.class));

        System.out.println(searchInfoVo);

        searchInfoVo.setPageNum(Integer.valueOf(request.getParameter("pageNum")));
        System.out.println("pageNum : " + request.getParameter("pageNum"));

        String result = null;
        try {
            result = searchTickets(searchInfoVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println(result);
        resp.getWriter().flush();
    }

    /**
     * 搜索机票，用户输入多少段那就返回多少段
     * @param searchInfoVo
     * @return
     */
    public String searchTickets(SearchInfoVo searchInfoVo) throws Exception {
        SearchService searchService = new SearchServiceImpl();
        System.out.println(searchInfoVo);
        System.out.println("请求已经进来了");
        return searchService.searchTickets(searchInfoVo);
    }
}
