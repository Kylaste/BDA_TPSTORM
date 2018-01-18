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


        String n = t.getValueByField("json").toString();
        TortoiseManager manager = new TortoiseManager(4, "Flores-Dorliat");
        Runner filter = manager.filter(n);
        Runner rank = manager.computeRank(filter.getId(), filter.getTop(), filter.getNom(), filter.getNbDevant(), filter.getNbDerriere(), filter.getTotal());

        System.out.println( n  + " is treated!");
        collector.emit(t,new Values(rank.getJSON_V2()));

        return;

    }



    /* (non-Javadoc)
     * @see backtype.storm.topology.IComponent#declareOutputFields(backtype.storm.topology.OutputFieldsDeclarer)
     */
    public void declareOutputFields(OutputFieldsDeclarer arg0) {
        arg0.declare(new Fields("json"));
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