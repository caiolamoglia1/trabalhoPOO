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

    class Categorias {
        private String nomeCategoria;
        private ArrayList<Notas> listaDeNotas;
        private ArrayList<NotaFinalizada> listaDeNotasFinalizadas;
        private ArrayList<NotaImportante> listaDeNotasImportantes;


        public Categorias(String nomeCategoria, ArrayList <Notas> listaDeNotas, ArrayList<NotaFinalizada> listaDeNotasFinalizadas, ArrayList<NotaImportante> listaDeNotasImportantes) {
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