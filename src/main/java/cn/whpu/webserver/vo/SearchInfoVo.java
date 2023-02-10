package cn.whpu.webserver.vo;

import java.util.Arrays;
import java.util.List;

public class SearchInfoVo {
    /**
     * 代理人列表
     */
    private String agencies;

    /**
     * 人数
     */
    private int personNum;

    /**
     * 前端发来的所有需要查询的航班的信息
     */
    List<BaseFlightInfo> searchOptionFlightInfos;

    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 用户输入的总记录数
     */
    private Integer totalCount;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getAgencies() {
        return agencies;
    }

    public void setAgencies(String agencies) {
        this.agencies = agencies;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public List<BaseFlightInfo> getSearchOptionFlightInfos() {
        return searchOptionFlightInfos;
    }

    public void setSearchOptionFlightInfos(List<BaseFlightInfo> searchOptionFlightInfos) {
        this.searchOptionFlightInfos = searchOptionFlightInfos;
    }

    public SearchInfoVo() {
    }

    public SearchInfoVo(String agencies, int personNum, List<BaseFlightInfo> searchOptionFlightInfos) {
        this.agencies = agencies;
        this.personNum = personNum;
        this.searchOptionFlightInfos = searchOptionFlightInfos;
    }

}
