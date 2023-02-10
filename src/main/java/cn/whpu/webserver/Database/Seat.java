package cn.whpu.webserver.Database;

public class Seat {
    private final String carrier;
    private final Integer flightNO;
    private final String departure;
    private final String arrival;
    private final Integer departureDate;
    private String seatF;
    private String seatC;
    private String seatY;

    public Seat(String carrier, Integer flightNO, String departure, String arrival, Integer departureDate) {
        this.carrier = carrier;
        this.flightNO = flightNO;
        this.departure = departure;
        this.arrival = arrival;
        this.departureDate = departureDate;
    }

    public void setSeatF(String seatF) {
        this.seatF = seatF;
    }

    public void setSeatC(String seatC) {
        this.seatC = seatC;
    }

    public void setSeatY(String seatY) {
        this.seatY = seatY;
    }

    public String getCarrier() {
        return carrier;
    }

    public Integer getFlightNO() {
        return flightNO;
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

    public String getSeatF() {
        return seatF;
    }

    public String getSeatC() {
        return seatC;
    }

    public String getSeatY() {
        return seatY;
    }
}
