public class BTreeTest
 {
    public static void main(String[] args) {
        BTree bTree = new BTree();

        // 🔹 Inserções conforme o PDF (exemplo clássico com ordem 5)
        int[] insercoes = {
            85, 60, 52, 70, 58, 37, 111, 23, 205, 5,
            97, 64, 14, 90, 30, 75, 25
        };

        System.out.println("🔹 Inserções conforme PDF:");
        for (int v : insercoes) {
            System.out.printf("➡ Inserindo %d\n", v);
            bTree.insert(v);
            bTree.levelOrder();
        }

        // 🔸 Remoção 1: 37 (caso 1 - nó interno, substituição por sucessor)
        System.out.println("\n🔸 Remoção de 37 (caso 1 - substituição por sucessor)");
        bTree.delete(37);
        bTree.levelOrder();

        // 🔸 Remoção 2: 58 (caso 2 - folha)
        System.out.println("\n🔸 Remoção de 58 (caso 2 - remoção simples em folha)");
        bTree.delete(58);
        bTree.levelOrder();

        // 🔸 Remoção 3: 25 (remoção com concatenação)
        System.out.println("\n🔸 Remoção de 25 (concatenação de páginas)");
        bTree.delete(25);
        bTree.levelOrder();

        // 🔸 Remoção 4: 30 (remoção com redistribuição)
        System.out.println("\n🔸 Remoção de 30 (redistribuição entre irmãos)");
        bTree.delete(30);
        bTree.levelOrder();

        System.out.println("\n✅ Teste finalizado conforme exemplos do PDF.");
    }
}
