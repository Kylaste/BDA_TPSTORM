package stormTP.operator;

import org.apache.storm.state.KeyValueState;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseStatefulWindowedBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.windowing.TupleWindow;
import stormTP.core.TortoiseManager;

import java.util.Map;



public class RankEvolutionBolt extends BaseStatefulWindowedBolt<KeyValueState<String, Integer>> {
    private static final long serialVersionUID = 4262379330788107343L;
    private  KeyValueState<String, Integer> state;
    private  int sum;

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void initState(KeyValueState<String, Integer> state) {

        this.state = state;
        sum = state.get("sum", 0);
        System.out.println("initState with state [" + state + "] current sum [" + sum + "]");
    }

    @Override
    public void execute(TupleWindow inputWindow) {

        sum = state.get("sum", 0);

        long id = inputWindow.get().get(0).getLongByField("id");
        String nom = inputWindow.get().get(0).getStringByField("nom");
        long topInit= inputWindow.get().get(0).getLongByField("top");
        long topFin = inputWindow.get().get(inputWindow.get().size()-1).getLongByField("top");
        TortoiseManager manager = new TortoiseManager(4, "Flores-Dorliat");

        int cpt = 0;
        int rangMoy;
        String[] tabRang = new String[inputWindow.get().size()];

        for (Tuple t : inputWindow.get()) {
            tabRang[cpt] = t.getStringByField("rang");
            cpt ++;
        }

        rangMoy = manager.giveAverageRank(tabRang);
        String evolution = manager.giveRankEvolution(rangMoy, sum);

        state.put("sum", rangMoy);
        collector.emit(inputWindow.get(),new Values(id, nom, topInit+"-"+topFin, evolution));

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id", "nom", "tops", "evolution"));
    }
}

