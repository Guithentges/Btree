public class NodeBTree {
    final int M;
    int numChaves;
    int[] chaves;
    NodeBTree[] filhos;
    boolean folha;

    NodeBTree(int M, boolean isFolha) {
        this.M = M;
        this.folha = isFolha;
        this.chaves = new int[M];
        this.filhos = new NodeBTree[M + 1];
        this.numChaves = 0;
    }
}
