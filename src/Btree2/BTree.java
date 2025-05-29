package Btree2;

public class BTree {
    /** Objeto usado internamente para propagar divisões. */
    private static class SplitResult {
        int median;          // chave que vai subir
        BTreeNode right;     // novo nó à direita
        SplitResult(int median, BTreeNode right) {
            this.median = median;
            this.right  = right;
        }
    }
    private BTreeNode root;
    private final int M;                    // ordem (máx. de filhos)

    public BTree() {
        this.M = 5;
    }

    /* -------- API pública -------- */
    public void insert(int key) {
        if (root == null) {
            root = new BTreeNode(M, true);
            root.keys[0] = key;
            root.n = 1;
            return;
        }

        SplitResult res = insertRecursive(root, key);

        // Se a raiz estourar, cria nova raiz
        if (res != null) {
            BTreeNode newRoot = new BTreeNode(M, false);
            newRoot.keys[0]   = res.median;
            newRoot.children[0] = root;
            newRoot.children[1] = res.right;
            newRoot.n = 1;
            root = newRoot;
        }
    }

    /** Impressão em largura mostrando vetor completo. */
    public void printLevelOrderPretty() {
        if (root == null) return;
        java.util.Queue<BTreeNode> q  = new java.util.LinkedList<>();
        java.util.Queue<Integer>  lv  = new java.util.LinkedList<>();
        q.add(root); lv.add(0);

        int cur = 0;
        System.out.print("Nível 0: ");
        while (!q.isEmpty()) {
            BTreeNode node = q.poll();
            int lvl = lv.poll();
            if (lvl != cur) {
                cur = lvl;
                System.out.print("\nNível " + cur + ": ");
            }
            // vetor completo
            System.out.print("[");
            for (int i = 0; i < M-1; i++) {
                if (i < node.n) System.out.print(node.keys[i]);
                else            System.out.print("_");
                if (i < M-2) System.out.print(",");
            }
            System.out.print("] ");

            if (!node.leaf) {
                for (int i = 0; i <= node.n; i++) {
                    q.add(node.children[i]);
                    lv.add(lvl + 1);
                }
            }
        }
        System.out.println();
    }

    /* -------- implementação recursiva -------- */
    /** Insere key em 'node'.  
     *  Se o nó não estourar, devolve null.  
     *  Se estourar, devolve SplitResult para o pai tratar.        */
    private SplitResult insertRecursive(BTreeNode node, int key) {

        /* ---------- CASO 1: nó folha ---------- */
        if (node.leaf) {
            // insere mantendo ordenação
            int i = node.n - 1;
            while (i >= 0 && node.keys[i] > key) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.n++;

            return (node.n < M) ? null : split(node);   // overflow?
        }

        /* ---------- CASO 2: nó interno ---------- */
        int idx = node.n - 1;
        while (idx >= 0 && node.keys[idx] > key) idx--;
        idx++;                                   // filho que vai receber a chave

        SplitResult childRes = insertRecursive(node.children[idx], key);

        // Se o filho dividiu, inserir a mediana no 'node'
        if (childRes != null) {
            // desloca para abrir espaço
            for (int j = node.n; j > idx; j--) {
                node.keys[j]     = node.keys[j - 1];
                node.children[j+1] = node.children[j];
            }
            node.keys[idx]        = childRes.median;
            node.children[idx + 1] = childRes.right;
            node.n++;
        }

        return (node.n < M) ? null : split(node);       // overflow?
    }

    /** Efetua a divisão de 'node', devolvendo mediana e novo nó direito. */
    private SplitResult split(BTreeNode node) {
        int mid = M / 2; 

        BTreeNode right = new BTreeNode(M, node.leaf);
        right.n = node.n - mid - 1;
        for (int j = 0; j < right.n; j++)
            right.keys[j] = node.keys[mid + 1 + j];
        if (!node.leaf) {
            for (int j = 0; j <= right.n; j++)
                right.children[j] = node.children[mid + 1 + j];
        }

        int median = node.keys[mid];
        node.n = mid;                       // nó esquerdo fica com mid chaves

        return new SplitResult(median,right);
}

        public void preOrder() {
    preOrderRecursive(root);
    System.out.println();
}

private void preOrderRecursive(BTreeNode node) {
    if (node == null) return;
    System.out.print("[");
    for (int i = 0; i < node.n; i++) {
        System.out.print(node.keys[i]);
        if (i < node.n - 1) System.out.print(",");
    }
    System.out.print("] ");
    if (!node.leaf) {
        for (int i = 0; i <= node.n; i++)
            preOrderRecursive(node.children[i]);
    }
}

public void delete(int key) {
    deleteRecursive(root, key);
    if (root.n == 0 && !root.leaf)
        root = root.children[0];
}

private void deleteRecursive(BTreeNode node, int key) {
    int idx = 0;
    while (idx < node.n && key > node.keys[idx]) idx++;

    // Caso 1: chave está neste nó
    if (idx < node.n && node.keys[idx] == key) {
        if (node.leaf) {
            for (int i = idx; i < node.n - 1; i++)
                node.keys[i] = node.keys[i + 1];
            node.n--;
        } else {
            BTreeNode succChild = node.children[idx + 1];
            if (succChild.n > M / 2) {
                int succ = getSuccessor(succChild);
                node.keys[idx] = succ;
                deleteRecursive(succChild, succ);
            } else {
                BTreeNode predChild = node.children[idx];
                if (predChild.n > M / 2) {
                    int pred = getPredecessor(predChild);
                    node.keys[idx] = pred;
                    deleteRecursive(predChild, pred);
                } else {
                    merge(node, idx);
                    deleteRecursive(predChild, key);
                }
            }
        }
        
    // Caso 2: chave está em subárvore
        }else{
        if (node.leaf) return;
        BTreeNode child = node.children[idx];

        if (child.n == M / 2) {
            BTreeNode rightSibling = (idx < node.n) ? node.children[idx + 1] : null;
            BTreeNode leftSibling = (idx > 0) ? node.children[idx - 1] : null;

            if (rightSibling != null && rightSibling.n > M / 2) {
                child.keys[child.n] = node.keys[idx];
                if (!child.leaf)
                    child.children[child.n + 1] = rightSibling.children[0];
                child.n++;
                node.keys[idx] = rightSibling.keys[0];
                for (int i = 0; i < rightSibling.n - 1; i++) {
                    rightSibling.keys[i] = rightSibling.keys[i + 1];
                    if (!rightSibling.leaf)
                        rightSibling.children[i] = rightSibling.children[i + 1];
                }
                if (!rightSibling.leaf)
                    rightSibling.children[rightSibling.n - 1] = rightSibling.children[rightSibling.n];
                rightSibling.n--;
            } else if (leftSibling != null && leftSibling.n > M / 2) {
                for (int i = child.n; i > 0; i--) {
                    child.keys[i] = child.keys[i - 1];
                    if (!child.leaf)
                        child.children[i + 1] = child.children[i];
                }
                if (!child.leaf)
                    child.children[1] = child.children[0];
                child.keys[0] = node.keys[idx - 1];
                if (!leftSibling.leaf)
                    child.children[0] = leftSibling.children[leftSibling.n];
                child.n++;
                node.keys[idx - 1] = leftSibling.keys[leftSibling.n - 1];
                leftSibling.n--;
            } else {
                if (rightSibling != null) {
                    merge(node, idx);
                    child = node.children[idx];
                } else {
                    merge(node, idx - 1);
                    child = node.children[idx - 1];
                }
            }
        }
        deleteRecursive(child, key);
    }
}

private int getPredecessor(BTreeNode node) {
    while (!node.leaf)
        node = node.children[node.n];
    return node.keys[node.n - 1];
}

private int getSuccessor(BTreeNode node) {
    while (!node.leaf)
        node = node.children[0];
    return node.keys[0];
}

private void merge(BTreeNode parent, int idx) {
    BTreeNode child = parent.children[idx];
    BTreeNode sibling = parent.children[idx + 1];
    child.keys[child.n] = parent.keys[idx];
    for (int i = 0; i < sibling.n; i++)
        child.keys[child.n + 1 + i] = sibling.keys[i];
    if (!child.leaf) {
        for (int i = 0; i <= sibling.n; i++)
            child.children[child.n + 1 + i] = sibling.children[i];
    }
    child.n += sibling.n + 1;

    for (int i = idx; i < parent.n - 1; i++) {
        parent.keys[i] = parent.keys[i + 1];
        parent.children[i + 1] = parent.children[i + 2];
    }
    parent.n--;
}


}