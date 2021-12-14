//206398984

package Ex2_code;

import Ex2_code.api.DirectedWeightedGraph;
import Ex2_code.api.DirectedWeightedGraphAlgorithms;

/**
 * This class is the main class for Ex2.Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */

    public static DirectedWeightedGraph getGrapg(String json_file) {
        DirectedWeightedGraph ans = null;
        // ****** Add your code here ******
        MyAlgo algo = (MyAlgo) getGrapgAlgo(json_file);
        MyGragh g = algo.getGraph();
        ans=g;
        // ********************************
        return ans;
    }
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = null;
        // ****** Add your code here ******
        MyAlgo algo = new MyAlgo();
        algo.load(json_file);
        ans=algo;
        // ********************************
        return ans;
    }
    /**
     * This static function will run your GUI using the json fime.
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     *
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        // ****** Add your code here ******
        MyAlgo algo=(MyAlgo) alg;
        Ex2_gui ex2 = new Ex2_gui(algo);
        ex2.setVisible(true);
        // ********************************
    }

    public static void main(String[] args) {
       getGrapgAlgo(args[0]);
        //runGUI("Ex2_code/data/G1.json");
       runGUI(args[0]);
    }
}