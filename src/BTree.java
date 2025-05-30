

public class BTree {
    
    private static class SplitResult {
        int median;          
        BTreeNode right;    
        SplitResult(int median, BTreeNode right) {
            this.median = median;
            this.right = right;
        }
    }
    private BTreeNode root;
    private final int M;                   

    public BTree() {
        this.M = 5;
    }

    // função para inserir valor na arvore
    public void insert(int key) {
        if (root == null) {
            root = new BTreeNode(M, true);
            root.keys[0] = key;
            root.qtd = 1;
            return;
        }

        SplitResult res = insertRec(root, key);

        // Se a raiz passar do limite, cria nova raiz
        if (res != null) {
            BTreeNode newRoot = new BTreeNode(M, false);
            newRoot.keys[0] = res.median;
            newRoot.children[0] = root;
            newRoot.children[1] = res.right;
            newRoot.qtd = 1;
            root = newRoot;
        }
    }
    
    private SplitResult insertRec(BTreeNode node, int key) {

        // caso nó folha
        if (node.leaf) {
            // insere mantendo ordenação
            int i = node.qtd - 1;
            while (i >= 0 && node.keys[i] > key) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.qtd++;
            //observa se o nó ultrapassou o limite
            return (node.qtd < M) ? null : split(node);   
        }

        // caso nó página
        int idx = node.qtd - 1;
        while (idx >= 0 && node.keys[idx] > key) idx--;
        idx++;                                   

        SplitResult childRes = insertRec(node.children[idx], key);

        // Se o filho dividiu, inserir a mediana no nó pai
        if (childRes != null) {
            // desloca para abrir espaço
            for (int j = node.qtd; j > idx; j--) {
                node.keys[j]     = node.keys[j - 1];
                node.children[j+1] = node.children[j];
            }
            node.keys[idx] = childRes.median;
            node.children[idx + 1] = childRes.right;
            node.qtd++;
        }
        //observa se o nó ultrapassou o limite
        return (node.qtd < M) ? null : split(node);       
    }

    // cisão do nó
    private SplitResult split(BTreeNode node) {
        int mid = M / 2; 
        //trata o nó da direita
        BTreeNode right = new BTreeNode(M, node.leaf);
        right.qtd = node.qtd - mid - 1;
        for (int j = 0; j < right.qtd; j++)
            right.keys[j] = node.keys[mid + 1 + j];
        if (!node.leaf) {
            for (int j = 0; j <= right.qtd; j++)
                right.children[j] = node.children[mid + 1 + j];
        }

        int median = node.keys[mid];
        node.qtd = mid;                 

        return new SplitResult(median,right);
}



    // imprimir em ordem
    public void levelOrder() {
        if (root == null) return;
        java.util.Queue<Integer>  level  = new java.util.LinkedList<>();
        java.util.Queue<BTreeNode> queue  = new java.util.LinkedList<>();
        queue.add(root); level.add(0);

        int current = 0;
        System.out.print("Nível 0: ");
        while (!queue.isEmpty()) {
            BTreeNode node = queue.poll();
            int lvl = level.poll();
            if (lvl != current) {
                current = lvl;
                System.out.print("\nNível " + current + ": ");
            }

            //imprime o nó
            System.out.print("[");
            for (int i = 0; i < M-1; i++) {
                if (i < node.qtd) System.out.print(node.keys[i]);
                else System.out.print("_");
                if (i < M-2) System.out.print(",");
            }
            System.out.print("] ");

            //se nó não for folha
            if (!node.leaf) {
                for (int i = 0; i <= node.qtd; i++) {
                    queue.add(node.children[i]);
                    level.add(lvl + 1);
                }
            }
        }
        System.out.println();
    }

//chamada da função pré ordem recursiva
public void preOrder() {
    preOrderRec(root);
    System.out.println();
}

private void preOrderRec(BTreeNode node) {
    if (node == null) return;

    //imprime o nó 
    System.out.print("[");
    for (int i = 0; i < node.qtd; i++) {
        System.out.print(node.keys[i]);
        if (i < node.qtd - 1) System.out.print(",");
    }
    System.out.print("] ");
    if (!node.leaf) {
        for (int i = 0; i <= node.qtd; i++)
            preOrderRec(node.children[i]);
    }
}
// chamada da função de deletar um valor
public void delete(int key) {
    deleteRec(root, key);
    if (root.qtd == 0 && !root.leaf)
        root = root.children[0];
}

private void deleteRec(BTreeNode node, int key) {
    int idx = 0;
    while (idx < node.qtd && key > node.keys[idx]) idx++;

    // Caso 1: chave está neste nó
    if (idx < node.qtd && node.keys[idx] == key) {
        //se nó for uma folha
        if (node.leaf) {
            for (int i = idx; i < node.qtd - 1; i++)
                node.keys[i] = node.keys[i + 1];
            node.qtd--;
        //se for uma página
        } else {
            BTreeNode nextChild = node.children[idx + 1];
            if (nextChild.qtd > M / 2) {
                int next = getNext(nextChild);
                node.keys[idx] = next;
                deleteRec(nextChild, next);
            } else {
                BTreeNode prevChild = node.children[idx];
                if (prevChild.qtd > M / 2) {
                    int pred = getPrevious(prevChild);
                    node.keys[idx] = pred;
                    deleteRec(prevChild, pred);
                } else {
                    concat(node, idx);
                    deleteRec(prevChild, key);
                }
            }
        }
        
        // Caso 2: chave está em subárvore
        }else{
        if (node.leaf) return;
        BTreeNode child = node.children[idx];
      
        if (child.qtd == M / 2) {
            BTreeNode leftbro = (idx > 0) ? node.children[idx - 1] : null;
            BTreeNode rightbro = (idx < node.qtd) ? node.children[idx + 1] : null;

            if (rightbro != null && rightbro.qtd > M / 2) {
                child.keys[child.qtd] = node.keys[idx];

                if (!child.leaf)
                    child.children[child.qtd + 1] = rightbro.children[0];
                child.qtd++;
                node.keys[idx] = rightbro.keys[0];
                for (int i = 0; i < rightbro.qtd - 1; i++) {
                    rightbro.keys[i] = rightbro.keys[i + 1];
                    if (!rightbro.leaf)
                        rightbro.children[i] = rightbro.children[i + 1];
                }

                if (!rightbro.leaf)
                    rightbro.children[rightbro.qtd - 1] = rightbro.children[rightbro.qtd];
                rightbro.qtd--;
            } else if (leftbro != null && leftbro.qtd > M / 2) {
                for (int i = child.qtd; i > 0; i--) {
                    child.keys[i] = child.keys[i - 1];
                    if (!child.leaf)
                        child.children[i + 1] = child.children[i];
                }
                
                if (!child.leaf)
                    child.children[1] = child.children[0];
                child.keys[0] = node.keys[idx - 1];
               
                if (!leftbro.leaf)
                    child.children[0] = leftbro.children[leftbro.qtd];
                child.qtd++;
                node.keys[idx - 1] = leftbro.keys[leftbro.qtd - 1];
                leftbro.qtd--;
            } else {
                if (rightbro != null) {
                    concat(node, idx);
                    child = node.children[idx];
                } else {
                    concat(node, idx - 1);
                    child = node.children[idx - 1];
                }
            }
        }
        deleteRec(child, key);
    }
}
//função para pegar o antecessor
private int getPrevious(BTreeNode node) {
    while (!node.leaf)
        node = node.children[node.qtd];
    return node.keys[node.qtd - 1];
}
//função para pegar o sucessor
private int getNext(BTreeNode node) {
    while (!node.leaf)
        node = node.children[0];
    return node.keys[0];
}
//função para concatenar os nós
private void concat(BTreeNode parent, int idx) {
    BTreeNode child = parent.children[idx];
    BTreeNode bro = parent.children[idx + 1];
    child.keys[child.qtd] = parent.keys[idx];
    for (int i = 0; i < bro.qtd; i++)
        child.keys[child.qtd + 1 + i] = bro.keys[i];
    if (!child.leaf) {
        for (int i = 0; i <= bro.qtd; i++)
            child.children[child.qtd + 1 + i] = bro.children[i];
    }
    child.qtd += bro.qtd + 1;

    for (int i = idx; i < parent.qtd - 1; i++) {
        parent.keys[i] = parent.keys[i + 1];
        parent.children[i + 1] = parent.children[i + 2];
    }
    parent.qtd--;
}
}