public class RubroNegra {
 
    private static final boolean VERMELHO = true;
    private static final boolean PRETO    = false;
 
    // ──────────────── NÓ ────────────────
 
    private static class No {
        int valor;
        boolean cor;
        No esquerda, direita, pai;
 
        No(int valor, boolean cor) {
            this.valor = valor;
            this.cor   = cor;
        }
    }
 
    private final No NIL;  
    private No raiz;
 
    public RubroNegra() {
        NIL = new No(0, PRETO);
        NIL.esquerda = NIL;
        NIL.direita  = NIL;
        NIL.pai      = NIL;
        raiz = NIL;
    }
 
 
    private void rotacaoEsquerda(No x) {
        No y = x.direita;
        x.direita = y.esquerda;
        if (y.esquerda != NIL) y.esquerda.pai = x;
        y.pai = x.pai;
        if (x.pai == NIL)          raiz = y;
        else if (x == x.pai.esquerda) x.pai.esquerda = y;
        else                          x.pai.direita  = y;
        y.esquerda = x;
        x.pai = y;
    }
 
    private void rotacaoDireita(No y) {
        No x = y.esquerda;
        y.esquerda = x.direita;
        if (x.direita != NIL) x.direita.pai = y;
        x.pai = y.pai;
        if (y.pai == NIL)          raiz = x;
        else if (y == y.pai.direita)  y.pai.direita  = x;
        else                          y.pai.esquerda = x;
        x.direita = y;
        y.pai = x;
    }
 
    // ──────────────── INSERÇÃO ────────────────
 
    public void inserir(int valor) {
        No z = new No(valor, VERMELHO);
        z.esquerda = NIL;
        z.direita  = NIL;
        z.pai      = NIL;
 
        No y = NIL, x = raiz;
        while (x != NIL) {
            y = x;
            if (z.valor < x.valor)       x = x.esquerda;
            else if (z.valor > x.valor)  x = x.direita;
            else return; // duplicata ignorada
        }
 
        z.pai = y;
        if (y == NIL)             raiz = z;
        else if (z.valor < y.valor) y.esquerda = z;
        else                        y.direita  = z;
 
        corrigirInsercao(z);
    }
 
    private void corrigirInsercao(No z) {
        while (z.pai.cor == VERMELHO) {
            if (z.pai == z.pai.pai.esquerda) {
                No tio = z.pai.pai.direita;
                if (tio.cor == VERMELHO) {          // Caso 1
                    z.pai.cor     = PRETO;
                    tio.cor       = PRETO;
                    z.pai.pai.cor = VERMELHO;
                    z = z.pai.pai;
                } else {
                    if (z == z.pai.direita) {       // Caso 2
                        z = z.pai;
                        rotacaoEsquerda(z);
                    }
                    z.pai.cor     = PRETO;          // Caso 3
                    z.pai.pai.cor = VERMELHO;
                    rotacaoDireita(z.pai.pai);
                }
            } else {
                No tio = z.pai.pai.esquerda;
                if (tio.cor == VERMELHO) {
                    z.pai.cor     = PRETO;
                    tio.cor       = PRETO;
                    z.pai.pai.cor = VERMELHO;
                    z = z.pai.pai;
                } else {
                    if (z == z.pai.esquerda) {
                        z = z.pai;
                        rotacaoDireita(z);
                    }
                    z.pai.cor     = PRETO;
                    z.pai.pai.cor = VERMELHO;
                    rotacaoEsquerda(z.pai.pai);
                }
            }
        }
        raiz.cor = PRETO;
    }

 
    public void remover(int valor) {
        No z = buscarNo(raiz, valor);
        if (z == NIL) return;
 
        No y = z;
        boolean corOriginal = y.cor;
        No x;
 
        if (z.esquerda == NIL) {
            x = z.direita;
            transplantar(z, z.direita);
        } else if (z.direita == NIL) {
            x = z.esquerda;
            transplantar(z, z.esquerda);
        } else {
            y = minimo(z.direita);
            corOriginal = y.cor;
            x = y.direita;
            if (y.pai == z) {
                x.pai = y;
            } else {
                transplantar(y, y.direita);
                y.direita = z.direita;
                y.direita.pai = y;
            }
            transplantar(z, y);
            y.esquerda = z.esquerda;
            y.esquerda.pai = y;
            y.cor = z.cor;
        }
 
        if (corOriginal == PRETO) corrigirRemocao(x);
    }
 
    private void transplantar(No u, No v) {
        if (u.pai == NIL)             raiz = v;
        else if (u == u.pai.esquerda) u.pai.esquerda = v;
        else                          u.pai.direita  = v;
        v.pai = u.pai;
    }
 
    private void corrigirRemocao(No x) {
        while (x != raiz && x.cor == PRETO) {
            if (x == x.pai.esquerda) {
                No w = x.pai.direita;
                if (w.cor == VERMELHO) {
                    w.cor     = PRETO;
                    x.pai.cor = VERMELHO;
                    rotacaoEsquerda(x.pai);
                    w = x.pai.direita;
                }
                if (w.esquerda.cor == PRETO && w.direita.cor == PRETO) {
                    w.cor = VERMELHO;
                    x = x.pai;
                } else {
                    if (w.direita.cor == PRETO) {
                        w.esquerda.cor = PRETO;
                        w.cor          = VERMELHO;
                        rotacaoDireita(w);
                        w = x.pai.direita;
                    }
                    w.cor          = x.pai.cor;
                    x.pai.cor      = PRETO;
                    w.direita.cor  = PRETO;
                    rotacaoEsquerda(x.pai);
                    x = raiz;
                }
            } else {
                No w = x.pai.esquerda;
                if (w.cor == VERMELHO) {
                    w.cor     = PRETO;
                    x.pai.cor = VERMELHO;
                    rotacaoDireita(x.pai);
                    w = x.pai.esquerda;
                }
                if (w.direita.cor == PRETO && w.esquerda.cor == PRETO) {
                    w.cor = VERMELHO;
                    x = x.pai;
                } else {
                    if (w.esquerda.cor == PRETO) {
                        w.direita.cor = PRETO;
                        w.cor         = VERMELHO;
                        rotacaoEsquerda(w);
                        w = x.pai.esquerda;
                    }
                    w.cor          = x.pai.cor;
                    x.pai.cor      = PRETO;
                    w.esquerda.cor = PRETO;
                    rotacaoDireita(x.pai);
                    x = raiz;
                }
            }
        }
        x.cor = PRETO;
    }
 
    private No minimo(No no) {
        while (no.esquerda != NIL) no = no.esquerda;
        return no;
    }
 
 
    public boolean buscar(int valor) {
        return buscarNo(raiz, valor) != NIL;
    }
 
    private No buscarNo(No no, int valor) {
        if (no == NIL || valor == no.valor) return no;
        if (valor < no.valor) return buscarNo(no.esquerda, valor);
        return buscarNo(no.direita, valor);
    }
 
 
    public int altura() {
        return alturaRec(raiz);
    }
 
    private int alturaRec(No no) {
        if (no == NIL) return -1;
        return 1 + Math.max(alturaRec(no.esquerda), alturaRec(no.direita));
    }
 
 
    public void emOrdem() {
        emOrdemRec(raiz);
        System.out.println();
    }
 
    private void emOrdemRec(No no) {
        if (no != NIL) {
            emOrdemRec(no.esquerda);
            System.out.print(no.valor + "(" + (no.cor == VERMELHO ? "V" : "P") + ") ");
            emOrdemRec(no.direita);
        }
    }
 
    public static void main(String[] args) {
        RubroNegra arvore = new RubroNegra();
 
        System.out.println(" Árvore Rubro-Negra \n");
 
        int[] valores = {10, 20, 30, 15, 25, 5, 1};
        for (int v : valores) arvore.inserir(v);
        System.out.print("Em ordem (V=vermelho, P=preto): ");
        arvore.emOrdem();
        System.out.println("Altura: " + arvore.altura());
 
        System.out.println("\nBusca por 15: " + arvore.buscar(15));
        System.out.println("Busca por 99: " + arvore.buscar(99));
 
        arvore.remover(20);
        System.out.print("\nApós remover 20: ");
        arvore.emOrdem();
        System.out.println("Altura após remoção: " + arvore.altura());
    }
}

