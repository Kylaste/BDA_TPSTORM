package stormTP.operator;

import org.apache.storm.state.KeyValueState;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseStatefulBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import stormTP.core.Runner;
import stormTP.core.TortoiseManager;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Map;


public class ComputeBonusBolt extends BaseStatefulBolt<KeyValueState<String, Integer>> {
    private static final long serialVersionUID = 4262379330722107343L;
    KeyValueState<String, Integer> kvState;
    int sum;
    private OutputCollector collector;


    @Override
    public void execute(Tuple t) {

        String n = t.getValueByField("json").toString();
        TortoiseManager manager = new TortoiseManager(4, "Flores-Dorliat");
        Runner filter = manager.filter(n);

        sum += manager.computePoints(filter.getRang(), filter.getTotal() );

        kvState.put("sum", sum);

        JsonObjectBuilder r = Json.createObjectBuilder();
        r.add("id",filter.getId());
        r.add("top", filter.getTop());
        r.add("nom", filter.getNom());
        r.add("score", 1);
        JsonObject row = r.build();

        collector.emit(t,new Values(row.toString()));

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
        declarer.declare(new Fields("json"));
    }


}