import com.wolfram.jlink.KernelLink;
import com.wolfram.jlink.MathLink;
import com.wolfram.jlink.MathLinkException;

import java.awt.image.Kernel;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by aharris on 6/21/2018.
 */
public class Node implements Comparable{

    public int n;
    public int m;

    private Double val;

    private KernelLink ml;

    public Node left = null;
    public Node right = null;

    public Node(int m, int n, KernelLink ml){
        this.n = n;
        this.m = m;
        this.ml = ml;
        value();
    }

    public LinkedList<Node> generatedNodes(){
        LinkedList<Node> ret = new LinkedList<>();
        ret.add(new Node(m+1, n, ml));
        ret.add(new Node(m, n+1, ml));
        return ret;
    }

    public List<Node> inOrder(){
        List<Node> ret = new LinkedList<>();
        if(left != null){
            ret.addAll(left.inOrder());
        }
        ret.add(this);
        if(right != null){
           ret.addAll(right.inOrder());
        }
        return ret;
    }

    /**
     * Recursive function to add a new node to the tree. Adds it to the first null slot available
     * @param node the node to add to the tree
     */
    public void insertNode(Node node){
        if(node.val < val){
            if(left == null){
                left = node;
            }
            else{
                left.insertNode(node);
            }
        }
        else if(node.val > val){
            if(right == null){
                right = node;
            }
            else{
                right.insertNode(node);
            }
        }
    }


    /**
     * Calculates the nth zero of J_m
     * @return the value of the nth zero of J_m
     */
    public double value(){
        if(val == null) {
            try {
                ml.evaluate("N[BesselJZero[" + m + ", " + n + "]]");
                ml.waitForAnswer();
                val = ml.getDouble();
                ml.newPacket();
            } catch (MathLinkException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof Node)){
            return -1;
        }
        Node other = (Node)o;
        if(other.value() > value()){
            return -1;
        }
        else if(other.value() < value()){
            return 1;
        }
        return 0;
    }

    @Override
    public int hashCode(){
        return Objects.hash(n, m);
    }

    @Override
    public boolean equals(Object o){
        return o instanceof Node && ((Node)o).m == m && ((Node)o).n == n;
    }
}
