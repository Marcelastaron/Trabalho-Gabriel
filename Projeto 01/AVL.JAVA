public class AVL {
 
 
    private static class No {
        int valor;
        int altura;
        No esquerda, direita;
 
        No(int valor) {
            this.valor = valor;
            this.altura = 0;
            this.esquerda = null;
            this.direita = null;
        }
    }
 
    private No raiz;
 
    public AVL() {
        this.raiz = null;
    }
 
   
 
    private int alturaNо(No no) {
        return (no == null) ? -1 : no.altura;
    }
 
    private void atualizarAltura(No no) {
        no.altura = 1 + Math.max(alturaNо(no.esquerda), alturaNо(no.direita));
    }
 
    private int fatorBalanceamento(No no) {
        return (no == null) ? 0 : alturaNо(no.esquerda) - alturaNо(no.direita);
    }
 
    // ──────────────── ROTAÇÕES ────────────────
 
    private No rotacaoDireita(No y) {
        No x = y.esquerda;
        No T2 = x.direita;
 
        x.direita = y;
        y.esquerda = T2;
 
        atualizarAltura(y);
        atualizarAltura(x);
        return x;
    }
 
    private No rotacaoEsquerda(No x) {
        No y = x.direita;
        No T2 = y.esquerda;
 
        y.esquerda = x;
        x.direita = T2;
 
        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }
 
    private No balancear(No no) {
        atualizarAltura(no);
        int fb = fatorBalanceamento(no);
 
        // Caso Esquerda-Esquerda
        if (fb > 1 && fatorBalanceamento(no.esquerda) >= 0)
            return rotacaoDireita(no);
 
        // Caso Esquerda-Direita
        if (fb > 1 && fatorBalanceamento(no.esquerda) < 0) {
            no.esquerda = rotacaoEsquerda(no.esquerda);
            return rotacaoDireita(no);
        }
 
        // Caso Direita-Direita
        if (fb < -1 && fatorBalanceamento(no.direita) <= 0)
            return rotacaoEsquerda(no);
 
        // Caso Direita-Esquerda
        if (fb < -1 && fatorBalanceamento(no.direita) > 0) {
            no.direita = rotacaoDireita(no.direita);
            return rotacaoEsquerda(no);
        }
 
        return no; // quando ja tiver balanceado
    }
 
  
    public void inserir(int valor) {
        raiz = inserirRec(raiz, valor);
    }
 
    private No inserirRec(No no, int valor) {
        if (no == null) return new No(valor);
        if (valor < no.valor)
            no.esquerda = inserirRec(no.esquerda, valor);
        else if (valor > no.valor)
            no.direita = inserirRec(no.direita, valor);
        else
            return no; // duplicata ignorada
        return balancear(no);
    }
 
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
            if (no.esquerda == null) return no.direita;
            if (no.direita == null) return no.esquerda;
            No sucessor = minimo(no.direita);
            no.valor = sucessor.valor;
            no.direita = removerRec(no.direita, sucessor.valor);
        }
        return balancear(no);
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
        return alturaNо(raiz);
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
        AVL arvore = new AVL();
 
        System.out.println(" Árvore AVL ");
 
        int[] valores = {10, 20, 30, 40, 50, 25};
        for (int v : valores) arvore.inserir(v);
        System.out.print("Em ordem: ");
        arvore.emOrdem();
        System.out.println("Altura (AVL balanceia): " + arvore.altura());
 
        System.out.println("\nBusca por 25: " + arvore.buscar(25));
        System.out.println("Busca por 99: " + arvore.buscar(99));
 
        arvore.remover(40);
        System.out.print("\nApós remover 40: ");
        arvore.emOrdem();
        System.out.println("Altura após remoção: " + arvore.altura());
    }
}