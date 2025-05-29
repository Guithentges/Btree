

public class BTreeTeste {
    public static void main(String[] args) {
        BTree bTree = new BTree();

        // 🔵 Inserções iniciais
        System.out.println("🔵 Inserindo valores: 1, 2, 4, 5, 3");
        int[] valores = {1, 2, 4, 5, 3};
        for (int v : valores) {
            bTree.insert(v);
        }

        System.out.println("\n🟢 Árvore após inserções (por nível):");
        bTree.printLevelOrderPretty();

        // 🟠 Remoção de valor inexistente
        System.out.println("\n🟠 Tentando remover o valor 6 (não existe na árvore):");
        bTree.delete(6);
        bTree.printLevelOrderPretty();

        System.out.println("\n🔴 Tentando remover o valor 13 (não existe na árvore):");
        bTree.delete(13);
        bTree.printLevelOrderPretty();

        // 🟡 Inserindo mais valores para forçar divisões
        System.out.println("\n🟡 Inserindo valores adicionais: 6, 7, 8, 9, 10");
        int[] maisValores = {6, 7, 8, 9, 10};
        for (int v : maisValores) {
            bTree.insert(v);
        }

        System.out.println("\n🟢 Árvore após novas inserções (por nível):");
        bTree.printLevelOrderPretty();

        // 🟣 Remoção de valor presente
        System.out.println("\n🟣 Removendo o valor 7 (presente):");
        bTree.delete(7);
        bTree.printLevelOrderPretty();

        // ⚪ Exibição em pré-ordem
        System.out.println("\n⚪ Exibindo árvore em pré-ordem:");
        bTree.preOrder();

        // 🧪 Remoção de valores adicionais
        System.out.println("\n🧪 Removendo os valores: 2, 3, 5");
        bTree.delete(2);
        bTree.delete(3);
        bTree.delete(5);
        bTree.printLevelOrderPretty();

        // ✅ Verificação final
        System.out.println("\n✅ Árvore final após todas as operações:");
        bTree.printLevelOrderPretty();
    }
}
