
public class Main {
    public static void main(String[] args) {

        if (args.length != 4) {
            System.out.println(
                    "Use o formato: java -jar vereadores.jar <codigo_cidade> <arquivo_candidatos> <arquivo_votacao> <data>");
            return;
        }

        int codigoCidade = Integer.parseInt(args[0]);
        String arquivoCandidatos = args[1];
        String arquivoVotacao = args[2];
        String dataEleicao = args[3];

        Processador processador = new Processador(arquivoCandidatos, arquivoVotacao, codigoCidade, dataEleicao);
        processador.processaCandidatosPartidos();
        processador.processaArquivoVotacao();
        processador.imprimeRelatorios();
    }
}
