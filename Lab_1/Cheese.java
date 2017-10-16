
public class Cheese extends Food {

    public Cheese() {
        super("Cheese");
    }

    public void consume() {
        System.out.println(this + " ate");
    }

    @Override
    public int calculateCalories() {
        return 100;
    }
}
