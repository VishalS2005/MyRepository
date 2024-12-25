import java.util.ArrayList;

/**
 * The StopAndFrisk class represents stop-and-frisk data, provided by
 * the New York Police Department (NYPD), that is used to compare
 * during when the policy was put in place and after the policy ended.
 * 
 * @author Tanvi Yamarthy
 * @author Vidushi Jindal
 */
public class StopAndFrisk {

    /*
     * The ArrayList keeps track of years that are loaded from CSV data file.
     * Each SFYear corresponds to 1 year of SFRecords. 
     * Each SFRecord corresponds to one stop and frisk occurrence.
     */ 
    private ArrayList<SFYear> database; 

    /*
     * Constructor creates and initializes the @database array
     * 
     * DO NOT update nor remove this constructor
     */
    public StopAndFrisk () {
        database = new ArrayList<>();
    }

    /*
     * Getter method for the database.
     * *** DO NOT REMOVE nor update this method ****
     */
    public ArrayList<SFYear> getDatabase() {
        return database;
    }

    /**
     * This method reads the records information from an input csv file and populates 
     * the database.
     * 
     * Each stop and frisk record is a line in the input csv file.
     * 
     * 1. Open file utilizing StdIn.setFile(csvFile)
     * 2. While the input still contains lines:
     *    - Read a record line (see assignment description on how to do this)
     *    - Create an object of type SFRecord containing the record information
     *    - If the record's year has already is present in the database:
     *        - Add the SFRecord to the year's records
     *    - If the record's year is not present in the database:
     *        - Create a new SFYear 
     *        - Add the SFRecord to the new SFYear
     *        - Add the new SFYear to the database ArrayList
     * 
     * @param csvFile
     */
    public void readFile ( String csvFile ) {

        // DO NOT remove these two lines
        StdIn.setFile(csvFile); // Opens the file
        StdIn.readLine();       // Reads and discards the header line

        // WRITE YOUR CODE HERE
        


        while (StdIn.hasNextLine()) { //this line is right
            
            String[] recordEntries = StdIn.readLine().split(",");

            int year = Integer.parseInt(recordEntries[0]); //converts string to integer
            String description = recordEntries[2];
            String gender = recordEntries[52];
            String race = recordEntries[66];
            String location = recordEntries[71];
            Boolean arrested = recordEntries[13].equals("Y"); //if equal --> yes
            Boolean frisked = recordEntries[16].equals("Y"); 
            SFRecord SFRecordtemp = new SFRecord(description, arrested, frisked, gender, race, location);

            SFYear sfYear = null;
        
            for (int i = 0; i <database.size(); i++) {          
                sfYear = database.get(i);
                if (sfYear.getcurrentYear() == year) { //checks if the year is right
                       //adds record to database
                     break;
                } 
                else{
                    sfYear=null;
                }
            }
            if (sfYear!=null)
            {
             sfYear.addRecord(SFRecordtemp); 
            }
            else{// first time
                 SFYear yearA = new SFYear(year);            //initiates new year
                 database.add(yearA); 
                 yearA.addRecord(SFRecordtemp);
            }
        }
    }
       
    

    /**
     * This method returns the stop and frisk records of a given year where 
     * the people that was stopped was of the specified race.
     * 
     * @param year we are only interested in the records of year.
     * @param race we are only interested in the records of stops of people of race. 
     * @return an ArrayList containing all stop and frisk records for people of the 
     * parameters race and year.
     */

    public ArrayList<SFRecord> populationStopped ( int year, String race ) {

        ArrayList<SFRecord> a = new ArrayList<SFRecord>();
        for (int i = 0; i < database.size(); i++) {
            ArrayList<SFRecord> records = new ArrayList<SFRecord>(); //creates array for return
            records = database.get(i).getRecordsForYear(); //copies the records from one year
            if (database.get(i).getcurrentYear()==year) { //checks if the SFYear is right
                for (int j = 0; j < records.size(); j++) {  //loops through each record of that year
                    if(records.get(j).getRace().equals(race)) { //checks if the race in the record matches the input
                        a.add(records.get(j));              //adds only the records we need to the return array
                    }
                }
            }
        }
        return a;
        // WRITE YOUR CODE HERE

    }

    /**
     * This method computes the percentage of records where the person was frisked and the
     * percentage of records where the person was arrested.
     * 
     * @param year we are only interested in the records of year.
     * @return the percent of the population that were frisked and the percent that
     *         were arrested.
     */
    public double[] friskedVSArrested ( int year ) {
        int popFrisked = 0;
        int popArrested = 0;
        int total = 0;
        for (int i = 0; i < database.size(); i++) {
            ArrayList<SFRecord> records = new ArrayList<SFRecord>();
            records = database.get(i).getRecordsForYear();
            if (database.get(i).getcurrentYear() == year) {
                for (int j = 0; j < records.size(); j++) {
                    if(records.get(j).getFrisked()) { //getFrisked method is boolean: true if Frisked and false if not
                        popFrisked++;
                    } if (records.get(j).getArrested()) {  //getArrested method is boolean: true if Arrested and false if not
                        popArrested++;
                    }
                }
                total = records.size();
            }
        }
        double[] display = new double[2];
        display[0] = ((double)popFrisked/total) * 100;
        display[1] = ((double)popArrested/total) * 100;
        // WRITE YOUR CODE HERE

        return display; // update the return value
    }

    /**
     * This method keeps track of the fraction of Black females, Black males,
     * White females and White males that were stopped for any reason.
     * Drawing out the exact table helps visualize the gender bias.
     * 
     * @param year we are only interested in the records of year.
     * @return a 2D array of percent of number of White and Black females
     *         versus the number of White and Black males.
     */
    public double[][] genderBias ( int year ) {
        double blackMen = 0;
        double blackWomen = 0;
        double whiteMen = 0;
        double whiteWomen = 0;
        double blackPop = 0;
        double whitePop = 0;
        for (int i = 0; i < database.size(); i++) {
            ArrayList<SFRecord> records = new ArrayList<SFRecord>();
            records = database.get(i).getRecordsForYear();
            if (database.get(i).getcurrentYear()==year) {
                for (int j = 0; j < records.size(); j++) {
                    if (records.get(j).getRace().equals("B")) {
                        blackPop++;
                        if (records.get(j).getGender().equals("M")) {
                            blackMen++;
                        } else if (records.get(j).getGender().equals("F")){
                            blackWomen++;
                        }
                    } else if (records.get(j).getRace().equals("W")){
                        whitePop++;
                        if (records.get(j).getGender().equals("M")) {
                            whiteMen++;
                        } else if (records.get(j).getGender().equals("F")){
                            whiteWomen++;
                        }
                    }
                }
            }
        }
        double[][] result = new double[2][3];
        result[0][0] = ((double)(blackWomen/blackPop) * 100 * 0.5);
        result[0][1] = ((double)(whiteWomen/whitePop) * 100 * 0.5);
        result[1][0] = ((double)(blackMen/blackPop) * 100 * 0.5);
        result[1][1] = ((double)(whiteMen/whitePop) * 100 * 0.5);
        result[0][2] = result[0][0] + result [0][1];
        result[1][2] = result[1][0] + result [1][1];
        // WRITE YOUR CODE HERE

        return result; // update the return value
    }

    /**
     * This method checks to see if there has been increase or decrease 
     * in a certain crime from year 1 to year 2.
     * 
     * Expect year1 to preceed year2 or be equal.
     * 
     * @param crimeDescription
     * @param year1 first year to compare.
     * @param year2 second year to compare.
     * @return 
     */

    public double crimeIncrease ( String crimeDescription, int year1, int year2 ) {
        
        // WRITE YOUR CODE HERE
        double year1crime = 0;
        double year2crime = 0;
        double year1size = 0;
        double year2size = 0;
        for (int i = 0; i < database.size(); i++) {
            
            if (database.get(i).getcurrentYear()==(year1)) {
                ArrayList<SFRecord> recordsA = new ArrayList<SFRecord>();
                recordsA = database.get(i).getRecordsForYear();
                for (int j = 0; j < recordsA.size(); j++) {
                    if(recordsA.get(j).getDescription().indexOf(crimeDescription) >= 0) { //database.get(i).getRecordsForYear().get(j).getDescription().indexOf(crimeDescription)>=0
                        year1crime++;
                    }
                }
                year1size = recordsA.size(); //191851
            }
            if (database.get(i).getcurrentYear()==(year2)) {
                ArrayList<SFRecord> recordsB = new ArrayList<SFRecord>();
                recordsB = database.get(i).getRecordsForYear();
                for (int j = 0; j < recordsB.size(); j++) {
                    if(recordsB.get(j).getDescription().indexOf(crimeDescription) >= 0) {
                        year2crime++;
                    }
                }
                year2size = recordsB.size(); //9999
            }
        }
        System.out.println(year1size); //9999
        System.out.println(year2size); //9999
        double year1per = (double)(year1crime/year1size) * 100;
        double year2per = (double)(year2crime/year2size) * 100;
        double result = year2per - year1per;
        if (year1 == year2) {
            return 0.0;
        } else if(year2 < year1) {
            return (result * -1);
        } else {
            return result;
        }
	 // update the return value
    }

    /**
     * This method outputs the NYC borough where the most amount of stops 
     * occurred in a given year. This method will mainly analyze the five 
     * following boroughs in New York City: Brooklyn, Manhattan, Bronx, 
     * Queens, and Staten Island.
     * 
     * @param year we are only interested in the records of year.
     * @return the borough with the greatest number of stops
     */
    public String mostCommonBorough ( int year ) {

        // WRITE YOUR CODE HERE
        int brooklyn = 0;
        int manhattan = 0;
        int bronx = 0;
        int queens = 0;
        int statenIsland = 0;
        for (int i = 0; i < database.size(); i++) {
            ArrayList<SFRecord> records = new ArrayList<SFRecord>();
            records = database.get(i).getRecordsForYear();
            if (database.get(i).getcurrentYear()==year) {
                for (int j = 0; j < records.size(); j++) {
                    if (records.get(j).getLocation().equalsIgnoreCase("BROOKLYN")) {
                        brooklyn++;
                    } else if (records.get(j).getLocation().equalsIgnoreCase("MANHATTAN")) {
                        manhattan++;
                    } else if (records.get(j).getLocation().equalsIgnoreCase("BRONX")) {
                        bronx++;
                    } else if (records.get(j).getLocation().equalsIgnoreCase("QUEENS")) {
                        queens++;
                    } else if (records.get(j).getLocation().equalsIgnoreCase("STATEN ISLAND")) {
                        statenIsland++;
                    }
                }
            }
        }
        String[] borough = {"Brooklyn","Manhattan","Bronx","Queens","Staten Island"};
        int[] counts = {brooklyn, manhattan, bronx, queens, statenIsland};
        int index = 0;
        int max = 0;
        String result = null;
        for (int k = 0; k < counts.length;k++) {
            if (counts[k] > max) {
                max = counts[k];
                index = k;
            }
            if (k == counts.length - 1) {
                result = borough[index];
            }
        }
        
        return result; // update the return value
    }

}
