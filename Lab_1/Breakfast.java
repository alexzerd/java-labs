
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Comparator;

public class Breakfast {

        private static int sNumberOfSandwiches = 0;
        private static boolean sFlagSort = false;
        private static boolean sFlagCalories = false;

    public static void breakfast(String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException {

        Food[] breakfast = new Food[20];

        int counter = 0;
        for(String arg: args) {

            if (arg.equals("-sort")) {
                sFlagSort = true;
                continue;
            } else if (arg.equals("-calories")) {
                sFlagCalories = true;
                continue;
            }

            String[] parts = arg.split("/");

            try {
            	
                Class someFood = Class.forName(parts[0]);
                
                if (parts.length == 1) {
                	
                    Constructor constructor = someFood.getConstructor();
                    breakfast[counter] = (Food)constructor.newInstance();
                    
                } else if (parts.length == 2) {
                	
                    Constructor constructor = someFood.getConstructor(String.class);
                    breakfast[counter] = (Food)constructor.newInstance(parts[1]);
                    
                } else if (parts.length == 3) {
                	
                    Constructor constructor = someFood.getConstructor(String.class, String.class);
                    breakfast[counter] = (Food)constructor.newInstance(parts[1], parts[2]);
                    
                    /*if (breakfast[counter] instanceof Sandwich) {
                        ++sNumberOfSandwiches;
                    }*/
                }
                
                
            } catch (ClassNotFoundException er_1) {
            	
                System.out.print(" error: ");
                System.out.println("class '" + parts[0] + "' not found");
                continue;
                
            } catch (NoSuchMethodException er_2) {
            	
                System.out.print(" error: ");
                System.out.println("suitable constructor for '" + parts[0] +
                                   "' not found");
                continue;
            }

            ++counter;
        }

        System.out.println("-------");
        
        boolean isOut = false;

        if (sFlagSort) {
        	
            Arrays.sort(breakfast, new Comparator() {
            	
                public int compare(Object first, Object second) {
                	
                    if (first == null) {
                    	
                        return 1;
                    } else if (second == null) {
                    	
                        return -1;
                    } else {
                    	
                        return ((Food)first).getName().compareTo(((Food)second).getName());
                    }
                }
                
            });
            
            
            for (int i = 0; i < counter; ++i) {
                breakfast[i].consume();
                
            }
            isOut = true;
        }
        
        if(!isOut) {
        	for (int i = 0; i < counter; ++i) {
        		breakfast[i].consume();
            
        	}
        }

        if (sFlagCalories) {
        	
            int calories = 0;
            for (int i = 0; i < counter; ++i) {
               calories += breakfast[i].calculateCalories();
            }
            System.out.println("Calories: " + calories);
        }

        //System.out.println("Sandwiches: "+ sNumberOfSandwiches);
    }
}