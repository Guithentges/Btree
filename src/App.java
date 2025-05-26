import java.util.Scanner;
import BTree.BTree;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BTree<Integer> tree = new BTree<>();
        int opcao;

        do {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Inserir número");
            System.out.println("2. Remover número");
            System.out.println("3. Exibir passeio pré-ordem");
            System.out.println("4. Exibir passeio por nível");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Digite um número para inserir: ");
                    int valor = scanner.nextInt();
                    tree.insert(valor);
                    System.out.println("Número " + valor + " inserido!");
                    break;

                case 2:
                    System.out.println("Digite o número que deseja remover: ");
                    int num = scanner.nextInt();
                    tree.delete(num);
                    break;
                case 3:
                    System.out.print("Passeio pré-ordem: ");
                    tree.preOrder();
                    break;

                case 4:
                    System.out.println("Passeio por nível:");
                    tree.LevelOrder();
                    break;

                case 0:
                    System.out.println("Encerrando o programa...");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

        } while (opcao != 0);

        scanner.close();
    }
}

