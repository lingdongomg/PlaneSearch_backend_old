package cn.whpu.webserver.Database.SearchData;


import cn.whpu.webserver.Database.Seat;
import cn.whpu.webserver.GlobalVar.Lock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class updateData {
    public void updateSeat(List<Seat> seats) {
        Lock.lock = true;
        try {
            Thread.sleep(10000);
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:plane1.db");
            connection.setAutoCommit(false);
            String sql;
            Statement statement = connection.createStatement();
            for (Seat seat : seats) {
                sql = "update seatinfo" + seat.getDepartureDate() / 100 + " set meiyong='";
                if (seat.getSeatF() != null) sql += "',seatF='" + seat.getSeatF();
                if (seat.getSeatC() != null) sql += "',seatC='" + seat.getSeatC();
                if (seat.getSeatY() != null) sql += "',seatY='" + seat.getSeatY();
                sql += String.format("' where carrier='%s' and flightNO=%d and departure='%s' and arrival='%s' and departureDate=%d", seat.getCarrier(),
                        seat.getFlightNO(), seat.getDeparture(), seat.getArrival(), seat.getDepartureDate());
                statement.executeLargeUpdate(sql);
            }
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Lock.lock = false;
        try {
            Thread.sleep(10000);
            Connection connection = DriverManager.getConnection("jdbc:sqlite:plane2.db");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            String sql;
            for (Seat seat : seats) {
                sql = "update seatinfo" + seat.getDepartureDate() / 100 + " set meiyong='";
                if (seat.getSeatF() != null) sql += "',seatF='" + seat.getSeatF();
                if (seat.getSeatC() != null) sql += "',seatC='" + seat.getSeatC();
                if (seat.getSeatY() != null) sql += "',seatY='" + seat.getSeatY();
                sql += String.format("' where carrier='%s' and flightNO=%d and departure='%s' and arrival='%s' and departureDate=%d", seat.getCarrier(),
                        seat.getFlightNO(), seat.getDeparture(), seat.getArrival(), seat.getDepartureDate());
                statement.executeLargeUpdate(sql);
            }
            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
