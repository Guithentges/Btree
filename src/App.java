import java.util.Scanner;
import BTree.BTree;

public class App {
    public static void main(String[] args) {
        BTree tree = new BTree();
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            System.out.println("Opções:");
            System.out.println("1 – Inserir valor na árvore");
            System.out.println("2 – Exibir as chaves por nível");
            System.out.println("3 – Exibir as chaves em pré-ordem");
            System.out.println("4 – Remover um valor da árvore");
            System.out.println("5 – Sair");
            System.out.print("Informe a opção desejada: ");
            option = scanner.nextInt();
            switch(option) {
                case 1:
                    System.out.print("Valor a inserir: ");
                    int valInsert = scanner.nextInt();
                    tree.insert(valInsert);
                    System.out.println("Valor " + valInsert + " inserido.");
                    break;
                case 2:
                    System.out.println("Chaves da árvore (por nível):");
                    tree.levelOrder();
                    break;
                case 3:
                    System.out.println("Chaves da árvore (pré-ordem):");
                    tree.preOrder();
                    break;
                case 4:
                    System.out.print("Valor a remover: ");
                    int valRemove = scanner.nextInt();
                    tree.delete(valRemove);
                    System.out.println("Valor " + valRemove + " removido (se existente).");
                    break;
                case 5:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
            System.out.println();
        } while(option != 5);
        scanner.close();
    }
}
