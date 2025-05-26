package BTree;

public class BTreeTest {
    public static void main(String[] args) {
        BTree tree = new BTree();

        int[] insertVals = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 5, 115, 1, 55};
        for (int val : insertVals) {
            System.out.println("Inserindo: " + val);
            tree.insert(val);
            tree.levelOrder();
        }

        System.out.println("\nApós todas as inserções:");
        tree.levelOrder();
        tree.preOrder();

        int[] deleteVals = {5, 30, 1, 120, 10, 20, 40, 50, 60, 70, 80, 90, 100, 110, 115, 55};
        for (int val : deleteVals) {
            System.out.println("\nRemovendo: " + val);
            tree.delete(val);
            tree.levelOrder();
        }

        System.out.println("\nApós todas as remoções:");
        tree.levelOrder();
        tree.preOrder();
    }
}
