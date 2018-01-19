package stormTP.core;

import stormTP.Util.Personne;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 Classe regroupant les fonctionnalités nécessaires au traitement des flux des coureurs
*/
public class TortoiseManager {
	
	public static final String CONST = "Constant";
	public static final String PROG = "En progression";
	public static final String REGR = "En régression";
	
	String nomsBinome ="";
	long dossard = -1;
	
	public TortoiseManager(long dossard, String nomsBinome){
		this.nomsBinome = nomsBinome;
		this.dossard = dossard;
	}
	
	
	/**
	 * Permet de filtrer les informations concernant votre coureur
	 * @param input : objet JSON contenant les observations de la course courante 
	 * @return un coureur
	 */
	public Runner filter(String input){
		
		Runner tortoise = null;
		String[] split = input.split("\\{", 0);

		for (int i = 2; i < split.length - 1; i++) {
			if (split[i].contains("\"id\":" + this.dossard + ",")) {
				tortoise = new Runner(
				this.dossard,
				this.nomsBinome,
				Integer.parseInt(split[i].substring(split[i].indexOf(":",split[i].indexOf("nbDevant"))+1,split[i].indexOf("nbDerriere")-2)),
				Integer.parseInt(split[i].substring(split[i].indexOf(":",split[i].indexOf("nbDerriere"))+1,split[i].indexOf("total")-2)),
				Integer.parseInt(split[i].substring(split[i].indexOf(":",split[i].indexOf("total"))+1,split[i].lastIndexOf(",")-1)),
				Integer.parseInt(split[i].substring(split[i].indexOf(":",split[i].indexOf("position"))+1,split[i].indexOf("nbDevant")-2)),
				Long.parseLong(split[i].substring(split[i].indexOf(":",split[i].indexOf("top"))+1,split[i].lastIndexOf("position")-2))
				);
			}
		}

		System.out.println(input);

		return tortoise;
	}
	
	/**
	 * Permet de calculer le rang de votre coureur
	 * @param id : identifiant du coureur
	 * @param top : numéro d'observation
	 * @param nom : nom du coureur
	 * @param nbDevant : nombre de coureur devant le coureur courant dans le classement
	 * @param nbDerriere : nombre de coureur derrière le coureur courant dans le classement
	 * @param total : nombre total de coureurs en lice
	 * @return un coureur
	 */
	public Runner computeRank(long id, long top, String nom, int nbDevant, int nbDerriere, int total){
		
		Runner tortoise = new Runner();

		tortoise.setId(id);
		tortoise.setTop(top);
		tortoise.setNom(nom);
		tortoise.setTotal(total);
		if(nbDevant+nbDerriere == total-1) {
			tortoise.setRang((nbDevant + 1) + "");
		}
		else{
			tortoise.setRang((nbDevant + 1) + "ex");
		}
	
		return tortoise;
	}
	
	
	/**
	 * Permet de calculer les points bonus d'un coureur
	 * @param rang : rang du coureur
	 * @param total : nombre total de coureurs en lice
	 * @return nombre de points gagnés par le coureur
	 */
	public static int computePoints(String rang,  int total){
		
                int points = -1;

				if(rang.contains("ex")){
					points = total - Integer.parseInt(rang.replace("ex", ""));
				}else{
					points = total - Integer.parseInt(rang);
				}
		return points;
	}
	

	/**
	 * Permet de calculer la vitesse d'un coureur
	 * @param topInit : numéro d'observation initial
	 * @param topFin : numéro d'observation final
	 * @param posInit : position initiale du coureur sur la piste à l'observation initiale
	 * @param posFin : position finiale du coureur sur la piste à l'observation finale
	 * @return la vitesse du coueur (en nombre moyen de cellules par top d'observation) arrondie à 2 chiffres après la virgule
	 */
	public static double computeSpeed(long topInit, long topFin, int posInit, int posFin){
		
		double vitesse = -0.0;
		vitesse = (float)(posFin-posInit)/ (float)(topFin-topInit);

		return Math.round(vitesse*100) / 100.0;
		
	}
	
	
	/**
	 * Permet de calculer le rang moyen d'un coureur
	 * @param rangs : tableau des différentes valeurs de rang observées pour un coureur
	 * @return la moyenne des rangs
	 */
	public static int giveAverageRank(String[] rangs){
				
		
		int rang = 0;

		for(String i : rangs){
			if(i.contains("ex")){
				rang += Integer.parseInt(i.replace("ex", ""));
			}else{
				 rang += Integer.parseInt(i);
			}
		}

		rang /= rangs.length;
		
		return rang;
		
	}
	

	/**
	 * Permet de calculer l'évolution du rang moyen d'une tortue
	 * @param cavg : le rang moyen courant
	 * @param pavg : le rang moyen précédent
	 * @return la chaine de caractères correspondante à l'évolution du rang moyen 
	 */
	public static String giveRankEvolution(int cavg, int pavg){
				
		String evol = "";
		
		if( cavg < pavg){
			evol = PROG;
		}
		else if(cavg > pavg){
			evol = REGR;
		}
		else{
			evol = CONST;
		}
		
		return evol;
	}
	
	
	/**
	 * Permet de calculer le podium
	 * @param input : objet JSON correspondant aux observations de la course
	 * @return objet JSON correspondant au podium
	 */
	public static String getPodium(String input){
		String[] split = input.split("\\{", 0);

		JsonArrayBuilder marche1Builder = Json.createArrayBuilder();
		JsonArrayBuilder marche2Builder = Json.createArrayBuilder();
		JsonArrayBuilder marche3Builder = Json.createArrayBuilder();
		ArrayList<String> marche1 = new ArrayList<>();
		ArrayList<String> marche2 = new ArrayList<>();
		ArrayList<String> marche3 = new ArrayList<>();

		int top = 0;
		String test ="";

		HashMap<Integer, String> map = new HashMap<>();
		ArrayList<Personne> lp = new ArrayList<>();

		// on récupère le top, les places et les noms
		for(int i = 2; i < split.length; i++){
			if(i==2){
				top = Integer.parseInt(split[i].substring(split[i].indexOf(":",split[i].indexOf("top"))+1,split[i].lastIndexOf("nom")-2));
			}

			int nbDevant = Integer.parseInt(split[i].substring(split[i].indexOf(":",split[i].indexOf("nbDevant"))+1,split[i].indexOf("nbDerriere")-2));
			String nom = split[i].substring(split[i].indexOf(":",split[i].indexOf("nom"))+2,split[i].indexOf("position")-3);

			lp.add(new Personne(nom, nbDevant));
		}

		//Ordonné par nbDevant
		Collections.sort(lp, new Comparator<Personne>() {
			@Override
			public int compare(Personne t1, Personne t2) {
				if( t1.getNbDevant() == t2.getNbDevant()){
					return 0;
				}
				else if (t1.getNbDevant() < t2.getNbDevant()){
					return -1;
				}else{
					return 1;
				}

			}
		});

		System.out.println(lp);

		// test podium
		for (int i = 0; i < lp.size() ; i++) {

			if(lp.get(i).getNbDevant() == 0) {
				marche1.add(lp.get(i).getNom());
			}
			else if(lp.get(i).getNbDevant() == marche1.size()) {
				marche2.add(lp.get(i).getNom());
			}
			else if(lp.get(i).getNbDevant() == (marche1.size()+marche2.size())) {
				marche3.add(lp.get(i).getNom());
			}
		}

		Collections.sort(marche1);
		Collections.sort(marche2);
		Collections.sort(marche3);
		for(int i = 0; i < marche1.size(); i++) {
			marche1Builder.add(Json.createObjectBuilder().add("nom", marche1.get(i)));
		}
		for(int i = 0; i < marche2.size(); i++) {
			marche2Builder.add(Json.createObjectBuilder().add("nom", marche2.get(i)));
		}
		for(int i = 0; i < marche3.size(); i++) {
			marche3Builder.add(Json.createObjectBuilder().add("nom", marche3.get(i)));
		}

		JsonObjectBuilder podiumJson =  Json.createObjectBuilder();
		podiumJson.add("top", top);
		podiumJson.add("marcheP1", marche1Builder);
		podiumJson.add("marcheP2", marche2Builder);
		podiumJson.add("marcheP3", marche3Builder);

		return podiumJson.build().toString();
		
	}

	
	/**
	 * S'assure que le podium est complet et réaffecte le classement en fonction des ex aequo.
	 * @param runners : tableau de coureurs classé par leur rang
	 * @return le podium sous forme de liste de liste de coureurs
	 */
	private static ArrayList<ArrayList<String>> computePodium(Runner[] runners){
		
		ArrayList<ArrayList<String>> realrank = new ArrayList<ArrayList<String>>();
		
		/* Initialisation des listes */
		for(int i = 0 ; i < runners.length; i++){
			realrank.add(i, new ArrayList<String>());
		}
		
		/* affectation des runner en fonction de leur position */
		for(int i = 0 ; i < runners.length; i++){
			realrank.get(runners[i].getNbDevant()).add(runners[i].getNom());
		}
		
		/* suppression des rangs vides dus aux ex aequo */
		int cpt = 0;
		int nbmaxiter = realrank.size();
		for(int i = 0 ; i < nbmaxiter; i++){
			if( realrank.get(cpt).size() == 0 ){
				realrank.remove(cpt);
			}else{
				cpt++;
			}
		}
		
		ArrayList<ArrayList<String>> marchesPodium = new ArrayList<ArrayList<String>>();
		
		/* transfert des données */
		for( int i = 0 ; i < Math.min(3,realrank.size())  ; i++){
			marchesPodium.add(i, realrank.get(i));
		}
		
		return marchesPodium;
		
	}

}
