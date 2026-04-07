public class BST {
 
    // ──────────────── NÓ ────────────────
 
    private static class No {
        int valor;
        No esquerda, direita;
 
        No(int valor) {
            this.valor = valor;
            this.esquerda = null;
            this.direita = null;
        }
    }
 
    private No raiz;
 
    public BST() {
        this.raiz = null;
    }
 
    // ──────────────── INSERÇÃO ────────────────
 
    public void inserir(int valor) {
        raiz = inserirRec(raiz, valor);
    }
 
    private No inserirRec(No no, int valor) {
        if (no == null) return new No(valor);
        if (valor < no.valor)
            no.esquerda = inserirRec(no.esquerda, valor);
        else if (valor > no.valor)
            no.direita = inserirRec(no.direita, valor);
        // duplicata: ignora
        return no;
    }
 
    // ──────────────── REMOÇÃO ────────────────
 
    public void remover(int valor) {
        raiz = removerRec(raiz, valor);
    }
 
    private No removerRec(No no, int valor) {
        if (no == null) return null;
 
        if (valor < no.valor) {
            no.esquerda = removerRec(no.esquerda, valor);
        } else if (valor > no.valor) {
            no.direita = removerRec(no.direita, valor);
        } else {
            // Caso 1: sem filhos
            if (no.esquerda == null && no.direita == null) return null;
            // Caso 2: um filho
            if (no.esquerda == null) return no.direita;
            if (no.direita == null) return no.esquerda;
            // Caso 3: dois filhos → menor da subárvore direita
            No sucessor = minimo(no.direita);
            no.valor = sucessor.valor;
            no.direita = removerRec(no.direita, sucessor.valor);
        }
        return no;
    }
 
    private No minimo(No no) {
        while (no.esquerda != null) no = no.esquerda;
        return no;
    }
 
 
    public boolean buscar(int valor) {
        return buscarRec(raiz, valor);
    }
 
    private boolean buscarRec(No no, int valor) {
        if (no == null) return false;
        if (valor == no.valor) return true;
        if (valor < no.valor) return buscarRec(no.esquerda, valor);
        return buscarRec(no.direita, valor);
    }
 
 
    public int altura() {
        return alturaRec(raiz);
    }
 
    private int alturaRec(No no) {
        if (no == null) return -1;
        return 1 + Math.max(alturaRec(no.esquerda), alturaRec(no.direita));
    }
 
    public void emOrdem() {
        emOrdemRec(raiz);
        System.out.println();
    }
 
    private void emOrdemRec(No no) {
        if (no != null) {
            emOrdemRec(no.esquerda);
            System.out.print(no.valor + " ");
            emOrdemRec(no.direita);
        }
    }
 
 
    public static void main(String[] args) {
        BST arvore = new BST();
 
        System.out.println("=== Árvore Binária de Busca (BST) ===\n");
 
        int[] valores = {50, 30, 70, 20, 40, 60, 80};
        for (int v : valores) arvore.inserir(v);
        System.out.print("Em ordem: ");
        arvore.emOrdem();
        System.out.println("Altura: " + arvore.altura());
 
        System.out.println("\nBusca por 40: " + arvore.buscar(40));
        System.out.println("Busca por 99: " + arvore.buscar(99));
 
        arvore.remover(30);
        System.out.print("\nApós remover 30: ");
        arvore.emOrdem();
 
        arvore.remover(50);
        System.out.print("Após remover 50 (raiz): ");
        arvore.emOrdem();
        System.out.println("Altura final: " + arvore.altura());
    }
}
 