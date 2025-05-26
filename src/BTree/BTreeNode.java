package BTree;

public class BTreeNode {
    int[] keys;
    BTreeNode[] children;
    int n;            // Número atual de chaves no nó
    boolean leaf;     // Indica se o nó é folha (true) ou interno (false)

    // Ordem da Árvore B (máximo de filhos por nó)
    static final int M = 5;
    // Número máximo de chaves em um nó = M - 1 = 4
    static final int MAX_KEYS = M - 1;
    // Número mínimo de chaves em um nó (exceto raiz) = ceil(M/2) - 1 = 2:contentReference[oaicite:3]{index=3}
    static final int MIN_KEYS = (int) Math.ceil(M / 2.0) - 1;

    public BTreeNode(boolean isLeaf) {
        this.leaf = isLeaf;
        this.keys = new int[M];               // até M-1 chaves, usando vetor de tamanho M para facilitar cisão
        this.children = new BTreeNode[M + 1]; // até M filhos, vetor de tamanho M+1 para operações de cisão
        this.n = 0;
    }

    public boolean isLeaf() {
        return this.leaf;
    }

    // Insere uma nova chave neste nó (que deve ter espaço disponível) de forma ordenada.
    public void insertKeySorted(int key) {
        int i = this.n - 1;
        // Desloca chaves maiores para a direita, abrindo espaço para a nova chave
        while (i >= 0 && this.keys[i] > key) {
            this.keys[i + 1] = this.keys[i];
            i--;
        }
        this.keys[i + 1] = key;
        this.n++;
    }

    // Remove a chave no índice idx deste nó (nó folha ou interno). 
    // Realiza o deslocamento das chaves à esquerda para preencher o espaço.
    public void removeKeyAt(int idx) {
        if (idx < 0 || idx >= this.n) return;
        for (int i = idx; i < this.n - 1; i++) {
            this.keys[i] = this.keys[i + 1];
        }
        this.n--;
        // Observação: os ponteiros de children (filhos) devem ser ajustados pelo chamador se necessário.
    }

    // Redistribui (rota) uma chave do irmão esquerdo para este nó (irmão direito está com déficit).
    // parentKeyIndex é o índice da chave no pai que separa este nó e seu irmão esquerdo.
    public void borrowFromLeftSibling(BTreeNode parent, int parentKeyIndex) {
        BTreeNode leftSibling = parent.children[parentKeyIndex];
        // Desloca todas as chaves deste nó para a direita para liberar posição 0
        for (int j = this.n - 1; j >= 0; j--) {
            this.keys[j + 1] = this.keys[j];
        }
        // Se não for folha, desloca também os ponteiros de filho para a direita
        if (!this.leaf) {
            for (int j = this.n; j >= 0; j--) {
                this.children[j + 1] = this.children[j];
            }
            // O último filho do irmão esquerdo passa a ser o primeiro deste nó
            this.children[0] = leftSibling.children[leftSibling.n];
        }
        // Move a chave separadora do pai para este nó (última posição do irmão esquerdo desce para cá)
        this.keys[0] = parent.keys[parentKeyIndex];
        this.n++;
        // Sobe a maior chave do irmão esquerdo para o pai:contentReference[oaicite:4]{index=4}
        parent.keys[parentKeyIndex] = leftSibling.keys[leftSibling.n - 1];
        // Reduz o número de chaves do irmão esquerdo, pois a maior chave foi movida
        leftSibling.n--;
        // (Os ponteiros de children do irmão esquerdo já foram ajustados acima)
    }

    // Redistribui (rota) uma chave do irmão direito para este nó (irmão esquerdo com déficit).
    // parentKeyIndex é o índice da chave no pai que separa este nó (esquerdo) do irmão direito.
    public void borrowFromRightSibling(BTreeNode parent, int parentKeyIndex) {
        BTreeNode rightSibling = parent.children[parentKeyIndex + 1];
        // A chave separadora do pai desce como nova última chave deste nó
        this.keys[this.n] = parent.keys[parentKeyIndex];
        this.n++;
        // Se não for folha, o primeiro ponteiro de filho do irmão direito vem para o final deste nó
        if (!this.leaf) {
            this.children[this.n] = rightSibling.children[0];
        }
        // Sobe a menor chave do irmão direito para o pai
        parent.keys[parentKeyIndex] = rightSibling.keys[0];
        // Remove a primeira chave do irmão direito, deslocando as demais para esquerda
        for (int j = 1; j < rightSibling.n; j++) {
            rightSibling.keys[j - 1] = rightSibling.keys[j];
        }
        // Se não for folha, desloca também os ponteiros de filhos do irmão direito para esquerda
        if (!rightSibling.leaf) {
            for (int j = 1; j <= rightSibling.n; j++) {
                rightSibling.children[j - 1] = rightSibling.children[j];
            }
        }
        // Diminui o contador de chaves do irmão direito, que cedeu sua menor chave
        rightSibling.n--;
    }

    // Concatena (merge) este nó com seu irmão da direita, removendo a chave separadora do pai.
    // Este método é chamado no nó esquerdo; parentKeyIndex é o índice da chave no pai que separa os dois nós.
    public void mergeWithRightSibling(BTreeNode parent, int parentKeyIndex) {
        BTreeNode rightSibling = parent.children[parentKeyIndex + 1];
        // Desce a chave do pai para este nó (coloca na posição após as chaves atuais)
        this.keys[this.n] = parent.keys[parentKeyIndex];
        this.n++;
        // Copia todas as chaves do irmão direito para este nó (após a chave descida do pai)
        for (int j = 0; j < rightSibling.n; j++) {
            this.keys[this.n + j] = rightSibling.keys[j];
        }
        // Se não for folha, copia também todos os filhos do irmão direito para este nó
        if (!this.leaf) {
            for (int j = 0; j <= rightSibling.n; j++) {
                this.children[this.n + j] = rightSibling.children[j];
            }
        }
        // Atualiza o contador de chaves deste nó (soma as chaves que vieram do irmão)
        this.n += rightSibling.n;
        // Remove a chave separadora do pai (deslocando as chaves após ela para esquerda)
        for (int j = parentKeyIndex; j < parent.n - 1; j++) {
            parent.keys[j] = parent.keys[j + 1];
        }
        // Remove o ponteiro do irmão direito do vetor de filhos do pai (deslocando restantes para esquerda)
        for (int j = parentKeyIndex + 1; j < parent.n; j++) {
            parent.children[j] = parent.children[j + 1];
        }
        parent.n--;  // diminui a contagem de chaves no pai
        // (O objeto rightSibling será elegível para GC em Java, pois o pai perdeu a referência a ele)
    }
}
