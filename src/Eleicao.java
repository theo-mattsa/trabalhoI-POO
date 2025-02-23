import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

public class Eleicao {

  private String pathArquivoCandidatos;
  private String pathArquivoVotacao;

  // Data da eleicao
  LocalDate dataEleicao;

  private int codigoCidade;
  private int quantidadeEleitos;

  // Partidos (numero partido -> Partido)
  private Map<String, Partido> partidos = new HashMap<>();

  // Candidatos (numero candidato -> Candidato)
  private Map<String, Candidato> candidatos = new HashMap<>();

  // Formatter de data
  private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public Eleicao(String pathArquivoCandidatos, String pathArquivoVotacao, int codigoCidade, String dataEleicao) {
    this.pathArquivoCandidatos = pathArquivoCandidatos;
    this.pathArquivoVotacao = pathArquivoVotacao;
    this.codigoCidade = codigoCidade;
    this.dataEleicao = LocalDate.parse(dataEleicao, formatterDate);
  }

  public void processaCandidatosPartidos() {

    try {
      FileInputStream fin = new FileInputStream(this.pathArquivoCandidatos);
      InputStreamReader r = new InputStreamReader(fin, "ISO-8859-1");
      BufferedReader br = new BufferedReader(r);

      // Lê o cabecalho do CSV
      String linha = br.readLine();

      // Remove aspas da linha e da um split de valores
      String[] cabecalhos = linha.replace("\"", "").split(";");

      // Cria um map de índices das colunas
      Map<String, Integer> indiceCabecalho = new HashMap<>();
      for (int i = 0; i < cabecalhos.length; i++) {
        indiceCabecalho.put(cabecalhos[i], i);
      }

      while ((linha = br.readLine()) != null) {

        String[] valores = linha.replace("\"", "").split(";");

        // Informações do partido
        String numeroPartido = valores[indiceCabecalho.get("NR_PARTIDO")];
        String siglaPartido = valores[indiceCabecalho.get("SG_PARTIDO")];

        Partido p;
        if (this.partidos.containsKey(numeroPartido)) {
          p = partidos.get(numeroPartido);
        } else {
          p = new Partido(numeroPartido, siglaPartido);
          partidos.put(numeroPartido, p);
        }

        int codigoCidadeArquivo = Integer.parseInt(valores[indiceCabecalho.get("SG_UE")]);
        int codigoCargo = Integer.parseInt(valores[indiceCabecalho.get("CD_CARGO")]);

        if (codigoCidadeArquivo != this.codigoCidade || codigoCargo != 13)
          continue;

        // Informações do candidato
        int numFederacao = Integer.parseInt(valores[indiceCabecalho.get("NR_FEDERACAO")]);
        int genero = Integer.parseInt(valores[indiceCabecalho.get("CD_GENERO")]);
        String numeroCandidato = valores[indiceCabecalho.get("NR_CANDIDATO")];
        String nomeCandidatoUrna = valores[indiceCabecalho.get("NM_URNA_CANDIDATO")];
        String dataNascimento = valores[indiceCabecalho.get("DT_NASCIMENTO")];
        Boolean candidatoEleito = false;

        int codigoCandidatoEleito = Integer.parseInt(valores[indiceCabecalho.get("CD_SIT_TOT_TURNO")]);
        if (codigoCandidatoEleito == -1) { // Candidatura inválida
          continue;
        } else if (codigoCandidatoEleito == 2 || codigoCandidatoEleito == 3) { // Candidato eleito
          candidatoEleito = true;
          this.quantidadeEleitos++;
        }

        Candidato candidato = new Candidato();
        candidato.setEleito(candidatoEleito);
        candidato.setNumeroCandidato(numeroCandidato);
        candidato.setNomeUrna(nomeCandidatoUrna);
        candidato.setPartido(p);
        candidato.setParticipaFederacao(numFederacao != -1);
        candidato.setDataNascimento(LocalDate.parse(dataNascimento, formatterDate));

        if (genero == 2) {
          candidato.setGenero(Genero.MASCULINO);
        } else if (genero == 4) {
          candidato.setGenero(Genero.FEMININO);
        }

        p.insereCandidato(candidato);
        this.candidatos.put(numeroCandidato, candidato);
      }
      br.close();
    } catch (IOException e) {
      System.out.println("Erro de I/O");
      e.printStackTrace();
    }
  }

  public void processaArquivoVotacao() {
    try {
      FileInputStream fin = new FileInputStream(this.pathArquivoVotacao);
      InputStreamReader r = new InputStreamReader(fin, "ISO-8859-1");
      BufferedReader br = new BufferedReader(r);

      // Lê o cabecalho do CSV
      String linha = br.readLine();

      // Remove aspas da linha e da um split de valores
      String[] cabecalhos = linha.replace("\"", "").split(";");

      // Cria um map de índices das colunas do csv
      Map<String, Integer> indiceCabecalho = new HashMap<>();
      for (int i = 0; i < cabecalhos.length; i++) {
        indiceCabecalho.put(cabecalhos[i], i);
      }

      while ((linha = br.readLine()) != null) {
        String[] valores = linha.replace("\"", "").split(";");
        int codigoCidadeArquivo = Integer.parseInt(valores[indiceCabecalho.get("CD_MUNICIPIO")]);
        int codigoCargo = Integer.parseInt(valores[indiceCabecalho.get("CD_CARGO")]);

        if (codigoCidadeArquivo != this.codigoCidade || codigoCargo != 13)
          continue;

        int quantidadeVotos = Integer.parseInt(valores[indiceCabecalho.get("QT_VOTOS")]);
        int numeroCandidato = Integer.parseInt(valores[indiceCabecalho.get("NR_VOTAVEL")]);

        if (numeroCandidato >= 10000 && numeroCandidato <= 99999) {
          Candidato c = candidatos.get(String.valueOf(numeroCandidato));
          c.incrementaQuantidadeVotos(quantidadeVotos);
          c.getPartido().incrementaVotosNominais(quantidadeVotos);
        } else if (numeroCandidato >= 10 && numeroCandidato <= 99
            && !(numeroCandidato >= 95 && numeroCandidato <= 98)) {
          partidos.get(String.valueOf(numeroCandidato)).incrementaVotosLegenda(quantidadeVotos);
        }

      }

      br.close();
    } catch (IOException e) {
      System.out.println("Erro de I/O");
      e.printStackTrace();
    }
  }

  public void imprimeRelatorios() {

    LinkedList<Candidato> listaCandidatos = new LinkedList<>(this.candidatos.values());
    LinkedList<Partido> listaPartidos = new LinkedList<>(this.partidos.values());

    // Ordena por quantiade de votos e idade (em caso de empate de votos)
    Collections.sort(listaCandidatos, new Candidato.CandidatoComparator(false));

    // Ordena por quantidade de votos (nominais e legenda) e numero (caso empate)
    Collections.sort(listaPartidos);

    // Eleitos == quantidade de vagas
    System.out.println("Número de vagas: " + this.quantidadeEleitos);
    System.out.println();
    System.out.println("Vereadores eleitos:");

    // Imprime os candidatos eleitos e obtem dados importantes
    int index = 1;
    for (Candidato c : listaCandidatos) {
      if (c.getEleito()) {
        String output = index + " - ";
        System.out.print(output);
        System.out.println(c);
        index++;
      }
    }

    // Imprime os candidatos mais votados
    System.out.println();
    System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
    for (int i = 0; i < this.quantidadeEleitos; i++) {
      Candidato c = listaCandidatos.get(i);
      String output = (i + 1) + " - ";
      System.out.println(output + c);
    }

    System.out.println();
    System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
    System.out.println("(com sua posição no ranking de mais votados)");
    for (int i = 0; i < this.quantidadeEleitos; i++) {
      Candidato c = listaCandidatos.get(i);
      if (!c.getEleito()) {
        String output = (i + 1) + " - ";
        System.out.println(output + c);
      }
    }

    System.out.println();
    System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
    System.out.println("(com sua posição no ranking de mais votados)");
    index = 1;
    for (Candidato c : listaCandidatos) {
      if (c.getEleito() && listaCandidatos.indexOf(c) >= this.quantidadeEleitos) {
        String output = index + " - ";
        System.out.println(output + c);
      }
      index++;
    }

    System.out.println();
    System.out.println("Votação dos partidos e número de candidatos eleitos:");
    index = 1;
    for (Partido p : listaPartidos) {
      String output = index + " - ";
      System.out.println(output + p);
      index++;
    }

    Collections.sort(listaPartidos, Partido.comparaPorCandidatoMaisVotado());
    System.out.println();
    System.out.println("Primeiro e último colocados de cada partido:");
    index = 1;
    for (Partido p : listaPartidos) {
      // Se for igual 1 no caso: ALEMAO VOTO 0 2x
      if (p.getCandidatos().isEmpty() || p.getCandidatos().size() == 1) // Caso o partido nao tenha candidatos
        continue;
      String output = index + " - ";
      System.out.println(output + p.getRelatorioMaisEMenosCandidatoVotado());
      index++;
    }

    System.out.println();
    System.out.println("Eleitos, por faixa etária (na data da eleição):");
    Map<String, Integer> candidatosPorIdade = new HashMap<>();

    for (Candidato c : listaCandidatos) {

      if (!c.getEleito())
        continue;

      String faixa = "";
      int idade = c.getIdade(dataEleicao);
      if (idade < 30) {
        faixa = "<30";
      } else if (idade >= 30 && idade < 40) {
        faixa = "30-39";
      } else if (idade >= 40 && idade < 50) {
        faixa = "40-49";
      } else if (idade >= 50 && idade < 60) {
        faixa = "50-59";
      } else {
        faixa = ">=60";
      }
      // Obter o valor atual e depois incrementar
      candidatosPorIdade.put(faixa, candidatosPorIdade.getOrDefault(faixa, 0) + 1);
    }

    // Formatador de porcentagem
    NumberFormat nf = NumberFormat.getPercentInstance(new Locale("pt", "BR")); // Usar a Locale do Brasil
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);

    System.out.println("      Idade < 30: " + candidatosPorIdade.getOrDefault("<30", 0) + " ("
        + nf.format((double) candidatosPorIdade.getOrDefault("<30", 0) / this.quantidadeEleitos) + ")");
    System.out.println("30 <= Idade < 40: " + candidatosPorIdade.getOrDefault("30-39", 0) + " ("
        + nf.format((double) candidatosPorIdade.getOrDefault("30-39", 0) / this.quantidadeEleitos) + ")");
    System.out.println("40 <= Idade < 50: " + candidatosPorIdade.getOrDefault("40-49", 0) + " ("
        + nf.format((double) candidatosPorIdade.getOrDefault("40-49", 0) / this.quantidadeEleitos) + ")");
    System.out.println("50 <= Idade < 60: " + candidatosPorIdade.getOrDefault("50-59", 0) + " ("
        + nf.format((double) candidatosPorIdade.getOrDefault("50-59", 0) / this.quantidadeEleitos) + ")");
    System.out.println("60 <= Idade     : " + candidatosPorIdade.getOrDefault(">=60", 0) + " ("
        + nf.format((double) candidatosPorIdade.getOrDefault(">=60", 0) / this.quantidadeEleitos) + ")");

    System.out.println();
    System.out.println("Eleitos, por gênero:");
    int qtdMasculino = 0, qtdFeminino = 0;
    for (Candidato c : listaCandidatos) {
      if (!c.getEleito())
        continue;
      if (c.getGenero() == Genero.MASCULINO) {
        qtdMasculino++;
      } else {
        qtdFeminino++;
      }
    }

    System.out
        .println("Feminino:  " + qtdFeminino + " (" + nf.format((double) qtdFeminino / this.quantidadeEleitos)
            + ")");
    System.out
        .println("Masculino: " + qtdMasculino + " (" + nf.format((double) qtdMasculino / this.quantidadeEleitos)
            + ")");

    System.out.println();
    int totalVotosValidos = 0, totalVotosNominais = 0, totalVotosLegenda = 0;
    for (Partido p : listaPartidos) {
      totalVotosValidos += p.getQuantidadeVotos();
      totalVotosNominais += p.getVotosNominais();
      totalVotosLegenda += p.getVotosLegenda();
    }

    NumberFormat brFormat = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
    System.out.println("Total de votos válidos:    " + brFormat.format(totalVotosValidos));
    System.out
        .println("Total de votos nominais:   " + brFormat.format(totalVotosNominais) + " ("
            + nf.format((double) totalVotosNominais / totalVotosValidos)
            + ")");
    System.out
        .println("Total de votos de legenda: " + brFormat.format(totalVotosLegenda) + " ("
            + nf.format((double) totalVotosLegenda / totalVotosValidos)
            + ")");
    System.out.println();
  }

}
