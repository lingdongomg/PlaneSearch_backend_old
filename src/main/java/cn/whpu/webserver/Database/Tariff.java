package cn.whpu.webserver.Database;

import java.util.Set;

public class Tariff implements Comparable<Tariff> {
    private final Integer sequenceNO;
    private final String nextCarrier;
    private final Integer surcharge;
    private final Set<String> agencies;

    public Tariff(Integer sequenceNO, String nextCarrier, Set<String> agencies, Integer surcharge) {
        this.sequenceNO = sequenceNO;
        this.nextCarrier = nextCarrier;
        this.agencies = agencies;
        this.surcharge = surcharge;
    }

    public Set<String> getAgencies() {
        return agencies;
    }

    public Integer getSequenceNO() {
        return sequenceNO;
    }


    public String getNextCarrier() {
        return nextCarrier;
    }

    public Integer getSurcharge() {
        return surcharge;
    }

    @Override
    public int compareTo(Tariff o) {
        return -o.getSequenceNO().compareTo(this.getSequenceNO());
    }

    @Override
    public String toString() {
        return sequenceNO + "\t" + nextCarrier + "\t" + agencies + "\t" + surcharge;
    }
}
