

public class BTreeNode {
    final int M;                   // ordem da árvore
    int n;                         // quantidade de chaves actualmente no nó
    int[] keys;                    // tamanho M   (1 posição extra)
    BTreeNode[] children;          // tamanho M+1 (1 posição extra)
    boolean leaf;

    BTreeNode(int M, boolean leaf) {
        this.M = M;
        this.leaf = leaf;
        this.keys = new int[M];        // ex.: M=5 → 5 posições
        this.children = new BTreeNode[M+1];
        this.n = 0;
    }
}