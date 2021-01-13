//Algorthim project 
package bridge.to.nowhere;

//______________________________________________________________________________
//graph imports
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
//GUI imports
import javax.swing.*;
import java.awt.*;
import java.util.Collection;
//read from file imports
import java.util.Scanner;
import java.io.File;
import static javafx.scene.input.KeyCode.J;

//______________________________________________________________________________
public class BridgeToNowhere {

    static Scanner input = new Scanner(System.in);

    // instance variables
    static Scanner readFile;
    static int northCities[];
    static int southCities[];
    static int numberOfCities;
    //_________________________________________________________

    //method for north and south cities Initialization
    public static int[] citiesInitialization(boolean isSouth) {
        int cities[] = new int[numberOfCities + 1];
        cities[0] = 0;
        //south cities
        if (isSouth) {
            for (int i = 1; i <= numberOfCities; i++) {
                cities[i] = i;
            }
            //north cities
        } else {
            for (int i = 1; i <= numberOfCities; i++) {
                cities[i] = readFile.nextInt();
            }
        }
        return cities;
    }
    //_________________________________________________________

    //method for connected Cities Array gneration to know the highest number of possible bridges
    public static int[][] connectedCitiesArray() {
        int lcs[][] = new int[numberOfCities + 1][numberOfCities + 1];

        for (int i = 0; i <= numberOfCities; i++) {
            lcs[0][i] = 0;
        }
        for (int i = 0; i <= numberOfCities; i++) {
            lcs[i][0] = 0;
        }

        for (int i = 1; i <= numberOfCities; i++) {
            for (int j = 1; j <= numberOfCities; j++) {
                if (northCities[i] == southCities[j]) {
                    lcs[i][j] = 1 + lcs[i - 1][j - 1];
                } else {
                    lcs[i][j] = Math.max(lcs[i - 1][j], lcs[i][j - 1]);
                }
            }
        }
        return lcs;
    }

    //_________________________________________________________
//    public static String connectedCitiesString(String connected , int CCArray[][] , int i ,int j){
//        if(CCArray[i][j] == 0)
//            return connected;
//        
//        if(CCArray[i][j] == CCArray[i-1][j])
//            return connectedCitiesString(connected , CCArray , i-1,j);
//        
//        if(CCArray[i][j] == CCArray[i][j-1])
//            return connectedCitiesString(connected , CCArray , i,j-1);
//        
//        connected = nortthCities[i]+" " +connected ;
//        return connectedCitiesString(connected , CCArray , i-1,j-1);
//       
//    }
    public static String connectedCitiesString(String connected, int CCArray[][], int j, int i) {
        String s = "";

        while (i != 0 && j != 0) {
            if (CCArray[i][j] == CCArray[i - 1][j]) {
                i -= 1;
            } else if (CCArray[i][j] == CCArray[i][j - 1]) {
                j -= 1;
            } else {
                s = northCities[i] + " " + s;
                i -= 1;
                j -= 1;
            }

        }
        return s;
    }

    //_________________________________________________________
    public static void visualize(int numberOfBridge, String connectedCitiesString, SimpleGraph graph) {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        //panel1 info
        JPanel panel1 = new JPanel();
        JLabel la = new JLabel("The highest number of possible bridges : ");
        panel1.add(la);
        JTextField textField = new JTextField(numberOfBridge + "");
        panel1.add(textField);
        textField.disable();
         frame.add(panel1, BorderLayout.NORTH);

        //panel2 
        JPanel panel2 = new JPanel();
        JLabel la2 = new JLabel("Connected Cities : ");
        panel2.add(la2);
        JTextField t22 = new JTextField(connectedCitiesString);
        panel2.add(t22);
        t22.disable();
        panel1.add(panel2, BorderLayout.SOUTH);

        //panel3 Graph visualization
        JPanel p3 = new JPanel();

        JGraphXAdapter jgxAdapter = new JGraphXAdapter<String, DefaultEdge>(graph);
        mxGraphComponent graphComponent = new mxGraphComponent(jgxAdapter);
        mxGraphModel graphModel = (mxGraphModel) graphComponent.getGraph().getModel();
        Collection<Object> cells = graphModel.getCells().values();
        // This part to remove arrow from edge
        mxUtils.setCellStyles(graphComponent.getGraph().getModel(),
                cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
        frame.getContentPane().add(graphComponent);
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());

        graphComponent.setSize(750, 750);

        JScrollPane Scrl = new JScrollPane(graphComponent,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.getContentPane().add(Scrl, BorderLayout.CENTER);

        //panel4
        JPanel p4 = new JPanel();
        JTextField t1 = new JTextField("              Nortth Cities          ");
        p4.add(t1, BorderLayout.EAST);

        JTextField t2 = new JTextField("             South Cities             ");
        p4.add(t2, BorderLayout.WEST);
        t1.disable();
        t2.disable();
        frame.add(p4, BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.setTitle("Bridge To Nowhere");
        frame.setSize(1300, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //_________________________________________________________
    public static SimpleGraph graph(String connectedCitiesString) {
        SimpleGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        //south Vertex
        for (int i = 1; i <= numberOfCities; i++) {
            graph.addVertex("s" + southCities[i]);
        }
        //north Vertex
        for (int i = numberOfCities; i >= 1; i--) {
            graph.addVertex("n" + northCities[i]);
        }

        //edges 
        while (connectedCitiesString.length() != 0) {

            int i = connectedCitiesString.indexOf(" ");
            if (i <= 0) {
                break;
            }
            int index = Integer.parseInt(connectedCitiesString.substring(0, i));
            graph.addEdge("n" + index, "s" + index);
            connectedCitiesString = connectedCitiesString.substring(i + 1, connectedCitiesString.length());

        }
        return graph;
    }

    //_________________________________________________________
    //main metho
    public static void main(String[] args) {
        try {
            readFile = new Scanner(new File("northCities.text"));

        } catch (Exception e) {
            System.out.println("Sorry the file northCities.text is not found");
        }

        //reads number of cities from the file
        numberOfCities = readFile.nextInt();
        northCities = citiesInitialization(false);
        southCities = citiesInitialization(true);

        int[][] CCArray = connectedCitiesArray();
        String connectedCitiesString = connectedCitiesString("", CCArray, numberOfCities, numberOfCities);

        SimpleGraph<String, DefaultEdge> graph = graph(connectedCitiesString);

        int numberOfBridge = CCArray[numberOfCities][numberOfCities];
        visualize(numberOfBridge, connectedCitiesString, graph);

    }

}
