package TP1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class World {

    private ArrayList<Aeroport> list;


    /*
     * Création du constructeur
     * 
     * Le constructeur devra donc permettre d'ouvrir un fichier csv
     * 
     */

    public World(String fileName) {
        list = new ArrayList<>(); // Déclarattion de la liste qui recevra toutes les informations du fichier CSV

        try {
            BufferedReader buf = new BufferedReader(new FileReader(fileName)); // Ouverture du fichier
            String s = buf.readLine(); // Variable qui reçevra l'entiereté des données du fichier

            /*
             * La boucle while lira ligne par ligne en récupérant chaque informations ligne par ligne
             * puis seront ajouté dans une liste créer avec la classe Aéroport
             */

            while (s != null) {
                s = s.replaceAll("\"", "");
                String[] fields = s.split(",");

                if (fields.length > 11 && fields[1].equals("large_airport")) {
                    String IATA = fields[9];
                    String name = fields[2];
                    String country = fields[5];

                    String coord = fields[11];
                    String[] coords = coord.split(" ");
                    double longitude = 0, latitude = 0;

                    if (coords.length >= 2) {
                        longitude = Double.parseDouble(coords[0]);
                        latitude = Double.parseDouble(coords[1]);
                    }

                    list.add(new Aeroport(IATA, name, country, latitude, longitude));
                }

                s = buf.readLine();
            }

            buf.close(); //ferme le fichier
        } 
        
        /*
         * En cas d'erreur renvoi un message d'erreur
         */

        catch (Exception e) {
            System.out.println("Maybe the file isn't there?");
            System.out.println(list.get(list.size()-1));
            e.printStackTrace();
        }
    }

    /*
     * Fonction qui permet de de trouvé un Code dans la liste créé avec la classe Aéroport
     * 
     * puis retourne la liste qui contient le CODE
     */

    public Aeroport findByCode(String code) {
        for (Aeroport a : list) {
            if (a.getIATA().equalsIgnoreCase(code)) {
                return a;
            }
        }
        return null;
    }

    /*
     * Fonction qui sert à trouver l'aéroport le plus proche de celui mis en argument
     * 
     * Le programme utilise la fonction calculDistance de la classe Aeroport
     * 
     */

    public Aeroport findNearestAirport(double longitude, double latitude) {
        Aeroport closest = null;
        double minDistance = Double.MAX_VALUE;
        Aeroport temp = new Aeroport("TMP", "Temp", "N/A", latitude, longitude);

        /*
         * Boucle for qui parcours la liste d'aéroport en calculant la distance en comparant si la distance calculé est
         * inférieur à la distance minimal
         */
        for (Aeroport a : list) {
            double d = a.calculDistance(temp);
            if (d < minDistance) {
                minDistance = d;
                closest = a;
            }
        }
        return closest;
    }

    public ArrayList<Aeroport> getList() {
        return list;
    }

    /*
     * fonction du calcul de la distance
     */
    private double distance(double lon1, double lat1, double lon2, double lat2) {
        double theta1 = Math.toRadians(lat1);
        double theta2 = Math.toRadians(lat2);
        double phi1 = Math.toRadians(lon1);
        double phi2 = Math.toRadians(lon2);

        return Math.pow(theta2 - theta1, 2)
             + Math.pow((phi2 - phi1) * Math.cos((theta2 + theta1) / 2), 2);
    }

    public static void main(String[] args){

        World w = new World ("src\\TP1\\airport-codes_no_comma.csv");
        System.out.println("Found "+w.getList().size()+" airports.");
        Aeroport paris = w.findNearestAirport(2.316,48.866);
        Aeroport cdg = w.findByCode("CDG");
        double distance = w.distance(2.316,48.866,paris.getLongitude(),paris.getLatitude());
        System.out.println(paris);
        System.out.println(distance);
        double distanceCDG = w.distance(2.316,48.866,cdg.getLongitude(),cdg.getLatitude());
        System.out.println(cdg);
        System.out.println(distanceCDG);
        
    }

}
