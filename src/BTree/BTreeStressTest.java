package BTree;

public class BTreeStressTest {
    public static void main(String[] args) {
        BTree tree = new BTree();

        // Inserções repetidas (valores iguais) - deve evitar duplicatas ou manter comportamento consistente
        int[] duplicatas = {15, 15, 15, 15};
        for (int val : duplicatas) {
            System.out.println("Inserindo duplicado: " + val);
            tree.insert(val);
            tree.levelOrder();
        }

        // Inserções em ordem reversa (pior caso para balanceamento)
        int[] reverso = {200, 190, 180, 170, 160, 150, 140, 130, 120, 110, 100, 90, 80, 70, 60, 50, 40, 30, 20, 10};
        for (int val : reverso) {
            System.out.println("Inserindo (ordem reversa): " + val);
            tree.insert(val);
        }
        System.out.println("\nÁrvore após inserções em ordem reversa:");
        tree.levelOrder();

        // Remoção de elemento inexistente (não deve causar erro)
        System.out.println("\nTentando remover elemento inexistente: 999");
        tree.delete(999);
        tree.levelOrder();

        // Remoção da raiz repetidamente até a árvore sumir
        int[] toDelete = {15, 10, 20, 30, 40, 50, 60, 70, 80, 90,
                          100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
        for (int val : toDelete) {
            System.out.println("\nRemovendo: " + val);
            tree.delete(val);
            tree.levelOrder();
        }

        // Verificação final da árvore vazia
        System.out.println("\nVerificação final (deve estar vazia):");
        tree.levelOrder();

        // Inserções de extremos para testar limites superiores
        System.out.println("\nInserindo extremos:");
        tree.insert(Integer.MIN_VALUE);
        tree.insert(Integer.MAX_VALUE);
        tree.insert(0);
        tree.levelOrder();

        // Remoção dos extremos
        tree.delete(Integer.MIN_VALUE);
        tree.delete(Integer.MAX_VALUE);
        tree.delete(0);
        System.out.println("\nApós remoção dos extremos:");
        tree.levelOrder();
    }
}
