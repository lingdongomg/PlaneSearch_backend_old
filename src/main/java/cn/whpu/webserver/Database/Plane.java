package cn.whpu.webserver.Database;

public class Plane {
    private final String departure;
    private final String arrival;
    private final Integer departureDate;
    private final String carrier;
    private final Integer flightNO;
    private final Long departureDatetime;
    private final Long arrivalDatetime;
    private String seat;

    public Plane(String departure, String arrival, Integer departureDate) {
        this.departure = departure;
        this.arrival = arrival;
        this.departureDate = departureDate;
        this.carrier = "a";
        this.flightNO = -1;
        this.departureDatetime = -1L;
        this.arrivalDatetime = -1L;
    }

    public Plane(String carrier, Integer flightNO, Long departureDatetime, Long arrivalDatetime, String departure, String arrival, Integer departureDate) {
        this.carrier = carrier;
        this.flightNO = flightNO;
        this.departureDatetime = departureDatetime;
        this.arrivalDatetime = arrivalDatetime;
        this.departure = departure;
        this.arrival = arrival;
        this.departureDate = departureDate;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getCarrier() {
        return carrier;
    }

    public Integer getFlightNO() {
        return flightNO;
    }

    public Long getDepartureDatetime() {
        return departureDatetime;
    }

    public Long getArrivalDatetime() {
        return arrivalDatetime;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public Integer getDepartureDate() {
        return departureDate;
    }
}
