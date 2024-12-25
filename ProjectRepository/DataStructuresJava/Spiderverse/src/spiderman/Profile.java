package spiderman;

public class Profile {
    private int currentDimension;          
    private String name;
    private int dimensionSignature;
    private Profile next;

    //Default Constructor
    public Profile() { 
        this.currentDimension = 0;
        this.name = null;
        this.dimensionSignature = 0;
        this.next = null;
    }

    public Profile(int currentDimension, String name, int dimensionSignature, Profile next) {
        this.currentDimension = currentDimension;
        this.name = name;
        this.dimensionSignature = dimensionSignature;
        this.next = next;
    }

   
    // "Getter" and "Setter" Methods
    public int getCurrentDimension() { return currentDimension; }
    public void setCurrentDimension ( int newDimension ) { currentDimension = newDimension; }

    public String getName() { return name; }
    public void setName ( String newName ) { name = newName; }

    public int getDimensionSignature () { return dimensionSignature; }
    public void setDimensionSignature ( int newDimensionSignature ) { dimensionSignature = newDimensionSignature; }

    public Profile getNextProfile() { return next; }
    public void setNextProfile(Profile next) { this.next = next; }
    
}
