public class BTree {
    
    private static class SplitResult {
        int chaveMediana;          
        NodeBTree right;    
        SplitResult(int chaveMediana, NodeBTree right) {
            this.chaveMediana = chaveMediana;
            this.right = right;
        }
    }
    
    private final int M;  
    private NodeBTree root;               

    public BTree() {
        this.M = 5;
    }

    // função para inserir valor na arvore
    public void insert(int chave) {
        if (root == null) {
            root = new NodeBTree(M, true);
            root.chaves[0] = chave;
            root.numChaves = 1;
            return;
        }

        SplitResult retorno  = inserir(root, chave);

        // Se a raiz passar do limite, cria nova raiz
        if (retorno != null) {
            NodeBTree newRoot = new NodeBTree(M, false);
            newRoot.chaves[0] = retorno.chaveMediana;
            newRoot.filhos[0] = root;
            newRoot.filhos[1] = retorno.right;
            newRoot.numChaves = 1;
            root = newRoot;
        }
    }
    
    private SplitResult inserir(NodeBTree node, int chave) {

        // caso nó folha
        if (node.folha) {
            // insere mantendo ordenação
            int i = node.numChaves - 1;
            while (i >= 0 && node.chaves[i] > chave) {
                node.chaves[i + 1] = node.chaves[i];
                i--;
            }
            node.chaves[i + 1] = chave;
            node.numChaves++;
            //observa se o nó ultrapassou o limite
            return (node.numChaves < M) ? null : split(node);   
        }

        // caso nó página
        int pos = node.numChaves - 1;
        while (pos >= 0 && node.chaves[pos] > chave) pos--;
        pos++;                                   

        SplitResult childRes = inserir(node.filhos[pos], chave);

        // Se o filho dividiu, inserir a chaveMedianaa no nó pai
        if (childRes != null) {
            // desloca para abrir espaço
            for (int j = node.numChaves; j > pos; j--) {
                node.chaves[j]     = node.chaves[j - 1];
                node.filhos[j+1] = node.filhos[j];
            }
            node.chaves[pos] = childRes.chaveMediana;
            node.filhos[pos + 1] = childRes.right;
            node.numChaves++;
        }
        //observa se o nó ultrapassou o limite
        return (node.numChaves < M) ? null : split(node);       
    }

    // cisão do nó
    private SplitResult split(NodeBTree node) {
        int meio = M / 2; 
        //trata o nó da direita
        NodeBTree right = new NodeBTree(M, node.folha);
        right.numChaves = node.numChaves - meio - 1;
        for (int j = 0; j < right.numChaves; j++)
            right.chaves[j] = node.chaves[meio + 1 + j];
        if (!node.folha) {
            for (int j = 0; j <= right.numChaves; j++)
                right.filhos[j] = node.filhos[meio + 1 + j];
        }

        int chaveMediana = node.chaves[meio];
        node.numChaves = meio;                 

        return new SplitResult(chaveMediana,right);
}



    // imprimir em ordem
    public void percorrerPorNivel() {
        if (root == null) return;
        java.util.Queue<Integer>  level  = new java.util.LinkedList<>();
        java.util.Queue<NodeBTree> queue  = new java.util.LinkedList<>();
        queue.add(root); level.add(0);

        int current = 0;
        System.out.print("N0: ");
        while (!queue.isEmpty()) {
            NodeBTree node = queue.poll();
            int lvl = level.poll();
            if (lvl != current) {
                current = lvl;
                System.out.print("\nN" + current + ": ");
            }

            //imprime o nó
            System.out.print("(");
            for (int i = 0; i < M-1; i++) {
                if (i < node.numChaves) System.out.print(node.chaves[i]);
                else System.out.print("_");
                if (i < M-2) System.out.print("|");
            }
            System.out.print(") ");

            //se nó não for folha
            if (!node.folha) {
                for (int i = 0; i <= node.numChaves; i++) {
                    queue.add(node.filhos[i]);
                    level.add(lvl + 1);
                }
            }
        }
        System.out.println();
    }

//chamada da função pré ordem recursiva
public void percorrerPreOrdem() {
    imprimirPreOrdem(root);
    System.out.println();
}

private void imprimirPreOrdem(NodeBTree node) {
    if (node == null) return;

    //imprime o nó 
    System.out.print("[");
    for (int i = 0; i < node.numChaves; i++) {
        System.out.print(node.chaves[i]);
        if (i < node.numChaves - 1) System.out.print(",");
    }
    System.out.print("] ");

    //se nó não for folha
    if (!node.folha) {
        for (int i = 0; i <= node.numChaves; i++)
            imprimirPreOrdem(node.filhos[i]);
    }
}
// chamada da função de deletar um valor
public void remover(int chave) {
    removerRec(root, chave);
    if (root.numChaves == 0 && !root.folha)
        root = root.filhos[0];
}

private void removerRec(NodeBTree node, int chave) {
    int pos = 0;
    while (pos < node.numChaves && chave > node.chaves[pos]) pos++;

    // Caso 1: chave está neste nó
    if (pos < node.numChaves && node.chaves[pos] == chave) {
        //se nó for uma folha
        if (node.folha) {
            for (int i = pos; i < node.numChaves - 1; i++)
                node.chaves[i] = node.chaves[i + 1];
            node.numChaves--;
        //se for uma página
        } else {
            NodeBTree nextChild = node.filhos[pos + 1];
            //Se o filho da direita tem mais que o número mínimo de chaves
            if (nextChild.numChaves > M / 2) {
                int next = getNext(nextChild);
                node.chaves[pos] = next;
                removerRec(nextChild, next);
            //Se o filho da esquerda tem mais que o número mínimo de chaves
            } else {
                NodeBTree prevChild = node.filhos[pos];
                if (prevChild.numChaves > M / 2) {
                    int pred = getPrevious(prevChild);
                    node.chaves[pos] = pred;
                    removerRec(prevChild, pred);
                } else {
                    concat(node, pos);
                    removerRec(prevChild, chave);
                }
            }
        }
        
        // Caso 2: chave está em subárvore
        }else{
        if (node.folha) return;
        NodeBTree child = node.filhos[pos];
      
        if (child.numChaves == M / 2) {
            NodeBTree leftbro = (pos > 0) ? node.filhos[pos - 1] : null;
            NodeBTree rightbro = (pos < node.numChaves) ? node.filhos[pos + 1] : null;

            //irmão da direita existe e tem mais que o mínimo de chaves
            if (rightbro != null && rightbro.numChaves > M / 2) {
                child.chaves[child.numChaves] = node.chaves[pos];

                if (!child.folha)
                    child.filhos[child.numChaves + 1] = rightbro.filhos[0];
                child.numChaves++;
                node.chaves[pos] = rightbro.chaves[0];
                for (int i = 0; i < rightbro.numChaves - 1; i++) {
                    rightbro.chaves[i] = rightbro.chaves[i + 1];
                    if (!rightbro.folha)
                        rightbro.filhos[i] = rightbro.filhos[i + 1];
                }

                if (!rightbro.folha)
                    rightbro.filhos[rightbro.numChaves - 1] = rightbro.filhos[rightbro.numChaves];
                rightbro.numChaves--;
            //irmão da esquerda existe e tem mais que o mínimo de chaves    
            } else if (leftbro != null && leftbro.numChaves > M / 2) {
                for (int i = child.numChaves; i > 0; i--) {
                    child.chaves[i] = child.chaves[i - 1];
                    if (!child.folha)
                        child.filhos[i + 1] = child.filhos[i];
                }
                
                if (!child.folha)
                    child.filhos[1] = child.filhos[0];
                child.chaves[0] = node.chaves[pos - 1];
               
                if (!leftbro.folha)
                    child.filhos[0] = leftbro.filhos[leftbro.numChaves];
                child.numChaves++;
                node.chaves[pos - 1] = leftbro.chaves[leftbro.numChaves - 1];
                leftbro.numChaves--;
            //os irmãos têm número mínimo de chaves
            } else {
                if (rightbro != null) {
                    concat(node, pos);
                    child = node.filhos[pos];
                } else {
                    concat(node, pos - 1);
                    child = node.filhos[pos - 1];
                }
            }
        }
        removerRec(child, chave);
    }
}
//função para pegar o antecessor
private int getPrevious(NodeBTree node) {
    while (!node.folha)
        node = node.filhos[node.numChaves];
    return node.chaves[node.numChaves - 1];
}
//função para pegar o sucessor
private int getNext(NodeBTree node) {
    while (!node.folha)
        node = node.filhos[0];
    return node.chaves[0];
}
//função para concatenar os nós
private void concat(NodeBTree parent, int pos) {
    NodeBTree child = parent.filhos[pos];
    NodeBTree bro = parent.filhos[pos + 1];
    child.chaves[child.numChaves] = parent.chaves[pos];
    for (int i = 0; i < bro.numChaves; i++)
        child.chaves[child.numChaves + 1 + i] = bro.chaves[i];
    if (!child.folha) {
        for (int i = 0; i <= bro.numChaves; i++)
            child.filhos[child.numChaves + 1 + i] = bro.filhos[i];
    }
    child.numChaves += bro.numChaves + 1;

    for (int i = pos; i < parent.numChaves - 1; i++) {
        parent.chaves[i] = parent.chaves[i + 1];
        parent.filhos[i + 1] = parent.filhos[i + 2];
    }
    parent.numChaves--;
}
}