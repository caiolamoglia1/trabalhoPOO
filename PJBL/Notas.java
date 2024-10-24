import java.util.ArrayList;

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

    class NotaFinalizada extends Notas {
        public NotaFinalizada(String titulo, String conteudo) {
            super(titulo, conteudo);
        }


        public String exibirNotaSemCategoria(String titulo, String conteudo) {
            return "Titulo (Nota finalizada): " + titulo + "\nConteudo: " + conteudo;
        }
    }

    class Categorias {
        private String nomeCategoria;
        private ArrayList<Notas> listaDeNotas;

        public Categorias(String nomeCategoria, ArrayList<Notas> listaDeNotas) {
            this.nomeCategoria = nomeCategoria;
            this.listaDeNotas = new ArrayList<>();
        }

        public void adicionarNota(Notas nota){
            listaDeNotas.add(nota);
        

        public void exibirNotas() {
            System.out.println("Categoria: " + nomeCategoria);
            for (Notas nota : listaDeNotas) {
                System.out.println(nota);
            }
        }
    }

    abstract class Arquivo{
        abstract void exportarArquivo();
        abstract void importarArquivo();
    }
}