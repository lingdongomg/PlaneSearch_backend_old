package cn.whpu.webserver.vo;

import java.util.Date;

/**
 * 前端传来的一段航程的信息
 */
public class BaseFlightInfo {
    @Override
    public String toString() {
        return "BaseFlightInfo{" +
                "departureCity='" + departureCity + '\'' +
                ", arrivalCity='" + arrivalCity + '\'' +
                ", departureTime=" + departureTime +
                '}';
    }

    /**
     * 起飞城市
     */
    private String departureCity;

    /**
     * 到达城市
     */
    private String arrivalCity;

    /**
     * 起飞时间
     */
    private Date departureTime;

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public BaseFlightInfo(String departureCity, String arrivalCity, Date departureTime) {
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureTime = departureTime;
    }

    public BaseFlightInfo() {
    }
}
