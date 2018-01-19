package stormTP.Util;

public class Personne {
    String nom;
    int nbDevant;

    public Personne(String s, int nb){
        nom = s;
        nbDevant = nb;
    }

    public Boolean tri(Personne p){
        if( p.nbDevant > this.nbDevant){
            return false;
        }
        else{
            return true;
        }
    }

    public int getNbDevant() {
        return nbDevant;
    }

    public String getNom() {
        return nom;
    }

    public String toString(){
        return this.nom + " : " + this.nbDevant;
    }
}
