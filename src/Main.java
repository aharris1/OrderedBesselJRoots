import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLinkException;
import com.wolfram.jlink.MathLinkFactory;

import java.util.*;

/**
 * Created by aharris on 6/21/2018.
 */
public class Main {

    public static void main(String[] args){
        KernelLink ml = null;

        try {
            ml = MathLinkFactory.createKernelLink("-linkmode launch -linkname 'C:\\Program Files\\Wolfram Research\\Mathematica\\11.0\\MathKernel.exe'");

        } catch (MathLinkException e) {
            e.printStackTrace();
            return;
        }

        try{
            ml.discardAnswer();

            ml.evaluate("2+2");
            ml.waitForAnswer();
            assert ml.getInteger() == 4;
            ml.newPacket();
            System.out.println("KernelLink initialized");
        } catch (MathLinkException e) {
            e.printStackTrace();
        }

        Node root = new Node(0, 1, ml);
        buildTree(root, 1000);
        List<Node> inOrderTraversal = root.inOrder();
        List<Double> sortedAlphas = new LinkedList<>();
        for(Node node : inOrderTraversal){
            sortedAlphas.add(node.value());
        }

        System.out.println(Arrays.deepToString(sortedAlphas.toArray()));
    }

    private static void buildTree (Node root, int count){
        //Initializes set to store which nodes are currently in the tree
        Set<Node> treeNodes = new HashSet<>();
        treeNodes.add(root);

        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.addAll(root.generatedNodes());

        while(treeNodes.size() < count){
            Node node = queue.poll(); //Takes the top node from the queue
            root.insertNode(node); //Adds the node to the tree
            treeNodes.add(node); //Adds the node to the set of nodes contained in the tree
            for(Node gen : node.generatedNodes()){
                //Adds the node to the queue if it is not present in either the queue or the tree
                if(!queue.contains(gen) && !treeNodes.contains(gen)){
                    queue.add(gen);

                }
            }
        }

    }

}
