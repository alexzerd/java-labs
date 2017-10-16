
public class IceCream extends Food {

    private String mSirup = null;


    public IceCream(String sirup) {
        super("IceCream");
        this.mSirup = sirup.toUpperCase();
    }

    public void consume() {
        System.out.println(this + "ate");
    }

    @Override
    public int calculateCalories() {
        if(mSirup.equals("CARAMEL")) {
            return 115;
        } else {
            return 100;
        }
    }

    public boolean equals(Object arg) {
        if(super.equals(arg) && arg instanceof IceCream) {
            return this.mSirup.equals(((IceCream)arg).mSirup);
        } else {
            return false;
        }
    }

    public void setSirup(String sirup) {
        this.mSirup = sirup.toUpperCase();
    }

    public String getSirup() {
        return mSirup;
    }

    public String toString() {
        return super.toString() + " with '" + mSirup + "' sirup ";
    }
}