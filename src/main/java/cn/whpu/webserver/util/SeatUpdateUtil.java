package cn.whpu.webserver.util;

import cn.whpu.webserver.Database.SearchData.updateData;
import cn.whpu.webserver.Database.Seat;
import cn.whpu.webserver.GlobalVar.Carrier;
import cn.whpu.webserver.GlobalVar.City;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SeatUpdateUtil {
    public static updateData seatUpdate = new updateData();

    public static ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(5);

    public static void startUpdateSeatTask() {
        threadPool.scheduleAtFixedRate(() -> {
                    updateSeat();
                },
                5L, // 5H 后开始执行定时任务
                12L, // 定时任务的执行间隔为 12H
                TimeUnit.HOURS); // 描述上面两个参数的时间单位
    }

    public static void updateSeat() {
        List<Seat> seats = new ArrayList<>(1000000);
        Random random = new Random();
        String[] seatInfo = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A"};

        for (int i = 0; i < seats.size(); i++) {
            String carrier = Carrier.carrier[random.nextInt(Carrier.carrier.length)];
            Integer flightNo = random.nextInt(10) * 1000 + random.nextInt(10) * 100 + random.nextInt(10) * 10 + random.nextInt(10);
            String departure = City.CITIES[random.nextInt(City.CITIES.length)];
            String arrival = City.CITIES[random.nextInt(City.CITIES.length)];

            int month = (random.nextInt(11) + 1);
            int day = -1;
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
                day = 31;
            else if (month == 2) day = 28;
            else day = 30;
            int departureDate = 2023 * 10000 + month * 100 + day;
            String seatF = seatInfo[random.nextInt(seatInfo.length)];
            String seatC = seatInfo[random.nextInt(seatInfo.length)];
            String seatY = seatInfo[random.nextInt(seatInfo.length)];

            Seat seat = new Seat(carrier, flightNo, departure, arrival, departureDate);
            seat.setSeatC(seatC);
            seat.setSeatF(seatF);
            seat.setSeatY(seatY);

            seats.add(seat);
        }

        seatUpdate.updateSeat(seats);

    }
}
