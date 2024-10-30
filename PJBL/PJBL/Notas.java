package PJBL;

import java.util.ArrayList;

// Classe principal para representar uma nota
public class Notas {
    private String titulo; // Título da nota
    private String conteudo; // Conteúdo da nota

    // Construtor para inicializar título e conteúdo
    public Notas(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
    }

    // Método para exibir a nota sem categoria
    public String exibirNotaSemCategoria(String titulo, String conteudo) {
        return "Título: " + titulo + "\nConteúdo: " + conteudo;
    }
}

// Classe para representar notas importantes, herda de Notas
class NotaImportante extends Notas {
    // Construtor que chama o construtor da classe pai
    public NotaImportante(String titulo, String conteudo) {
        super(titulo, conteudo);
    }

    // Método para exibir a nota como importante
    public String exibirNotaSemCategoria(String titulo, String conteudo) {
        return "Titulo (Nota Importante!): " + titulo + "\nConteudo: " + conteudo;
    }
}

// Classe para representar notas finalizadas, herda de Notas
class NotaFinalizada extends Notas {
    // Construtor que chama o construtor da classe pai
    public NotaFinalizada(String titulo, String conteudo) {
        super(titulo, conteudo);
    }

    // Método para exibir a nota como finalizada
    public String exibirNotaSemCategoria(String titulo, String conteudo) {
        return "Titulo (Nota Finalizada!): " + titulo + "\nConteudo: " + conteudo;
    }
}

// Classe para gerenciar categorias de notas
class Categorias {
    private String nomeCategoria; // Nome da categoria
    private ArrayList<Notas> listaDeNotas; // Lista de notas
    private ArrayList<NotaFinalizada> listaDeNotasFinalizadas; // Lista de notas finalizadas
    private ArrayList<NotaImportante> listaDeNotasImportantes; // Lista de notas importantes

    // Construtor para inicializar a categoria e suas listas
    public Categorias(String nomeCategoria, ArrayList<Notas> listaDeNotas,
                      ArrayList<NotaFinalizada> listaDeNotasFinalizadas,
                      ArrayList<NotaImportante> listaDeNotasImportantes) {
        this.nomeCategoria = nomeCategoria;
        this.listaDeNotas = new ArrayList<>(); // Inicializa a lista de notas
        this.listaDeNotasFinalizadas = new ArrayList<>(); // Inicializa a lista de notas finalizadas
        this.listaDeNotasImportantes = new ArrayList<>(); // Inicializa a lista de notas importantes
    }

    // Método para adicionar uma nota à lista de notas
    public void adicionarNota(Notas nota) {
        listaDeNotas.add(nota);
    }

    // Método para adicionar uma nota finalizada à lista
    public void adicionarNotaFinalizada(NotaFinalizada notaFinalizada) {
        listaDeNotasFinalizadas.add(notaFinalizada);
    }

    // Método para adicionar uma nota importante à lista
    public void adicionarNotaImportante(NotaImportante notaImportante) {
        listaDeNotasImportantes.add(notaImportante);
    }

    // Método para exibir todas as notas da categoria
    public void exibirNotas() {
        System.out.println("Categoria: " + nomeCategoria); // Exibe o nome da categoria

        // Exibe todas as notas
        for (Notas nota : listaDeNotas) {
            System.out.println(nota);
        }

        // Exibe todas as notas finalizadas
        for (NotaFinalizada notaFinalizada : listaDeNotasFinalizadas) {
            System.out.println(notaFinalizada);
        }

        // Exibe todas as notas importantes
        for (NotaImportante notaImportante : listaDeNotasImportantes) {
            System.out.println(notaImportante);
        }
    }
}

// Classe abstrata para operações de arquivo
abstract class Arquivo {
    abstract void exportarArquivo(); // Método abstrato para exportar um arquivo
    abstract void importarArquivo(); // Método abstrato para importar um arquivo
}

// Classe vazia, sem funcionalidade
class oi {
    // Esta classe não contém nenhum código ou propósito específico
}
