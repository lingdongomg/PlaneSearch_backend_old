package cn.whpu.webserver.respVo;


import java.util.List;

/**
 * 代理人名称、总价格、总时长、每一趟航班的详细信息
 */
public class ResponseInfoVo {
    /**
     * 代理人名称
     */
    private String agency;

    /**
     * 总价格
     */
    private Integer totalPrice;
    /**
     * 返回给前端页面的每一条的详细的航班信息
     */
    private List<FlightDetailVo> flightDetailVos;



    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<FlightDetailVo> getFlightDetailVos() {
        return flightDetailVos;
    }

    public void setFlightDetailVos(List<FlightDetailVo> flightDetailVos) {
        this.flightDetailVos = flightDetailVos;
    }


}
