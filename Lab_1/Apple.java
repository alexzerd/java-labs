
public class Apple extends Food {

    private String mSize = null;


    public Apple(String size) {
        super("Apple");
        this.mSize = size.toUpperCase();
    }

    public  void consume() {
        System.out.println(this + "ate");
    }

    @Override
    public int calculateCalories() {
        if (mSize.equals("BIG")) {
            return 100;
        } else if (mSize.equals("SMALL")) {
            return 25;
        } else {
            return 50;
        }
    }

    public boolean equals (Object arg) {
        if((super.equals(arg)) && (arg instanceof Apple)) {
            return mSize.equals(((Apple)arg).mSize);
        } else {
            return false;
        }
    }

    public void setSize(String size) {
        this.mSize = size.toUpperCase();
    }

    public String getSize() {
        return mSize;
    }

    public  String toString() {
        return  super.toString() + " size '" + mSize + "' ";
    }
}