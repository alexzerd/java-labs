
public abstract class Food implements Consumable, Nutrious{

	   String mName = null;


	    public Food(String name) {
	        this.mName = name;
	    }

	    public boolean equals(Object arg0) {
	        if (arg0 instanceof Food &&
	            mName != null && ((Food)arg0).mName != null) {
	            return mName.equals(((Food)arg0).mName);
	        } else {
	            return false;
	        }
	    }

	    public void setName(String name) {
	        this.mName = mName;
	    }

	    public String getName() {
	        return mName;
	    }

	    public String toString() {
	        return mName;
	}
	    
}
