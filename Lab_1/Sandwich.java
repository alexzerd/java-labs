
public class Sandwich extends Food {

    private static final int NUMBER_OF_ATTRIBUTES = 2;
    private String[] mComponents = new String[NUMBER_OF_ATTRIBUTES];


    public Sandwich(String componentNo1, String componentNo2) {
        super("Sandwich");
        this.mComponents[0] = componentNo1.toUpperCase();
        this.mComponents[1] = componentNo2.toUpperCase();
    }

    public void consume() {
        System.out.println(this + "ate");
    }

    @Override
    public int calculateCalories() {
        int calories = 0;
        for (int i = 0; i < NUMBER_OF_ATTRIBUTES; ++i) {
            if(mComponents[i].equals("HAM")) {
                calories += 100;
            } else if (mComponents[i].equals("MAYO")) {
                calories += 100;
            } else {
                calories += 100;
            }
        }
        return calories;
    }

    public boolean equals (Object arg) {
        if((super.equals(arg)) && (arg instanceof Sandwich)) {
            for (int i = 0; i < NUMBER_OF_ATTRIBUTES; ++i) {
                if (mComponents[i].equals(((Sandwich)arg).mComponents[i])) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void setComponent(int whichComponent, String component) {
        this.mComponents[whichComponent] = component.toUpperCase();
    }

    public String getComponentNo1(int whichComponent) {
        return mComponents[whichComponent];
    }

    public  String toString() {
        return super.toString() + " fillingNo1 '" + mComponents[0] + "', fillingNo2 '" + mComponents[1] + "' ";
    }
}