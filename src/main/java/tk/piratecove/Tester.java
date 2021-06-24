package tk.piratecove;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Tester {
    public static void main(String[] args) {
        try {
            /*

            Map<String, Map<String, String>> result = new HashMap<>();

            Map<String, String> capsizeAchievements = new HashMap<>();
            capsizeAchievements.put("deathCounter", "2");
            capsizeAchievements.put("killCounter", "0");
            capsizeAchievements.put("dcounter", "69");
            result.put("Capsize",capsizeAchievements);

            Map<String, String> lavatnt2955Achievements = new HashMap<>();
            lavatnt2955Achievements.put("deathCounter", "2");
            lavatnt2955Achievements.put("killCounter", "0");
            lavatnt2955Achievements.put("dcounter", "1");
            result.put("Lava",lavatnt2955Achievements);

            System.out.println(result);
            */
            Object object = new JSONParser().parse(new FileReader("C:\\MCServerFiles\\achievements.json"));
            JSONObject jo = (JSONObject) object;

            //System.out.println(jo);

            for (Object string : jo.keySet()) {
                //System.out.println(string);
                /*
                Map address = (Map) jo.get(string);
                Iterator<Map.Entry> itr1 = address.entrySet().iterator();
                while (itr1.hasNext()) {
                    var pair = itr1.next();
                    System.out.println(pair.getKey() + " : " + pair.getValue());
                }
                System.out.println();

                 */
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }


        ArrayList<String> registeredPlayers = new ArrayList<>();
        try {
            Object object = new JSONParser().parse(new FileReader("C:\\MCServerFiles\\achievements.json"));
            JSONObject jo = (JSONObject) object;
            for (Object string : jo.keySet()) {
                if (!registeredPlayers.contains(string.toString())) {
                    registeredPlayers.add(string.toString());
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(registeredPlayers);
    }
}
