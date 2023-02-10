package cn.whpu.webserver.Database.SearchData;


import cn.whpu.webserver.Database.Plane;
import cn.whpu.webserver.Database.Tariff;
import cn.whpu.webserver.Database.connectionPool.ConnectionPool;
import cn.whpu.webserver.GlobalVar.Agencies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class SearchData {

    public List<Plane> searchPlane(String departure, String arrival, int departureDate, int people) {
        String flighttable = "flightinfo" + departureDate / 100;
        String seattable = "seatinfo" + departureDate / 100;
        String sql = String.format("SELECT %s.carrier, %s.flightNO," +
                        "%s.departureDatetime,%s.arrivalDatetime,%s.seatY,%s.seatC,%s.seatF " +
                        "FROM %s INNER JOIN %s ON %s.ID=%s.ID WHERE %s.departure='%s' AND " +
                        "%s.arrival='%s' AND %s.departureDate=%d;",
                flighttable, flighttable, flighttable, flighttable, seattable,
                seattable, seattable, flighttable, seattable, flighttable, seattable,
                flighttable, departure, flighttable, arrival, flighttable, departureDate);
        List<Plane> planes = new ArrayList<>();
        try (
                Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                String carrier = resultSet.getString("carrier");
                int flightNO = resultSet.getInt("flightNO");
                long departureDatetime = resultSet.getLong("departureDatetime");
                long arrivalDatetime = resultSet.getLong("arrivalDatetime");
                String seatY = resultSet.getString("seatY");
                int Ynum = Objects.equals(seatY, "A") ? 10 : Integer.parseInt(seatY);
                String seatC = resultSet.getString("seatC");
                int Cnum = Objects.equals(seatC, "A") ? 10 : Integer.parseInt(seatC);
                String seatF = resultSet.getString("seatF");
                int Fnum = Objects.equals(seatF, "A") ? 10 : Integer.parseInt(seatF);
                Plane plane = new Plane(carrier, flightNO, departureDatetime, arrivalDatetime, departure, arrival, departureDate);
                String seat = "";
                if (Ynum >= people) {
                    seat += "Y".repeat(people);
                } else {
                    seat += "Y".repeat(Ynum);
                    people -= Ynum;
                    if (Cnum >= people) {
                        seat += "C".repeat(people);
                    } else {
                        seat += "C".repeat(Cnum);
                        people -= Cnum;
                        if (Fnum >= people) {
                            seat += "F".repeat(people);
                        } else {
                            continue;
                        }
                    }
                }

                plane.setSeat(seat);
                planes.add(plane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return planes;
    }

    public Map<String, Integer> searchFare(String departure, String arrival) {
        Map<String, Integer> map = new HashMap<>();
        String sql = String.format("select carrier,cabin,amount from fareinfo where departure='%s' and arrival='%s';", departure, arrival);
        try (
                Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                map.put(resultSet.getString("carrier") + resultSet.getString("cabin"), resultSet.getInt("amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, PriorityQueue<Tariff>> searchTariff(String departure, String arrival) {
        Map<String, PriorityQueue<Tariff>> map = new HashMap<>();
        String sql = String.format("select sequenceNO,carrier,nextCarrier,agencies,surcharge from tariff where departure in ('%s','') and arrival in ('%s','')", departure, arrival);
        try (
                Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                Set<String> agenciesset = new HashSet<>();
                int sequenceNO = resultSet.getInt("sequenceNO");
                String carrier = resultSet.getString("carrier");
                String nextcarrier = resultSet.getString("nextCarrier");
                if (Objects.equals(nextcarrier, "")) {
                    nextcarrier = "ALL";
                }
                String agencies = resultSet.getString("agencies");
                if (Objects.equals(agencies, "")) {
                    agenciesset = Agencies.agenciesSet;
                } else {
                    for (int i = 0; i < agencies.length(); i += 6) {
                        agenciesset.add(agencies.substring(i, i + 6));
                    }
                }
                int surchaege = resultSet.getInt("surcharge");

                Tariff tariff = new Tariff(sequenceNO, nextcarrier, agenciesset, surchaege);
                if (map.get(carrier) == null) {
                    PriorityQueue<Tariff> queue = new PriorityQueue<>();
                    queue.add(tariff);
                    map.put(carrier, queue);
                } else {
                    PriorityQueue<Tariff> queue = map.get(carrier);
                    queue.add(tariff);
                    map.put(carrier, queue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}