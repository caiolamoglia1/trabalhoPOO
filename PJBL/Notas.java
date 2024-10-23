import java.util.ArrayList;

public class Notas {
        private String titulo;
        private String conteudo;

        public Notas(String titulo, String conteudo) {
            this.titulo = titulo;
            this.conteudo = conteudo;
        }
    }

    class NotaImportante extends Notas{
        public NotaImportante(String titulo, String conteudo) {
            super(titulo, conteudo);
        }
    }

    class NotaFinalizada extends Notas{
        public NotaFinalizada(String titulo, String conteudo) {
            super(titulo, conteudo);
        }
    }

    class Usuario{
        private String usuario;
        private String senha;
            
        public Usuario(String usuario, String senha) {
            this.usuario = usuario;
            this.senha = senha;
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

