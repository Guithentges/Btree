

import java.util.Scanner;

public class app {
    public static void main(String[] args) {
        BTree btree = new BTree();
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\nOpções:");
            System.out.println("1 – Inserir valor na árvore");
            System.out.println("2 – Exibir as chaves por nível");
            System.out.println("3 – Exibir as chaves em ordem");
            System.out.println("4 – Remover um valor da árvore");
            System.out.println("0 – Sair");
            System.out.print("Informe a opção desejada: ");
            opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o valor: ");
                    int valor = sc.nextInt();
                    btree.insert(valor);
                    break;
                case 2:
                    btree.printLevelOrderPretty();
                    break;
                case 3:
                    btree.preOrder();
                    break;
                case 4:
                    System.out.print("Digite o valor a remover: ");
                    int del = sc.nextInt();
                    btree.delete(del);
                    break;
                case 0:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        sc.close();
    }
}
