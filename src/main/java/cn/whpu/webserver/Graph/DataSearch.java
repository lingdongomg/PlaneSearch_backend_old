package cn.whpu.webserver.Graph;

import cn.whpu.webserver.Database.Plane;
import cn.whpu.webserver.Database.SearchData.SearchData;
import cn.whpu.webserver.Database.Tariff;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DataSearch {
    public HashMap<String, PriorityQueue<Tariff>> SearchTariff(String departure, String arrival) {
        SearchData searchData = new SearchData();
        return (HashMap<String, PriorityQueue<Tariff>>) searchData.searchTariff(departure, arrival);
    }

    public List<Plane> SearchPlane(Plane plane, int people) {
        SearchData searchData = new SearchData();
        return searchData.searchPlane(plane.getDeparture(), plane.getArrival(), plane.getDepartureDate(), people);
    }

    public Map<String, Integer> SearchFare(Plane plane) {
        SearchData searchData = new SearchData();
        return searchData.searchFare(plane.getDeparture(), plane.getArrival());
    }
}
