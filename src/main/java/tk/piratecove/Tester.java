package tk.piratecove;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Tester {
    public static void main(String[] args) {
        try{
            File file = new File("C:\\MCServerFiles\\playerHomes.txt");
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                String[] data = scanner.nextLine().split(";");
                String[] locationData = data[1].split(":");

                System.out.println("Data: " + Arrays.toString(data));
                System.out.println("LocationData: " + Arrays.toString(locationData));

                System.out.println("Playername: " + data[0]);
                System.out.println("X: " + locationData[0]);
                System.out.println("Y: " + locationData[1]);
                System.out.println("Z: " + locationData[2]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException in readPLayerHomes");
        }
    }
}
