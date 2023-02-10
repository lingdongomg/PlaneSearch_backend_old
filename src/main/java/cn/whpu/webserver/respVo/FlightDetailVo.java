package cn.whpu.webserver.respVo;


import java.util.Date;

/**
 * 返回给前端页面的每一条详细的航班信息
 */
public class FlightDetailVo {
    /**
     * 承运人名称(长度2)
     */
    private String carrierName;

    /**
     * 航班编号（长度4）
     */
    private String flightNo;

    /**
     * 起飞时间
     */
    private String departureTime;

    /**
     * 到达城市
     */
    private String arrivalTime;

    /**
     * 座位信息
     */
    private String seatInfo;

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getSeatInfo() {
        return seatInfo;
    }

    public void setSeatInfo(String seatInfo) {
        this.seatInfo = seatInfo;
    }
}
