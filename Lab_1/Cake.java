
public class Cake extends Food {

    private String mIcing = null;


    public Cake(String icing) {
        super("Cake");
        this.mIcing = icing.toUpperCase();
    }

    public void consume() {
        System.out.println(this + "ate");
    }

    @Override
    public int calculateCalories() {
        if(mIcing.equals("CHOCOLATE")) {
            return 1;
        } else if (mIcing.equals("CREAMY")) {
            return 1;
        } else {
            return 1;
        }
    }

    public boolean equals (Object arg) {
        if(super.equals(arg) && arg instanceof Cake) {
            return mIcing.equals(((Cake)arg).mIcing);
        } else {
            return false;
        }
    }

    public void setIcing(String icing) {
       this.mIcing = icing.toUpperCase();
    }

    public  String getIcing() {
        return mIcing;
    }

    public String toString() {
        return super.toString() + " with '" + mIcing + "' icing ";
    }
}