import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EleicaoRelatorio {

  private Eleicao eleicao;
  private List<Candidato> listaCandidatos;
  private List<Partido> listaPartidos;

  public EleicaoRelatorio(Eleicao eleicao) {
    this.eleicao = eleicao;
    listaCandidatos = new ArrayList<>(eleicao.getCandidatos().values());
    listaPartidos = new LinkedList<>(eleicao.getPartidos().values());
    // Realiza ordenacao
    Collections.sort(listaCandidatos, new Candidato.CandidatoComparator(false));
    Collections.sort(listaPartidos);
  }

  public void imprimeTodosRelatorios() {
    imprimeNumeroVagas();
    imprimeCandidatosEleitos();
    imprimeCandidatosMaisVotados();
    imprimeCandidatosEleitosCasoMajoritario();
    imprimeCandidatosBeneficiadosProporcional();
    imprimeVotacaoPartidos();
    imprimePrimeiroUltimoPartido();
    eleitosFaixaEtaria();
    imprimeRelatorioGenero();
    imprimeRelatorioGeral();
  }

  public void imprimeNumeroVagas() {
    System.out.println("Número de vagas: " + this.eleicao.getQuantidadeEleitos());
    System.out.println();
    System.out.println("Vereadores eleitos:");
  }

  public void imprimeCandidatosEleitos() {
    int index = 1;
    for (Candidato c : listaCandidatos) {
      if (c.getEleito()) {
        String output = index + " - ";
        System.out.print(output);
        System.out.println(c);
        index++;
      }
    }
  }

  public void imprimeCandidatosMaisVotados() {
    System.out.println();
    System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
    for (int i = 0; i < this.eleicao.getQuantidadeEleitos(); i++) {
      Candidato c = listaCandidatos.get(i);
      String output = (i + 1) + " - ";
      System.out.println(output + c);
    }
  }

  public void imprimeCandidatosEleitosCasoMajoritario() {
    System.out.println();
    System.out.println("Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:");
    System.out.println("(com sua posição no ranking de mais votados)");
    for (int i = 0; i < this.eleicao.getQuantidadeEleitos(); i++) {
      Candidato c = listaCandidatos.get(i);
      if (!c.getEleito()) {
        String output = (i + 1) + " - ";
        System.out.println(output + c);
      }
    }
  }

  public void imprimeCandidatosBeneficiadosProporcional() {
    System.out.println();
    System.out.println("Eleitos, que se beneficiaram do sistema proporcional:");
    System.out.println("(com sua posição no ranking de mais votados)");
    int index = 1;
    for (Candidato c : listaCandidatos) {
      if (c.getEleito() && listaCandidatos.indexOf(c) >= this.eleicao.getQuantidadeEleitos()) {
        String output = index + " - ";
        System.out.println(output + c);
      }
      index++;
    }
  }

  public void imprimeVotacaoPartidos() {
    System.out.println();
    System.out.println("Votação dos partidos e número de candidatos eleitos:");
    int index = 1;
    for (Partido p : listaPartidos) {
      String output = index + " - ";
      System.out.println(output + p);
      index++;
    }
  }

  public void imprimePrimeiroUltimoPartido() {
    Collections.sort(listaPartidos, Partido.comparaPorCandidatoMaisVotado());
    System.out.println();
    System.out.println("Primeiro e último colocados de cada partido:");
    int index = 1;
    for (Partido p : listaPartidos) {
      // Se for igual 1 no caso: ALEMAO VOTO 0 2x
      if (p.getCandidatos().isEmpty() || p.getCandidatos().size() == 1) // Caso o partido nao tenha candidatos
        continue;
      String output = index + " - ";
      System.out.println(output + p.getRelatorioMaisEMenosCandidatoVotado());
      index++;
    }
  }

  public void eleitosFaixaEtaria() {

    System.out.println();
    System.out.println("Eleitos, por faixa etária (na data da eleição):");
    Map<String, Integer> candidatosPorIdade = new HashMap<>();

    for (Candidato c : listaCandidatos) {

      if (!c.getEleito())
        continue;

      String faixa = "";
      int idade = c.getIdade(this.eleicao.getDataEleicao());
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
        + nf.format((double) candidatosPorIdade.getOrDefault("<30", 0) / this.eleicao.getQuantidadeEleitos()) + ")");
    System.out.println("30 <= Idade < 40: " + candidatosPorIdade.getOrDefault("30-39", 0) + " ("
        + nf.format((double) candidatosPorIdade.getOrDefault("30-39", 0) / this.eleicao.getQuantidadeEleitos()) + ")");
    System.out.println("40 <= Idade < 50: " + candidatosPorIdade.getOrDefault("40-49", 0) + " ("
        + nf.format((double) candidatosPorIdade.getOrDefault("40-49", 0) / this.eleicao.getQuantidadeEleitos()) + ")");
    System.out.println("50 <= Idade < 60: " + candidatosPorIdade.getOrDefault("50-59", 0) + " ("
        + nf.format((double) candidatosPorIdade.getOrDefault("50-59", 0) / this.eleicao.getQuantidadeEleitos()) + ")");
    System.out.println("60 <= Idade     : " + candidatosPorIdade.getOrDefault(">=60", 0) + " ("
        + nf.format((double) candidatosPorIdade.getOrDefault(">=60", 0) / this.eleicao.getQuantidadeEleitos()) + ")");

    System.out.println();
  }

  public void imprimeRelatorioGenero() {

    NumberFormat nf = NumberFormat.getPercentInstance(new Locale("pt", "BR")); // Usar a Locale do Brasil
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);

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
        .println(
            "Feminino:  " + qtdFeminino + " (" + nf.format((double) qtdFeminino / this.eleicao.getQuantidadeEleitos())
                + ")");
    System.out
        .println(
            "Masculino: " + qtdMasculino + " (" + nf.format((double) qtdMasculino / this.eleicao.getQuantidadeEleitos())
                + ")");

    System.out.println();
  }

  public void imprimeRelatorioGeral() {
    int totalVotosValidos = 0, totalVotosNominais = 0, totalVotosLegenda = 0;
    NumberFormat nf = NumberFormat.getPercentInstance(new Locale("pt", "BR")); // Usar a Locale do Brasil
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);

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
