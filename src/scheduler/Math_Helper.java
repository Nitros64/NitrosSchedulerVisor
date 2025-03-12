
package scheduler;

import java.util.ArrayList;

public class Math_Helper {    
    
   public static int mcm(int[] numbers, int length){
        if (length < 1) return 0;
	if (length == 1) return numbers[0];

	int mcm = 1;
	int div = 2;
	int unos = 0;
	boolean div_used = false;

	while (true) {
        for (int i = 0; i < length; i++) {
            if (numbers[i] % div == 0) {
                numbers[i] = numbers[i] / div;
                div_used = true;
            }
            else if (numbers[i] == 1) {
                ++unos;
            }
        }
        if (div_used)
            mcm *= div;
        else
            ++div;

        if (unos == length) break;

        unos = 0;
        div_used = false;
	}
	return mcm;
    }
    
   public static int tasks_mcm(ArrayList <Task> tasks_table){
       if (tasks_table.size() < 1) return -1;
	
	int num = 0;
	int[] numbers = new int[tasks_table.size()];

	for (int i = 0; i < tasks_table.size(); i++)
		numbers[i] = tasks_table.get(i).getT();
        
        return Math_Helper.mcm(numbers, tasks_table.size());
    }
}
