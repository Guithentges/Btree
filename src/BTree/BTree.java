
package BTree;

public class BTree {
    BTreeNode root;

    public BTree() {
        this.root = null;
    }

    public void insert(int key) {
        if (root == null) {
            root = new BTreeNode(true);
            root.keys[0] = key;
            root.n = 1;
        } else {
            if (contains(root, key)) return; // evitar duplicatas
            if (root.n == BTreeNode.MAX_KEYS) {
                BTreeNode newRoot = new BTreeNode(false);
                newRoot.children[0] = root;
                splitChild(newRoot, 0);
                root = newRoot;
            }
            insertNonFull(root, key);
        }
    }

    private boolean contains(BTreeNode node, int key) {
        int i = 0;
        while (i < node.n && key > node.keys[i]) i++;
        if (i < node.n && key == node.keys[i]) return true;
        if (node.leaf) return false;
        return contains(node.children[i], key);
    }

    private void insertNonFull(BTreeNode node, int key) {
        if (node.leaf) {
            node.insertKeySorted(key);
        } else {
            int i = node.n - 1;
            while (i >= 0 && key < node.keys[i]) i--;
            i++;
            if (node.children[i].n == BTreeNode.MAX_KEYS) {
                splitChild(node, i);
                if (key > node.keys[i]) i++;
            }
            insertNonFull(node.children[i], key);
        }
    }

    private void splitChild(BTreeNode parent, int index) {
        BTreeNode y = parent.children[index];
        BTreeNode z = new BTreeNode(y.leaf);
        int midIndex = y.n / 2;
        int rightKeyCount = y.n - midIndex - 1;

        for (int j = 0; j < rightKeyCount; j++) {
            z.keys[j] = y.keys[midIndex + 1 + j];
        }

        if (!y.leaf) {
            for (int j = 0; j < rightKeyCount + 1; j++) {
                z.children[j] = y.children[midIndex + 1 + j];
            }
        }

        z.n = rightKeyCount;
        y.n = midIndex;

        for (int j = parent.n; j > index; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[index + 1] = z;

        for (int j = parent.n - 1; j >= index; j--) {
            parent.keys[j + 1] = parent.keys[j];
        }

        parent.keys[index] = y.keys[midIndex];
        parent.n++;
    }

    public void delete(int key) {
        if (root == null) return;
        deleteRecursive(root, key);
        if (root.n == 0 && !root.leaf) {
            root = root.children[0];
        } else if (root.n == 0 && root.leaf) {
            root = null;
        }
    }

    private void deleteRecursive(BTreeNode node, int key) {
        int idx = 0;
        while (idx < node.n && key > node.keys[idx]) idx++;

        // Caso 1: A chave está neste nó
        if (idx < node.n && node.keys[idx] == key) {
            if (node.leaf) {
                node.removeKeyAt(idx);
            } else {
                BTreeNode leftChild = node.children[idx];
                BTreeNode rightChild = node.children[idx + 1];
                if (rightChild.n > BTreeNode.MIN_KEYS) {
                    int succKey = getSuccessor(rightChild);
                    node.keys[idx] = succKey;
                    deleteRecursive(rightChild, succKey);
                } else if (leftChild.n > BTreeNode.MIN_KEYS) {
                    int predKey = getPredecessor(leftChild);
                    node.keys[idx] = predKey;
                    deleteRecursive(leftChild, predKey);
                } else {
                    leftChild.mergeWithRightSibling(node, idx);
                    deleteRecursive(leftChild, key);
                }
            }
        }

        // Caso 2: A chave está em um dos filhos
        else {
            if (node.leaf) return;

            BTreeNode child = node.children[idx];

            // Corrigir filho com menos de MIN_KEYS
            while (child.n < BTreeNode.MIN_KEYS) {
                BTreeNode leftSibling = (idx > 0) ? node.children[idx - 1] : null;
                BTreeNode rightSibling = (idx < node.n) ? node.children[idx + 1] : null;

                if (rightSibling != null && rightSibling.n > BTreeNode.MIN_KEYS) {
                    child.borrowFromRightSibling(node, idx);
                } else if (leftSibling != null && leftSibling.n > BTreeNode.MIN_KEYS) {
                    child.borrowFromLeftSibling(node, idx - 1);
                } else {
                    if (rightSibling != null) {
                        child.mergeWithRightSibling(node, idx);
                    } else if (leftSibling != null) {
                        leftSibling.mergeWithRightSibling(node, idx - 1);
                        child = leftSibling;
                        idx--;
                    }
                    break; // importante para não entrar em loop infinito se não for possível corrigir
                }

                // Caso a raiz tenha perdido todas as chaves, substituí-la
                if (root.n == 0 && !root.leaf) {
                    root = root.children[0];
                }
            }

            // Chamada recursiva (após garantir que o filho está consistente)
            deleteRecursive(child, key);
        }
    }


    private int getPredecessor(BTreeNode node) {
        BTreeNode cur = node;
        while (!cur.leaf) cur = cur.children[cur.n];
        return cur.keys[cur.n - 1];
    }

    private int getSuccessor(BTreeNode node) {
        BTreeNode cur = node;
        while (!cur.leaf) cur = cur.children[0];
        return cur.keys[0];
    }

    public void preOrder() {
        preOrderTraverse(root);
        System.out.println();
    }

    private void preOrderTraverse(BTreeNode node) {
        if (node == null) return;
        System.out.print("[");
        for (int i = 0; i < node.n; i++) {
            System.out.print(node.keys[i]);
            if (i < node.n - 1) System.out.print(", ");
        }
        System.out.print("] ");
        if (!node.leaf) {
            for (int i = 0; i <= node.n; i++) {
                preOrderTraverse(node.children[i]);
            }
        }
    }

    public void levelOrder() {
        if (root == null) {
            System.out.println("Árvore vazia");
            return;
        }
        java.util.Queue<BTreeNode> queue = new java.util.LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelCount = queue.size();
            while (levelCount-- > 0) {
                BTreeNode node = queue.poll();
                System.out.print("[");
                for (int i = 0; i < node.n; i++) {
                    System.out.print(node.keys[i]);
                    if (i < node.n - 1) System.out.print(", ");
                }
                System.out.print("] ");
                if (!node.leaf) {
                    for (int j = 0; j <= node.n; j++) {
                        queue.add(node.children[j]);
                    }
                }
            }
            System.out.println();
        }
    }
}
