package stormTP.operator;

import org.apache.storm.state.KeyValueState;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseStatefulBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import stormTP.core.TortoiseManager;

import java.util.Map;


public class ComputeBonusBolt extends BaseStatefulBolt<KeyValueState<String, Integer>> {
    private static final long serialVersionUID = 4262379330722107343L;
    KeyValueState<String, Integer> kvState;
    int sum;
    private OutputCollector collector;


    @Override
    public void execute(Tuple t) {

        TortoiseManager manager = new TortoiseManager(4, "Flores-Dorliat");

        sum += manager.computePoints(t.getStringByField("rang"), t.getIntegerByField("total") );

        kvState.put("sum", sum);

        collector.emit(t,new Values(t.getLongByField("id"), t.getLongByField("top"), t.getStringByField("nom"),sum));

    }

    @Override
    public void initState(KeyValueState<String, Integer> state) {
        kvState = state;
        sum = kvState.get("sum", 0);

    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "top", "nom", "score"));
    }


}