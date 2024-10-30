
import java.util.ArrayList;
import javax.swing.*;

    public class Notas {
        private String titulo;
        private String conteudo;

        public Notas(String titulo, String conteudo) {
            this.titulo = titulo;
            this.conteudo = conteudo;
        }

         public String exibirNotaSemCategoria(String titulo, String conteudo){
            return "Título: " + titulo + "\nConteúdo: " + conteudo;
        }
    }

    class NotaImportante extends Notas{
        public NotaImportante(String titulo, String conteudo) {
            super(titulo, conteudo);
        }
        public String exibirNotaSemCategoria(String titulo, String conteudo){
            return "Titulo (Nota Importante!): " + titulo + "\nConteudo: " + conteudo;
        }
    }

    class NotaFinalizada extends Notas{
        public NotaFinalizada(String titulo, String conteudo) {
            super(titulo, conteudo);
        }
        public String exibirNotaSemCategoria(String titulo, String conteudo){
            return "Titulo (Nota Finalizada!): " + titulo + "\nConteudo: " + conteudo;
        }
    }

    class Categorias {
        private String nomeCategoria;
        private ArrayList<Notas> listaDeNotas;
        private ArrayList<NotaFinalizada> listaDeNotasFinalizadas;
        private ArrayList<NotaImportante> listaDeNotasImportantes;


        public Categorias(String nomeCategoria) {
            this.nomeCategoria = nomeCategoria;
            this.listaDeNotas = new ArrayList<>();
            this.listaDeNotasFinalizadas = new ArrayList<>();
            this.listaDeNotasImportantes = new ArrayList<>();
        }

        public void adicionarNota(Notas nota) {
            listaDeNotas.add(nota);
        }
        public void adicionarNotaFinalizada(NotaFinalizada notaFinalizada) {
            listaDeNotasFinalizadas.add(notaFinalizada);
        }
        public void adicionarNotaImportante(NotaImportante notaImportante) {
            listaDeNotasImportantes.add(notaImportante);
        }

        public void exibirNotas() {
            System.out.println("Categoria: " + nomeCategoria);
            for (Notas nota : listaDeNotas) {
                System.out.println(nota);
            }
            for (NotaFinalizada notaFinalizada : listaDeNotasFinalizadas){
                System.out.println(notaFinalizada);
            }
            for (NotaImportante notaImportante: listaDeNotasImportantes){
                System.out.println(listaDeNotasImportantes);
            }
        }
    }

    abstract class Arquivo {
        abstract void exportarArquivo();
        abstract void importarArquivo();
    }
class CriarNota {
    public void criarNovaNota(Categorias categoria) {
        String titulo = JOptionPane.showInputDialog("Digite o título da nota:");
        String conteudo = JOptionPane.showInputDialog("Digite o conteúdo da nota:");

        if (titulo != null && conteudo != null) {
            Notas novaNota = new Notas(titulo, conteudo);
            categoria.adicionarNota(novaNota);
            JOptionPane.showMessageDialog(null, "Nota adicionada com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Criação de nota cancelada.");
        }
    }
}

class Main {
    public static void main(String[] args) {
        Categorias categoria = new Categorias("Minhas Notas");
        CriarNota criarNota = new CriarNota();
        criarNota.criarNovaNota(categoria);
        categoria.exibirNotas();
    }
}