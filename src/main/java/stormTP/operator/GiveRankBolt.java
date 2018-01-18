package stormTP.operator;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import stormTP.core.Runner;
import stormTP.core.TortoiseManager;

import java.util.Map;

//import java.util.logging.Logger;


/**
 * Sample of stateless operator
 * @author lumineau
 *
 */
public class GiveRankBolt implements IRichBolt {

    private static final long serialVersionUID = 4262369370788107343L;
    private OutputCollector collector;

    public GiveRankBolt () {

    }

    /* (non-Javadoc)
     * @see backtype.storm.topology.IRichBolt#execute(backtype.storm.tuple.Tuple)
     */
    public void execute(Tuple t) {



        TortoiseManager manager = new TortoiseManager(4, "Flores-Dorliat");
        Runner rank = manager.computeRank(t.getLongByField("id"), t.getLongByField("top"),
                t.getStringByField("nom"),
                t.getIntegerByField("nbDevant"), t.getIntegerByField("nbDerriere"), t.getIntegerByField("total"));

        collector.emit(t,new Values(rank.getId(), rank.getTop(), rank.getNom(), rank.getTotal(), rank.getRang()));

        return;

    }



    /* (non-Javadoc)
     * @see backtype.storm.topology.IComponent#declareOutputFields(backtype.storm.topology.OutputFieldsDeclarer)
     */
    public void declareOutputFields(OutputFieldsDeclarer arg0) {
        arg0.declare(new Fields("id", "top", "nom", "total", "rang"));
    }


    /* (non-Javadoc)
     * @see backtype.storm.topology.IComponent#getComponentConfiguration()
     */
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    /* (non-Javadoc)
     * @see backtype.storm.topology.IBasicBolt#cleanup()
     */
    public void cleanup() {

    }



    /* (non-Javadoc)
     * @see backtype.storm.topology.IRichBolt#prepare(java.util.Map, backtype.storm.task.TopologyContext, backtype.storm.task.OutputCollector)
     */
    @SuppressWarnings("rawtypes")
    public void prepare(Map arg0, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }
}