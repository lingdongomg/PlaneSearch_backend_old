package cn.whpu.webserver.Graph;

import cn.whpu.webserver.Database.Plane;
import cn.whpu.webserver.Database.Tariff;
import cn.whpu.webserver.GlobalVar.Agencies;
import cn.whpu.webserver.Search.graph.Graph;
import cn.whpu.webserver.Search.ksp.Eppstein;
import cn.whpu.webserver.Search.util.Path;

import java.util.*;

public class BuildPlaneFile {
    // 代理人名
    private final String agencies;
    // 人数
    private final Integer people;
    // 存哪些承运人能到下一段行程
    DataSearch dataSearch;

    // carrier key的列表，里面的key一定可以通过某个agencies到终点
    Map<String, List<String>> carriermap = new HashMap<>();

    // key 这个key有哪些agencies可以到终点
    Map<String, Set<String>> agenciesmap = new HashMap<>();

    StringBuilder graphfile;

    public BuildPlaneFile(String agencies, Integer people) {
        if (agencies.length() < 4)
            this.agencies = Agencies.agencies;
        else
            this.agencies = agencies;
        this.people = people;
        dataSearch = new DataSearch();
        graphfile = new StringBuilder();
        graphfile.append("000 999 999999999\n");
    }

    // 建图
    public void BuildPlane(List<Plane> planeList) {
        // 查找运价规则
        int len = planeList.size();

        // 如果只有一段行程
        if (len == 1) {
            buildOnlyOne(planeList.get(0));
        }

        for (int i = len - 1; i >= 0; i--) {
            // 如果是最后一段行程
            if (i == len - 1) {
                buildLast(i, planeList.get(i));
            } else if (i == 0) {
                buildFirst(i, planeList.get(i));
            } else {
                buildPlaneGraph(i, planeList.get(i));
            }
        }
    }

    // 只有一段行程
    private void buildOnlyOne(Plane plane2) {
        // 查找行程
        List<Plane> planes = dataSearch.SearchPlane(plane2, this.people);
        Map<String, Integer> faremap = dataSearch.SearchFare(plane2);

        // 直接建边
        for (Plane plane : planes) {
            // 首先查询票价
            int price = 0;
            for (int i = 0; i < plane.getSeat().length(); i++) {
                if (plane.getSeat().charAt(i) == 'Y') price += faremap.get(plane.getCarrier() + "Y");
                if (plane.getSeat().charAt(i) == 'C') price += faremap.get(plane.getCarrier() + "C");
                if (plane.getSeat().charAt(i) == 'F') price += faremap.get(plane.getCarrier() + "F");
            }
            String key = "1" + plane.getCarrier() + plane.getFlightNO() + plane.getDepartureDatetime() + plane.getArrivalDatetime() + plane.getSeat();

            // 如果有票
            if (price != 0) {
                graphfile.append(String.format("%s 999 %d\n", key, price));
                graphfile.append(String.format("000 %s 0\n", key));
            }
        }
    }

    // 如果是最后一段行程
    private void buildLast(int i, Plane plane2) {
        Map<String, List<String>> carriermap = new HashMap<>();
        Map<String, Set<String>> agenciesmap = new HashMap<>();

        // 查找行程
        List<Plane> planes = dataSearch.SearchPlane(plane2, this.people);
        Map<String, Integer> faremap = dataSearch.SearchFare(plane2);

        // 最后一段行程不需要匹配运价规则，直接建边
        for (Plane plane : planes) {
            // 获取第j个航班
            // 首先查询票价
            int price = 0;
            for (int i1 = 0; i1 < plane.getSeat().length(); i1++) {
                if (plane.getSeat().charAt(i1) == 'Y') price += faremap.get(plane.getCarrier() + "Y");
                if (plane.getSeat().charAt(i1) == 'C') price += faremap.get(plane.getCarrier() + "C");
                if (plane.getSeat().charAt(i1) == 'F') price += faremap.get(plane.getCarrier() + "F");
            }
            String key = (i + 1) + plane.getCarrier() + plane.getFlightNO() + plane.getDepartureDatetime() + plane.getArrivalDatetime() + plane.getSeat();

            // 将key加入 carrier key的字典
            List<String> list = carriermap.get(plane.getCarrier());
            if (list == null) list = new ArrayList<>();
            list.add(key);
            carriermap.put(plane.getCarrier(), list);

            // 所有承运人都可以到达
            Set<String> strings = new HashSet<>();
            for (String agency : this.agencies.split(",")) {
                // 建边
                // agency可以到下一个，加入到集合中
                strings.add(agency);
                graphfile.append(String.format("%s%s 999 %d\n", key, agency, price));
            }
            // 每个航班对应一个key，因此可以直接更新其对应的set
            agenciesmap.put(key, strings);
        }

        // 更新最后一段行程的Carrier字典
        this.carriermap = carriermap;
        this.agenciesmap = agenciesmap;
    }

    // 如果是第一段行程
    private void buildFirst(int i, Plane plane2) {
        Map<String, List<String>> carriermap = new HashMap<>();
        Map<String, Set<String>> agenciesmap = new HashMap<>();
        // 查询行程
        List<Plane> planes = dataSearch.SearchPlane(plane2, this.people);
        Map<String, Integer> faremap = dataSearch.SearchFare(plane2);

        // 遍历行程
        for (Plane plane : planes) {
            // 首先查询票价
            int price = 0;
            for (int i1 = 0; i1 < plane.getSeat().length(); i1++) {
                if (plane.getSeat().charAt(i1) == 'Y') price += faremap.get(plane.getCarrier() + "Y");
                if (plane.getSeat().charAt(i1) == 'C') price += faremap.get(plane.getCarrier() + "C");
                if (plane.getSeat().charAt(i1) == 'F') price += faremap.get(plane.getCarrier() + "F");
            }
            String key = (i + 1) + plane.getCarrier() + plane.getFlightNO() + plane.getDepartureDatetime() + plane.getArrivalDatetime() + plane.getSeat();
            // agenciesmap，一个key对应一个set
            Set<String> agenciesset = new HashSet<>();

            // 一个key对应一个carrier，一个carrier对应多个key
            List<String> carrierlist = carriermap.get(plane.getCarrier());
            if (carrierlist == null) carrierlist = new ArrayList<>();

            // 如果有票
            // 如果可达则匹配运价规则
            HashMap<String, PriorityQueue<Tariff>> tariff = dataSearch.SearchTariff(plane.getDeparture(), plane.getArrival());
            PriorityQueue<Tariff> tariffs = tariff.get(plane.getCarrier());

            // 遍历优先队列
            while (tariffs.size() > 0) {
                // 标识位
                boolean flag = false;

                Tariff tariff1 = tariffs.poll();

                float surcharge = tariff1.getSurcharge();
                // 如果sur为-1表示不卖
                if (surcharge == -1) {
                    continue;
                }

                price = (int) ((100 + surcharge) * price / 100);

                // 获取优先匹配的承运人
                String nextCarrier = tariff1.getNextCarrier();
                Set<String> agencies2 = tariff1.getAgencies();

                // 如果所有都卖
                if (Objects.equals(nextCarrier, "ALL")) {
                    // 所有都买直接遍历全部
                    for (Map.Entry<String, List<String>> entry : this.carriermap.entrySet()) {
                        for (String nextkey : entry.getValue()) {
                            long nextdeparturetime = Long.parseLong(nextkey.substring(7, 19));
                            // 如果不满足时间要求直接continue
                            if (nextdeparturetime - plane.getArrivalDatetime() <= 200) continue;

                            // 匹配agencies
                            Set<String> strings = this.agenciesmap.get(nextkey);
                            if (strings == null) continue;
                            strings.retainAll(agencies2);
                            if (strings.size() == 0) continue;

                            for (String s1 : strings) {
                                graphfile.append(String.format("%s%s %s%s %d\n", key, s1, nextkey, s1, price));
                                graphfile.append(String.format("000 %s%s 0\n", key, s1));
                                agenciesset.add(s1);
                                // 如果加边了就表示能到下一段
                                flag = true;
                            }
                        }
                    }
                } else {
                    // 如果不是所有人都卖

                    // 获取承运人的航班
                    List<String> list = this.carriermap.get(nextCarrier);

                    // 如果下一段行程没有这个承运人直接continue
                    if (list == null || list.size() == 0) {
                        continue;
                    }

                    // 如果有这个承运人就直接遍历承运人对应的所有航班
                    for (String nextkey : list) {
                        long nextdeparturetime = Long.parseLong(nextkey.substring(7, 19));
                        // 如果不满足时间要求直接continue
                        if (nextdeparturetime - plane.getArrivalDatetime() <= 200) continue;

                        // 匹配agencies，首先获取nextkey有哪些agencies可以到下一个航班
                        Set<String> strings = this.agenciesmap.get(nextkey);
                        if (strings == null) continue;
                        strings.retainAll(agencies2);
                        if (strings.size() == 0) continue;

                        for (String s : strings) {
                            // 如果有代理人能到就建边
                            graphfile.append(String.format("%s%s %s%s %d\n", key, s, nextkey, s, price));
                            graphfile.append(String.format("000 %s%s 0\n", key, s));
                            flag = true;
                            // 对key来说s能到下一个，把他加入到能到的集合中
                            agenciesset.add(s);

                        }
                    }
                }
                if (flag) {
                    // 如果这一个key可以到下一个
                    carrierlist.add(key);
                    carriermap.put(plane.getCarrier(), carrierlist);
                    agenciesmap.put(key, agenciesset);
                    break;
                }
            }
        }

        // 在所有行程都遍历完之后更新全局
        this.carriermap = carriermap;
        this.agenciesmap = agenciesmap;
    }

    // 如果不是第一段也不是最后一段
    private void buildPlaneGraph(int i, Plane plane2) {
        Map<String, List<String>> carriermap = new HashMap<>();
        Map<String, Set<String>> agenciesmap = new HashMap<>();
        // 查询行程
        List<Plane> planes = dataSearch.SearchPlane(plane2, this.people);
        Map<String, Integer> faremap = dataSearch.SearchFare(plane2);

        // 遍历行程
        for (Plane plane : planes) {
            // 首先查询票价
            int price = 0;
            for (int i1 = 0; i1 < plane.getSeat().length(); i1++) {
                if (plane.getSeat().charAt(i1) == 'Y') price += faremap.get(plane.getCarrier() + "Y");
                if (plane.getSeat().charAt(i1) == 'C') price += faremap.get(plane.getCarrier() + "C");
                if (plane.getSeat().charAt(i1) == 'F') price += faremap.get(plane.getCarrier() + "F");
            }
            // 如果不可达，直接跳过
            String key = (i + 1) + plane.getCarrier() + plane.getFlightNO() + plane.getDepartureDatetime() + plane.getArrivalDatetime() + plane.getSeat();
            // agenciesmap，一个key对应一个set
            Set<String> agenciesset = new HashSet<>();

            // 一个key对应一个carrier，一个carrier对应多个key
            List<String> carrierlist = carriermap.get(plane.getCarrier());
            if (carrierlist == null) carrierlist = new ArrayList<>();

            // 如果不可达，直接跳过
            if (price == 0) {
                continue;
            }

            // 如果有票
            // 如果可达则匹配运价规则
            HashMap<String, PriorityQueue<Tariff>> tariff = dataSearch.SearchTariff(plane.getDeparture(), plane.getArrival());
            PriorityQueue<Tariff> tariffs = tariff.get(plane.getCarrier());

            // 遍历优先队列
            while (tariffs.size() > 0) {
                // 标识位
                boolean flag = false;

                Tariff tariff1 = tariffs.poll();

                float surcharge = tariff1.getSurcharge();
                // 如果sur为-1表示不卖
                if (surcharge == -1) {
                    continue;
                }

                price = (int) (100 + surcharge) * price / 100;

                // 获取优先匹配的承运人
                String nextCarrier = tariff1.getNextCarrier();
                Set<String> agencies2 = tariff1.getAgencies();

                // 如果所有都卖
                if (Objects.equals(nextCarrier, "ALL")) {
                    // 所有都买直接遍历全部
                    for (Map.Entry<String, List<String>> entry : this.carriermap.entrySet()) {
                        for (String nextkey : entry.getValue()) {
                            long nextdeparturetime = Long.parseLong(nextkey.substring(7, 19));
                            // 如果不满足时间要求直接continue
                            if (nextdeparturetime - plane.getArrivalDatetime() <= 200) continue;

                            // 匹配agencies
                            Set<String> strings = this.agenciesmap.get(nextkey);
                            if (strings == null) continue;
                            strings.retainAll(agencies2);
                            if (strings.size() == 0) continue;

                            for (String s1 : strings) {
                                graphfile.append(String.format("%s%s %s%s %d\n", key, s1, nextkey, s1, price));
                                agenciesset.add(s1);
                            }
                            // 如果加边了就表示能到下一段
                            flag = true;
                        }
                    }
                } else {
                    // 如果不是所有人都卖

                    // 获取承运人的航班
                    List<String> list = this.carriermap.get(nextCarrier);

                    // 如果下一段行程没有这个承运人直接continue
                    if (list == null || list.size() == 0) {
                        continue;
                    }

                    // 如果有这个承运人就直接遍历承运人对应的所有航班
                    for (String nextkey : list) {
                        long nextdeparturetime = Long.parseLong(nextkey.substring(7, 19));
                        // 如果不满足时间要求直接continue
                        if (nextdeparturetime - plane.getArrivalDatetime() <= 200) continue;

                        // 匹配agencies，首先获取nextkey有哪些agencies可以到下一个航班
                        Set<String> strings = this.agenciesmap.get(nextkey);
                        if (strings == null) continue;
                        strings.retainAll(agencies2);
                        if (strings.size() == 0) continue;

                        for (String s : strings) {
                            // 如果有代理人能到就建边
                            graphfile.append(String.format("%s%s %s%s %d\n", key, s, nextkey, s, price));
                            flag = true;
                            // 对key来说s能到下一个，把他加入到能到的集合中
                            agenciesset.add(s);

                        }
                    }
                }
                if (flag) {
                    // 如果这一个key可以到下一个
                    carrierlist.add(key);
                    carriermap.put(plane.getCarrier(), carrierlist);
                    agenciesmap.put(key, agenciesset);
                    break;
                }
            }
        }

        // 在所有行程都遍历完之后更新全局
        this.carriermap = carriermap;
        this.agenciesmap = agenciesmap;
    }

    public List<Path> getPathfile(int k) {
        Graph graph = new Graph(String.valueOf(this.graphfile));
        Eppstein eppstein = new Eppstein();
        return eppstein.ksp(graph, "000", "999", k);
    }
}