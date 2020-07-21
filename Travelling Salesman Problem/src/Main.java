import com.sun.org.apache.xpath.internal.objects.XBoolean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        int numberOfCities = 0;
        //List of cities
        ArrayList<City> cities = new ArrayList<>();

        try {
            //gets the default application path
            String path = System.getProperty("user.dir");
            //opens a file and reads the file
            File file = new File(path + "\\example-input-2.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (!line.isEmpty()) {
                        //split the read line into x, y, and r
                        String[] cityInfo = line.split(" ");
                        int cityID = Integer.parseInt(cityInfo[0]);
                        int x = Integer.parseInt(cityInfo[1]);
                        int y = Integer.parseInt(cityInfo[2]);
                        City city = new City(cityID, x, y);
                        cities.add(city);
                        numberOfCities++;
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Total number of cites: " + numberOfCities);
        System.out.println("-------------------------");

        ArrayList<City> solution = FindPath(cities);

        System.out.println("GREEDY ALGORITHM TOUR LENGTH:");
        System.out.println(TourLength(solution));
        System.out.println("-------------------------");

        ArrayList<City> optimizedTour = new ArrayList<>(TwoOpt(solution));

        int optimizedTourLength = TourLength(optimizedTour);
        System.out.println("TWO-OPT OPTIMIZATION TOUR LENGTH: ");
        System.out.println(optimizedTourLength);


        try {
            //create the output file
            BufferedWriter outputFile = new BufferedWriter(new FileWriter("example-output-2.txt"));

            outputFile.write(optimizedTourLength + "\n");

            for (City city : optimizedTour) {
                //writes the outputs to the output file
                outputFile.write(city.getCityID()+ "\n");
            }

            outputFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<City> FindPath(ArrayList<City> cities){
        ArrayList<City> solution = new ArrayList<>();
        ArrayList<City> freeCities = new ArrayList<>(cities);
        City city = cities.get(0);
        freeCities.remove(city);
        solution.add(city);

        int totalTravel = 0;

        while (freeCities.size() > 0) {
            double minLength = 0;
            City minNode = null;
            for (City i : freeCities){
                double length = city.distanceToCity(i);
                if(minLength == 0){
                    minLength = length;
                    minNode = i;
                }else if (length < minLength ) {
                    minLength = length;
                    minNode = i;
                }
            }
            totalTravel += minLength;
            solution.add(minNode);
            freeCities.remove(minNode);;
            city = minNode;
        }

        if(TourLength(cities) > TourLength(solution))
            return solution;
        else
            return cities;
    }

    public static ArrayList<City> TwoOpt(ArrayList<City> tour){

        ArrayList<City> newTour;
        int bestTourLength = TourLength(tour);
        int newTourLength;
        int swaps = 1;
        int improve = 0;
        int iterations = 0;
        int comparisons = 0;

        //Loop until there are no improvement left
        while (swaps != 0) {
            swaps = 0;

            int numberOfCities = tour.size();
            for (int i = 1; i < numberOfCities - 2; i++) {
                for (int j = i + 1; j < numberOfCities - 1; j++) {
                    comparisons++;

                    double first_second = tour.get(i).distanceToCity(tour.get(i - 1));
                    double third_fourth = tour.get(j + 1).distanceToCity(tour.get(j));
                    double first_third = tour.get(i).distanceToCity(tour.get(j + 1));
                    double second_forth = tour.get(i - 1).distanceToCity(tour.get(j));

                    if( first_second + third_fourth >= first_third + second_forth) {

                        newTour = swapCities(tour, i, j);

                        newTourLength = TourLength(newTour);

                        if(newTourLength < bestTourLength){
                            tour = newTour;
                            bestTourLength = newTourLength;
                            swaps++;
                            improve++;
                        }
                    }
                }
            }
            iterations++;
        }

        return tour;
    }

    //Calculate the total length of the route
    public static int TourLength(ArrayList<City> cities) {
        int result = 0;
        City previousCity = cities.get(cities.size() - 1);
        //Traverse all cities
        for (City city : cities) {
            //Get distance from previous city
            result += city.distanceToCity(previousCity);
            //Current city is now previous city
            previousCity = city;

        }


        return result;
    }

    //Swaps two cities by inverting the order of cities between i and j
    public static ArrayList<City> swapCities(ArrayList<City> tour, int i, int j){
        ArrayList<City> newTour = new ArrayList<>();

        //fill the new tour until ith city
        for(int x = 0; x < i; x++){
            newTour.add(tour.get(x));
        }
        //fill the new tour from ith city to jth city in reverse order
        int change = 0;
        for(int y = i; y <= j; y++) {
            newTour.add(tour.get(j - change));
            change++;
        }
        //fill the new tour with the rest of the cities from jth city
        int cities = tour.size();
        for(int z = j + 1; z < cities; z++){
            newTour.add(tour.get(z));
        }

        return newTour;
    }

    public static void printCitiesOneLine(ArrayList<City> cities) {
        for (City city : cities) {
            System.out.print(city.getCityID() + " ");
        }
        System.out.println();
    }

}
