package stormTP.operator;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.windowing.TupleWindow;

import java.util.Map;


public class SpeedBolt extends BaseWindowedBolt {
    private static final long serialVersionUID = 4262387370788107343L;
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(TupleWindow inputWindow) {
/*
        int cpt = 0;
        ArrayList<Integer> tableau = new ArrayList<Integer>();
        TortoiseManager manager = new TortoiseManager(4, "Flores-Dorliat");

        for(Tuple t: inputWindow.get()) {
            if ( cpt%5 ==0 ){
                tableau.add(cpt);
            }
            cpt++;
        }

        List<Tuple> t = inputWindow.get();
        JsonObject row = Json.createObjectBuilder().build();

        for(int i: tableau){
            if(tableau.contains(i+2)) {
                String n = t.get(i).getValueByField("json").toString();
                String n2 = t.get(i+2).getValueByField("json").toString();
                Runner filterInit = manager.filter(n);
                Runner filterFin = manager.filter(n2);

                Double speed = manager.computeSpeed(filterInit.getTop(), filterFin.getTop(), filterInit.getPosition(), filterFin.getPosition());

                JsonObjectBuilder r = Json.createObjectBuilder();
                r.add("id", filterInit.getId());
                r.add("top", filterInit.getTop() + "-" + filterFin.getTop());
                r.add("nom", filterInit.getNom());
                r.add("vitesse", speed);
                row = r.build();
            }
        }

        //System.out.println( n  + " is treated!");
        collector.emit(inputWindow.get(), new Values(row.toString()));*/
            return;

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("json"));
    }
}
