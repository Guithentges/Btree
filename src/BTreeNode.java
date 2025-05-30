public class BTreeNode {
    final int M;                  
    int qtd;                         
    int[] keys;                  
    BTreeNode[] children;          
    boolean leaf;

    BTreeNode(int M, boolean leaf) {
        this.M = M;
        this.leaf = leaf;
        this.keys = new int[M];       
        this.children = new BTreeNode[M+1];
        this.qtd = 0;
    }
}