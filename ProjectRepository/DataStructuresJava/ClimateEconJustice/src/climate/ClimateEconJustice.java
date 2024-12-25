package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {
        //add a new StateNode to the end of the firstState instance variable
        //How to use split:
        //Call it on a string with a comma string as a parameter - it will return an array of Strings (String[]). 
        //You are supposed to split the input line by commas in order to extract data from the line.
        String[] a = inputLine.split(",");
        StateNode object = new StateNode(a[2],null,null);
        
        if (firstState == null) {
            firstState = object;
            return;
        } else {
            StateNode prev = null;
            for (StateNode ptr = firstState; ptr != null; ptr = ptr.next) {
                if (object.getName().equals(ptr.getName())) {
                    return;//add object to firstState     firstState, obj1, obj2, objn null
                                                                        //        prev  ptr
                } 
                prev = ptr;
            }
            prev.next = object;  
        }
        // WRITE YOUR CODE HERE
    }
    
    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {
        String[] a = inputLine.split(",");
        CountyNode county = new CountyNode(a[1],null,null);
        for(StateNode sptr = firstState; sptr != null; sptr = sptr.next) {
            if (sptr.getName().equals(a[2])) {
                if (sptr.getDown() == null) {
                    sptr.setDown(county);
                    return;
                } else { // 3 1 3 2 3 
                    CountyNode prev = null;
                    CountyNode firstCounty = sptr.getDown();
                    for (CountyNode cptr = firstCounty; cptr!= null; cptr = cptr.next) {
                        if (cptr.getName().equals(county.getName())) {
                            return;
                        }
                        prev = cptr;
                    }
                    prev.next = county;
                }
            }
        }
        // WRITE YOUR CODE HERE
    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {
        String[] a = inputLine.split(",");
        Data data = new Data(Double.parseDouble(a[3]), Double.parseDouble(a[4]),
        Double.parseDouble(a[5]), Double.parseDouble(a[8]), Double.parseDouble(a[9]), 
        a[19], Double.parseDouble(a[49]), Double.parseDouble(a[37]), Double.parseDouble(a[121]));
        CommunityNode comm = new CommunityNode(a[0], null, data);
        for(StateNode sptr = firstState; sptr != null; sptr = sptr.next) {
            if (sptr.getName().equals(a[2])) {
                CountyNode firstCounty = sptr.getDown();
                for(CountyNode cptr = firstCounty; cptr!= null; cptr = cptr.next) {
                    if(cptr.getName().equals(a[1])) {
                        if (cptr.getDown() == null) {
                            cptr.setDown(comm);
                            return;
                        } else {
                            CommunityNode prev = null;
                            CommunityNode firstComm = cptr.getDown();
                            for (CommunityNode comptr = firstComm; comptr!= null; comptr = comptr.next){
                                if (comptr.getName().equals(comm.getName())) {
                                    return;
                                }
                                prev = comptr;
                            }
                            prev.next = comm;
                        }   
                    }
                }
            }
        }
        // WRITE YOUR CODE HERE
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {
        int disadComm = 0;
        
        for (StateNode sptr = firstState; sptr != null; sptr = sptr.next) {
            CountyNode firstCounty = sptr.getDown();
            for(CountyNode cptr = firstCounty; cptr != null; cptr = cptr.next) {
                CommunityNode firstComm = cptr.getDown();
                for (CommunityNode comptr = firstComm; comptr!= null; comptr = comptr.next){
                    if(race.equals("African American")) {
                        if (comptr.getInfo().getAdvantageStatus().equals("True")) {
                            if((100*comptr.getInfo().getPrcntAfricanAmerican()) >= userPrcntage) {
                                disadComm++;
                            }
                        }
                    } else if(race.equals("Native American")) { 
                        if (comptr.getInfo().getAdvantageStatus().equals("True")) {
                            if((100*comptr.getInfo().getPrcntNative()) >= userPrcntage) {
                                disadComm++;
                            }
                        }
                    } else if(race.equals("Asian American")) { 
                        if (comptr.getInfo().getAdvantageStatus().equals("True")) {
                            if((100*comptr.getInfo().getPrcntAsian()) >= userPrcntage) {
                                disadComm++;
                            }
                        }
                    } else if(race.equals("White American")) { 
                        if (comptr.getInfo().getAdvantageStatus().equals("True")) {
                            if((100*comptr.getInfo().getPrcntWhite()) >= userPrcntage) {
                                disadComm++;
                            }
                        }
                    } else if(race.equals("Hispanic American")) { 
                        if (comptr.getInfo().getAdvantageStatus().equals("True")) {
                            if((100*comptr.getInfo().getPrcntHispanic()) >= userPrcntage) {
                                disadComm++;
                            }
                        }
                    }
                }
            }
        }
        // WRITE YOUR CODE HERE
        return disadComm; // replace this line
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {
        int adComm = 0;
        
        for (StateNode sptr = firstState; sptr != null; sptr = sptr.next) {
            CountyNode firstCounty = sptr.getDown();
            for(CountyNode cptr = firstCounty; cptr != null; cptr = cptr.next) {
                CommunityNode firstComm = cptr.getDown();
                for (CommunityNode comptr = firstComm; comptr!= null; comptr = comptr.next){
                    if(race.equals("African American")) {
                        if (comptr.getInfo().getAdvantageStatus().equals("False")) {
                            if((100*comptr.getInfo().getPrcntAfricanAmerican()) >= userPrcntage) {
                                adComm++;
                            }
                        }
                    } else if(race.equals("Native American")) { 
                        if (comptr.getInfo().getAdvantageStatus().equals("False")) {
                            if((100*comptr.getInfo().getPrcntNative()) >= userPrcntage) {
                                adComm++;
                            }
                        }
                    } else if(race.equals("Asian American")) { 
                        if (comptr.getInfo().getAdvantageStatus().equals("False")) {
                            if((100*comptr.getInfo().getPrcntAsian()) >= userPrcntage) {
                                adComm++;
                            }
                        }
                    } else if(race.equals("White American")) { 
                        if (comptr.getInfo().getAdvantageStatus().equals("False")) {
                            if((100*comptr.getInfo().getPrcntWhite()) >= userPrcntage) {
                                adComm++;
                            }
                        }
                    } else if(race.equals("Hispanic American")) { 
                        if (comptr.getInfo().getAdvantageStatus().equals("False")) {
                            if((100*comptr.getInfo().getPrcntHispanic()) >= userPrcntage) {
                                adComm++;
                            }
                        }
                    }
                }
            }
        }
        // WRITE YOUR CODE HERE
        return adComm; // replace this line
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {
        ArrayList<StateNode> a = new ArrayList<StateNode>();
        for (StateNode sptr = firstState; sptr != null; sptr = sptr.next) {
            CountyNode firstCounty = sptr.getDown();
            for(CountyNode cptr = firstCounty; cptr != null; cptr = cptr.next) {
                CommunityNode firstComm = cptr.getDown();
                for (CommunityNode comptr = firstComm; comptr!= null; comptr = comptr.next){
                    if (comptr.getInfo().getPMlevel() >= PMlevel) {
                        boolean foo = false;
                        for(int i = 0; i < a.size(); i++) {
                            if(a.get(i).equals(sptr)) {
                                foo = true;
                                break;
                            }
                        }
                        if(!foo) a.add(sptr);
                    }
                }
            }
        }
        // WRITE YOUR METHOD HERE
        return a; // replace this line TestCommunityData.csv
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {
        int numComm = 0;
        
        for (StateNode sptr = firstState; sptr != null; sptr = sptr.next) {
            CountyNode firstCounty = sptr.getDown();
            for(CountyNode cptr = firstCounty; cptr != null; cptr = cptr.next) {
                CommunityNode firstComm = cptr.getDown();
                for (CommunityNode comptr = firstComm; comptr!= null; comptr = comptr.next){
                    if (comptr.getInfo().getChanceOfFlood() >= userPercntage) {
                        numComm++;
                    }                   
                }
            }
        }
        // WRITE YOUR METHOD HERE
        return numComm; // replace this line
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {
        ArrayList<CommunityNode> a = new ArrayList<CommunityNode>();
        for (StateNode sptr = firstState; sptr != null; sptr = sptr.next) {
            if (sptr.getName().equals(stateName)) {
                CountyNode firstCounty = sptr.getDown();
                for(CountyNode cptr = firstCounty; cptr != null; cptr = cptr.next) {
                    CommunityNode firstComm = cptr.getDown();
                    for (CommunityNode comptr = firstComm; comptr!= null; comptr = comptr.next){
                        if (a.size() < 10 ) {
                            a.add(comptr);
                        } 
                        else if (a.size() == 10) {
                            int index = 0;
                            double min = a.get(0).getInfo().getPercentPovertyLine();
                            for (int i = 1; i < a.size(); i++) { // 2 4 2 1 7
                                if (a.get(i).getInfo().getPercentPovertyLine() < min) {
                                    min = a.get(i).getInfo().getPercentPovertyLine();
                                    index = i;
                                }
                            }
                            if(comptr.getInfo().getPercentPovertyLine() > min) a.set(index, comptr);
                        }
                    }
                }
            }
        }
        //WRITE YOUR METHOD HERE
        return a; // replace this line
    }
}



    
