import java.text.NumberFormat;
import java.util.*;

public class EleicaoRelatorios {
  private Eleicao eleicao;
  private List<Candidato> listaCandidatos;
  private List<Partido> listaPartidos;

  public EleicaoRelatorios(Eleicao eleicao) {
    this.eleicao = eleicao;
    this.listaCandidatos = new ArrayList<>(eleicao.getCandidatos());
    this.listaPartidos = new ArrayList<>(eleicao.getPartidos());

    // Ordenações para os relatórios
    Collections.sort(listaCandidatos);
    Collections.sort(listaPartidos);
  }

  public void imprimeRelatorios() {
    imprimeNumeroVagas();
    imprimeVereadoresEleitos();
    imprimeMaisVotados();
    imprimeEleitosMajoritaria();
    imprimeBeneficiadosSistemaProporcional();
    imprimeVotacaoPartidos();
    imprimeMaisEMenosVotadosPorPartido();
    imprimeEleitosPorFaixaEtaria();
    imprimeEleitosPorGenero();
    imprimeResumoVotacao();
  }

  private void imprimeNumeroVagas() {
    System.out.println("Número de vagas: " + eleicao.getQuantidadeEleitos());
    System.out.println();
  }

  private void imprimeVereadoresEleitos() {
    System.out.println("Vereadores eleitos:");
    int index = 1;
    for (Candidato c : listaCandidatos) {
      if (c.getEleito()) {
        System.out.println(index + " - " + c);
        index++;
      }
    }
    System.out.println();
  }

  private void imprimeMaisVotados() {
    System.out.println("Candidatos mais votados (respeitando número de vagas):");
    for (int i = 0; i < eleicao.getQuantidadeEleitos(); i++) {
      System.out.println((i + 1) + " - " + listaCandidatos.get(i));
    }
    System.out.println();
  }

  private void imprimeEleitosMajoritaria() {
    System.out.println("Teriam sido eleitos se fosse majoritária, mas não foram:");
    System.out.println("(posição no ranking de mais votados)");
    for (int i = 0; i < eleicao.getQuantidadeEleitos(); i++) {
      Candidato c = listaCandidatos.get(i);
      if (!c.getEleito()) {
        System.out.println((i + 1) + " - " + c);
      }
    }
    System.out.println();
  }

  private void imprimeBeneficiadosSistemaProporcional() {
    System.out.println("Eleitos que se beneficiaram do sistema proporcional:");
    System.out.println("(posição no ranking de mais votados)");
    int index = 1;
    for (Candidato c : listaCandidatos) {
      if (c.getEleito() && listaCandidatos.indexOf(c) >= eleicao.getQuantidadeEleitos()) {
        System.out.println(index + " - " + c);
        index++;
      }
    }
    System.out.println();
  }

  private void imprimeVotacaoPartidos() {
    System.out.println("Votação dos partidos e número de candidatos eleitos:");
    int index = 1;
    for (Partido p : listaPartidos) {
      System.out.println(index + " - " + p);
      index++;
    }
    System.out.println();
  }

  private void imprimeMaisEMenosVotadosPorPartido() {
    Collections.sort(listaPartidos, Partido.comparaPorCandidatoMaisVotado());
    System.out.println("Primeiro e último colocados de cada partido:");
    int index = 1;
    for (Partido p : listaPartidos) {
      if (p.getQuantidadeVotos() > 0) {
        System.out.println(index + " - " + p.getRelatorioMaisEMenosCandidatoVotado());
        index++;
      }
    }
    System.out.println();
  }

  private void imprimeEleitosPorFaixaEtaria() {

    String[] faixas = { "<30", "30-39", "40-49", "50-59", ">=60" };

    System.out.println("Eleitos, por faixa etária (na data da eleição):");
    Map<String, Integer> candidatosPorIdade = new HashMap<>();
    for (Candidato c : listaCandidatos) {
      if (!c.getEleito())
        continue;

      String faixa = getFaixaEtaria(c.getIdade(eleicao.getDataEleicao()));
      candidatosPorIdade.put(faixa, candidatosPorIdade.getOrDefault(faixa, 0) + 1);
    }

    NumberFormat nf = NumberFormat.getPercentInstance(new Locale("pt", "BR"));
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);

    for (String faixa : faixas) {
      int qtd = candidatosPorIdade.getOrDefault(faixa, 0);
      System.out.println(faixa + ": " + qtd + " (" + nf.format((double) qtd / eleicao.getQuantidadeEleitos()) + ")");
    }
    System.out.println();
  }

  private void imprimeEleitosPorGenero() {
    System.out.println("Eleitos, por gênero:");
    int qtdMasculino = 0, qtdFeminino = 0;
    for (Candidato candidato : listaCandidatos) {
      if (!candidato.getEleito())
        continue;

      switch (candidato.getGenero()) {
        case MASCULINO:
          qtdMasculino++;
          break;
        case FEMININO:
          qtdFeminino++;
          break;
      }
    }

    NumberFormat nf = NumberFormat.getPercentInstance(new Locale("pt", "BR"));
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);

    System.out.println(
        "Feminino:  " + qtdFeminino + " (" + nf.format((double) qtdFeminino / eleicao.getQuantidadeEleitos()) + ")");
    System.out.println(
        "Masculino: " + qtdMasculino + " (" + nf.format((double) qtdMasculino / eleicao.getQuantidadeEleitos()) + ")");
    System.out.println();
  }

  private void imprimeResumoVotacao() {
    int totalVotosValidos = 0, totalVotosNominais = 0, totalVotosLegenda = 0;
    for (Partido p : listaPartidos) {
      totalVotosValidos += p.getQuantidadeVotos();
      totalVotosNominais += p.getVotosNominais();
      totalVotosLegenda += p.getVotosLegenda();
    }

    NumberFormat brFormat = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
    NumberFormat nf = NumberFormat.getPercentInstance(new Locale("pt", "BR"));
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);

    System.out.println("Total de votos válidos:    " + brFormat.format(totalVotosValidos));
    System.out.println("Total de votos nominais:   " + brFormat.format(totalVotosNominais) + " ("
        + nf.format((double) totalVotosNominais / totalVotosValidos) + ")");
    System.out.println("Total de votos de legenda: " + brFormat.format(totalVotosLegenda) + " ("
        + nf.format((double) totalVotosLegenda / totalVotosValidos) + ")");
    System.out.println();
  }

  private String getFaixaEtaria(int idade) {
    if (idade < 30)
      return "<30";
    if (idade < 40)
      return "30-39";
    if (idade < 50)
      return "40-49";
    if (idade < 60)
      return "50-59";
    return ">=60";
  }
}
