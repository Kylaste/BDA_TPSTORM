package stormTP.operator;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.windowing.TupleWindow;
import stormTP.core.Runner;
import stormTP.core.TortoiseManager;

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

        TortoiseManager manager = new TortoiseManager(4, "Flores-Dorliat");
        String n1 = inputWindow.get().get(0).getValueByField("json").toString();
        Runner tupleInit = manager.filter(n1);
        String n2 = inputWindow.get().get(inputWindow.get().size()-1).getValueByField("json").toString();
        Runner tupleFin = manager.filter(n2);

        long id = tupleInit.getId();
        String nom = tupleInit.getNom();

        long topInit = tupleInit.getTop();
        long topFin = tupleFin.getTop();

        int positionInit = tupleInit.getPosition();
        int positionFin = tupleFin.getPosition();

        Double vitesse = manager.computeSpeed(topInit, topFin, positionInit, positionFin);

        String tops = topInit + "-" + topFin ;

        collector.emit(inputWindow.get(), new Values(id, tops, nom, vitesse));
            return;

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "tops", "nom", "vitesse"));
    }
}
