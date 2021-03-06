package stormTP.topology;


import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import stormTP.operator.*;

public class TopologyT4 {

        public static void main(String[] args) throws Exception {
            int nbExecutors = 1;
            int portINPUT = 9001;
            int portOUTPUT = 9002;
            String ipmINPUT = "224.0.0." + args[0];
            String ipmOUTPUT = "225.0." + args[0] + "." + args[1];

		/*Création du spout*/
            MasterInputStreamSpout spout = new MasterInputStreamSpout(portINPUT, ipmINPUT);
    	/*Création de la topologie*/
            TopologyBuilder builder = new TopologyBuilder();
        /*Affectation à la topologie du spout*/
            builder.setSpout("masterStream", spout);
        /*Affectation à la topologie du bolt qui ne fait rien, il prendra en input le spout localStream*/
            builder.setBolt("myTortoise", new MyTortoiseBolt(), nbExecutors).shuffleGrouping("masterStream");
            builder.setBolt("giveRank", new GiveRankBolt(), nbExecutors).shuffleGrouping("myTortoise");
            builder.setBolt("computeBonus", new ComputeBonusBolt(), nbExecutors).shuffleGrouping("giveRank");
        /*Affectation à la topologie du bolt qui émet le flux de sortie, il prendra en input le bolt nofilter*/
            builder.setBolt("exit", new Exit4Bolt(portOUTPUT, ipmOUTPUT), nbExecutors).shuffleGrouping("computeBonus");

        /*Création d'une configuration*/
            Config config = new Config();
        /*La topologie est soumise à STORM*/
            StormSubmitter.submitTopology("topoT4", config, builder.createTopology());
        }
}
