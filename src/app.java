import java.util.Scanner;

public class app {
    public static void main(String[] args) {
        BTree btree = new BTree();

        Scanner leitor = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n---- Menu Principal ----");
            System.out.println("1 - Inserir valor");
            System.out.println("2 - Exibir por nível");
            System.out.println("3 - Exibir em pré-ordem");
            System.out.println("4 - Remover valor");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            opcao = leitor.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Valor a inserir: ");
                    int valor = leitor.nextInt();
                    btree.insert(valor);
                    break;
                case 2:
                    btree.percorrerPorNivel();
                    break;
                case 3:
                    btree.percorrerPreOrdem();
                    break;
                case 4:
                    System.out.print("Valor a remover: ");
                    int del = leitor.nextInt();
                    btree.remover(del);
                    break;
                case 0:
                    System.out.println("Encerrando ...");
                    break;
                default:
                    System.out.println("Eleitorolha inválida.");
            }
        } while (opcao != 0);

        leitor.close();
    }
}
