public class Main {
    public static void main(String[] args) {

        if (args.length != 4) {
            System.out.println(
                    "Uso correto: java -jar vereadores.jar <codigo_cidade> <arquivo_candidatos> <arquivo_votacao> <data_eleicao>");
            return;
        }

        // Parâmetros da linha de comando
        int codigoCidade = Integer.parseInt(args[0]);
        String caminhoArquivoCandidatos = args[1];
        String caminhoArquivoVotacao = args[2];
        String dataEleicao = args[3];

        // Leitura dos arquivos CSV
        EleicaoArquivoCSV leitorCandidatos = new EleicaoArquivoCSV(caminhoArquivoCandidatos, "ISO-8859-1");
        leitorCandidatos.leArquivo();
        EleicaoArquivoCSV leitorVotacao = new EleicaoArquivoCSV(caminhoArquivoVotacao, "ISO-8859-1");
        leitorVotacao.leArquivo();

        Eleicao eleicao = new Eleicao(codigoCidade, dataEleicao);
        // Processamento de candidatos e votação
        eleicao.processaCandidatosPartidos(leitorCandidatos.getLinhas(), leitorCandidatos.getIndiceCabecalho());
        eleicao.processaVotacao(leitorVotacao.getLinhas(), leitorVotacao.getIndiceCabecalho());

        // Relatórios
        EleicaoRelatorios relatorios = new EleicaoRelatorios(eleicao);
        relatorios.imprimeRelatorios();
    }
}
