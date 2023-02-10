package cn.whpu.webserver.service.impl;

import cn.whpu.webserver.Database.Plane;
import cn.whpu.webserver.GlobalVar.City;
import cn.whpu.webserver.Graph.BuildPlaneFile;
import cn.whpu.webserver.Search.util.Path;
import cn.whpu.webserver.respVo.FlightDetailVo;
import cn.whpu.webserver.respVo.ResponseInfoVo;
import cn.whpu.webserver.service.SearchService;
import cn.whpu.webserver.vo.BaseFlightInfo;
import cn.whpu.webserver.vo.SearchInfoVo;
import com.alibaba.fastjson.JSON;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class SearchServiceImpl implements SearchService {
    @Override
    public String searchTickets(SearchInfoVo searchInfoVo) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        // 一共有两个库，plane1.db和plane2.db，当更新数据时，GlobalVar中的Lock会记为true
        // plane1.db会被锁，此时只能传plane2.db的连接，当Lock为false时两个都行
        List<Plane> planeList = new ArrayList<>();

        for(int i = 0; i < searchInfoVo.getSearchOptionFlightInfos().size(); i++){
            BaseFlightInfo curFlightInfo = searchInfoVo.getSearchOptionFlightInfos().get(i);
            System.out.println(curFlightInfo);

            String depature = City.GetCity(curFlightInfo.getDepartureCity());
            String arrival = City.GetCity(curFlightInfo.getArrivalCity());
            Plane plane = new Plane(depature, arrival, getIntDate(curFlightInfo.getDepartureTime()));
            planeList.add(plane);
        }

        //校验代理人和总记录数(默认30)
        if(searchInfoVo.getAgencies() == null) searchInfoVo.setAgencies("");
        if(searchInfoVo.getTotalCount() == null || searchInfoVo.getTotalCount() == 0){
            searchInfoVo.setTotalCount(30);
        }

        //校验页码(每页显示10个)，小于等于0的部分
        if(searchInfoVo.getPageNum() == null || searchInfoVo.getPageNum() <= 0){
            searchInfoVo.setPageNum(1);
        }
        int pageNum = searchInfoVo.getPageNum();        //当前从前端传过来的页码


        BuildPlaneFile buildPlaneFile = new BuildPlaneFile(searchInfoVo.getAgencies(), searchInfoVo.getPersonNum());

        buildPlaneFile.BuildPlane(planeList);
        //根据当前页码获取总结果数
        List<Path> paths = buildPlaneFile.getPathfile(searchInfoVo.getPageNum() * 10);

        int resultCount = paths.size() - 1;   //去掉最后一个99999999的
        //如果前端传过来的页码要大于能够展现的最大的页码，则将页码修改为能够展现的最大的页码
        if(resultCount - (pageNum - 1) * 10 <= 0){
            pageNum = resultCount / 10;
            pageNum += resultCount % 10 == 0 ? 0 : 1;   //如果不能够整数，那么就需要再加一个1
        }

        List<ResponseInfoVo> responseInfoVos = new ArrayList<>();

        for (int i = 10 * (pageNum - 1); i < 10 * pageNum && i < paths.size(); i++) {
            if(responseInfoVos.size() >= 10 * pageNum) break;
            // 当输出的总价非常大时（999999999）表示后续没有了
            Path path = paths.get(i);
            if(path.getTotalCost() >= 999999999) break;
            ResponseInfoVo responseInfoVo = new ResponseInfoVo();
            responseInfoVo.setTotalPrice(path.getTotalCost());

            List<FlightDetailVo> flightDetailVos = new ArrayList<>();

            String[] pathInfo = path.getPath().split(",");
            for(int j = 1; j < pathInfo.length - 1; j++){
                try{
                    FlightDetailVo flightDetailVo = new FlightDetailVo();
                    flightDetailVo.setCarrierName(pathInfo[j].substring(1, 3));
                    flightDetailVo.setFlightNo(pathInfo[j].substring(3, 7));
                    flightDetailVo.setDepartureTime(pathInfo[j].substring(7, 19));
                    flightDetailVo.setArrivalTime(pathInfo[j].substring(19, 31));
                    flightDetailVo.setSeatInfo(pathInfo[j].substring(31, 31 + searchInfoVo.getPersonNum()));
                    //1HU 7145 202301011529 202301011944 YY

                    //如果段数大于1段的话并且没有设置代理人信息的话就设置代理人信息
                    if(searchInfoVo.getSearchOptionFlightInfos().size() > 1 && responseInfoVo.getAgency() == null) responseInfoVo.setAgency(pathInfo[j].substring(31 + searchInfoVo.getPersonNum()));

                    flightDetailVos.add(flightDetailVo);
                } catch(Exception e){
                    System.out.println("=============算法中存在长度不符合规范的结果");
                }

            }
            if(flightDetailVos.size() == searchInfoVo.getSearchOptionFlightInfos().size()) {
                responseInfoVo.setFlightDetailVos(flightDetailVos);
                //添加结果集
                responseInfoVos.add(responseInfoVo);
            }

        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("pageNum", pageNum);
        map.put("data", responseInfoVos);
        System.out.println(map);
        return JSON.toJSONString(map);
    }

    private String arrayToString(String[] agencies) {
        if(agencies == null || agencies.length == 0) return "ALL";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < agencies.length; i++){
            sb.append(agencies[i]);
            if(i != agencies.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

    public int getIntDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return (date.getYear() + 1900) * 10000 + (date.getMonth() + 1) * 100 + calendar.get(Calendar.DAY_OF_MONTH) + 1;
    }
}
