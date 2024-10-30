package PJBL;

import java.util.ArrayList;

public class Notas {

    private String titulo;
    private String conteudo;

    public Notas(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public String exibirNota() {
        return "Título: " + titulo + "\nConteúdo: " + conteudo;
    }
}

class NotaImportante extends Notas {

    public NotaImportante(String titulo, String conteudo) {
        super(titulo, conteudo);
    }

    @Override
    public String exibirNota() {
        return "Título (Nota Importante!): " + getTitulo() + "\nConteúdo: " + getConteudo();
    }
}

class NotaFinalizada extends Notas {

    public NotaFinalizada(String titulo, String conteudo) {
        super(titulo, conteudo);
    }

    @Override
    public String exibirNota() {
        return "Título (Nota Finalizada!): " + getTitulo() + "\nConteúdo: " + getConteudo();
    }
}

class Categorias {
    private String nomeCategoria;
    private ArrayList<Notas> listaDeNotas;

    public Categorias(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
        this.listaDeNotas = new ArrayList<>();
    }

    public void adicionarNota(Notas nota) {
        listaDeNotas.add(nota);
    }

    public void exibirNotas() {
        System.out.println("Categoria: " + nomeCategoria);
        for (Notas nota : listaDeNotas) {
            System.out.println(nota.exibirNota());
        }
    }
}

abstract class Arquivo {
    abstract void exportarArquivo();
    abstract void importarArquivo();
}
