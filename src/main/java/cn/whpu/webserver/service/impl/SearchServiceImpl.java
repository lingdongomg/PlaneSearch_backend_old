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
        // һ���������⣬plane1.db��plane2.db������������ʱ��GlobalVar�е�Lock���Ϊtrue
        // plane1.db�ᱻ������ʱֻ�ܴ�plane2.db�����ӣ���LockΪfalseʱ��������
        List<Plane> planeList = new ArrayList<>();

        for(int i = 0; i < searchInfoVo.getSearchOptionFlightInfos().size(); i++){
            BaseFlightInfo curFlightInfo = searchInfoVo.getSearchOptionFlightInfos().get(i);
            System.out.println(curFlightInfo);

            String depature = City.GetCity(curFlightInfo.getDepartureCity());
            String arrival = City.GetCity(curFlightInfo.getArrivalCity());
            Plane plane = new Plane(depature, arrival, getIntDate(curFlightInfo.getDepartureTime()));
            planeList.add(plane);
        }

        //У������˺��ܼ�¼��(Ĭ��30)
        if(searchInfoVo.getAgencies() == null) searchInfoVo.setAgencies("");
        if(searchInfoVo.getTotalCount() == null || searchInfoVo.getTotalCount() == 0){
            searchInfoVo.setTotalCount(30);
        }

        //У��ҳ��(ÿҳ��ʾ10��)��С�ڵ���0�Ĳ���
        if(searchInfoVo.getPageNum() == null || searchInfoVo.getPageNum() <= 0){
            searchInfoVo.setPageNum(1);
        }
        int pageNum = searchInfoVo.getPageNum();        //��ǰ��ǰ�˴�������ҳ��


        BuildPlaneFile buildPlaneFile = new BuildPlaneFile(searchInfoVo.getAgencies(), searchInfoVo.getPersonNum());

        buildPlaneFile.BuildPlane(planeList);
        //���ݵ�ǰҳ���ȡ�ܽ����
        List<Path> paths = buildPlaneFile.getPathfile(searchInfoVo.getPageNum() * 10);

        int resultCount = paths.size() - 1;   //ȥ�����һ��99999999��
        //���ǰ�˴�������ҳ��Ҫ�����ܹ�չ�ֵ�����ҳ�룬��ҳ���޸�Ϊ�ܹ�չ�ֵ�����ҳ��
        if(resultCount - (pageNum - 1) * 10 <= 0){
            pageNum = resultCount / 10;
            pageNum += resultCount % 10 == 0 ? 0 : 1;   //������ܹ���������ô����Ҫ�ټ�һ��1
        }

        List<ResponseInfoVo> responseInfoVos = new ArrayList<>();

        for (int i = 10 * (pageNum - 1); i < 10 * pageNum && i < paths.size(); i++) {
            if(responseInfoVos.size() >= 10 * pageNum) break;
            // ��������ܼ۷ǳ���ʱ��999999999����ʾ����û����
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

                    //�����������1�εĻ�����û�����ô�������Ϣ�Ļ������ô�������Ϣ
                    if(searchInfoVo.getSearchOptionFlightInfos().size() > 1 && responseInfoVo.getAgency() == null) responseInfoVo.setAgency(pathInfo[j].substring(31 + searchInfoVo.getPersonNum()));

                    flightDetailVos.add(flightDetailVo);
                } catch(Exception e){
                    System.out.println("=============�㷨�д��ڳ��Ȳ����Ϲ淶�Ľ��");
                }

            }
            if(flightDetailVos.size() == searchInfoVo.getSearchOptionFlightInfos().size()) {
                responseInfoVo.setFlightDetailVos(flightDetailVos);
                //��ӽ����
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
