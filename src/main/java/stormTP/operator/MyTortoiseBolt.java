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

public class MyTortoiseBolt implements IRichBolt {

    private static final long serialVersionUID = 4262369370788107343L;
    private OutputCollector collector;

    public MyTortoiseBolt () {

    }

    /* (non-Javadoc)
	 * @see backtype.storm.topology.IRichBolt#execute(backtype.storm.tuple.Tuple)
	 */
    public void execute(Tuple t) {

        String n = t.getValueByField("json").toString();
        TortoiseManager manager = new TortoiseManager(4, "Flores-Dorliat");

        Runner filter = manager.filter(n);

        //System.out.println( n  + " is treated!");
        collector.emit(t,new Values(filter.getId(), t.getLong("top"), t.getString("nom"),
                                    filter.getInt("position"), filter.getInt("nbDevant"), filter.getInt("nbDerriere"),
                                    filter.getInt("total")));

        return;

    }

    /* (non-Javadoc)
	 * @see backtype.storm.topology.IComponent#declareOutputFields(backtype.storm.topology.OutputFieldsDeclarer)
	 */
    public void declareOutputFields(OutputFieldsDeclarer arg0) {
        arg0.declare(new Fields("id", "top", "nom", "position", "nbDevant", "nbDerriere","total"));
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
