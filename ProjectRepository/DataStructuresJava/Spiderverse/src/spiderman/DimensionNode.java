package spiderman;

public class DimensionNode {
    private int dimensionNum;          
    private int canonEvent;
    private int dimensionWeight;
    private DimensionNode next;

    public DimensionNode() { 
        this.dimensionNum = 0;
        this.canonEvent = 0;
        this.dimensionWeight = 0;
        this.next = null;
    }

    public DimensionNode(int dimensionNum, int canonEvent, int dimensionWeight, DimensionNode next) {
        this.dimensionNum = dimensionNum;
        this.canonEvent = canonEvent;
        this.dimensionWeight = dimensionWeight;
        this.next = next;
    }

   
    // "Getter" and "Setter" Methods
    public int getDimensionNum() { return dimensionNum; }
    public void setDimensionNum ( int newName ) { dimensionNum = newName; }

    public int getCanonEvent() { return canonEvent; }
    public void setCanonEvent ( int newCanonEvent ) { canonEvent = newCanonEvent; }

    public int getDimensionWeight () { return dimensionWeight; }
    public void setDimensionWeight ( int newDimensionWeight ) { dimensionWeight = newDimensionWeight; }

    public DimensionNode getNextDimensionNode() { return next; }
    public void setNextDimensionNode(DimensionNode next) { this.next = next; }
    
}
