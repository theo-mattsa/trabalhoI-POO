import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Eleicao {

  private LocalDate dataEleicao;
  private int codigoCidade;
  private int quantidadeEleitos;
  private Map<String, Partido> partidos = new HashMap<>(); // numero partido -> Partido
  private Map<String, Candidato> candidatos = new HashMap<>(); // numero candidato -> Candidato
  private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public Eleicao(int codigoCidade, String dataEleicao) {
    this.codigoCidade = codigoCidade;
    this.dataEleicao = LocalDate.parse(dataEleicao, formatterDate);
  }

  public void processaVotacao(List<String[]> linhas, Map<String, Integer> indiceCabecalho) {
    for (String[] valores : linhas) {
      try {
        int codigoCidadeArquivo = Integer.parseInt(valores[indiceCabecalho.get("CD_MUNICIPIO")]);
        int codigoCargo = Integer.parseInt(valores[indiceCabecalho.get("CD_CARGO")]);

        if (codigoCidadeArquivo != this.codigoCidade || codigoCargo != 13)
          continue;

        int quantidadeVotos = Integer.parseInt(valores[indiceCabecalho.get("QT_VOTOS")]);
        int numeroCandidato = Integer.parseInt(valores[indiceCabecalho.get("NR_VOTAVEL")]);

        if (numeroCandidato >= 10000 && numeroCandidato <= 99999) {
          Candidato candidato = candidatos.get(String.valueOf(numeroCandidato));
          if (candidato != null) {
            candidato.incrementaQuantidadeVotos(quantidadeVotos);
            Partido partido = candidato.getPartido();
            if (partido != null) {
              partido.incrementaVotosNominais(quantidadeVotos);
            }
          }
        } else if (numeroCandidato >= 10 && numeroCandidato <= 99
            && !(numeroCandidato >= 95 && numeroCandidato <= 98)) {
          String partidoKey = String.valueOf(numeroCandidato);
          if (partidos.containsKey(partidoKey)) {
            Partido partido = partidos.get(partidoKey);
            partido.incrementaVotosLegenda(quantidadeVotos);
          }
        }
      } catch (Exception e) {
        System.out.println("Erro ao processar linha de votação: " + Arrays.toString(valores));
        e.printStackTrace();
      }
    }
  }

  public void processaCandidatosPartidos(List<String[]> linhas, Map<String, Integer> indiceCabecalho) {
    for (String[] valores : linhas) {
      try {
        if (!ehCandidatoValido(valores, indiceCabecalho))
          continue;

        Partido partido = getOrCreatePartido(valores, indiceCabecalho);
        Candidato candidato = criaCandidato(valores, partido, indiceCabecalho);

        partido.insereCandidato(candidato);
        this.candidatos.put(candidato.getNumeroCandidato(), candidato);

        if (candidato.getEleito()) {
          this.quantidadeEleitos++;
        }
      } catch (Exception e) {
        System.out.println("Erro ao processar linha de candidato: " + Arrays.toString(valores));
        e.printStackTrace();
      }
    }
  }

  private boolean ehCandidatoValido(String[] valores, Map<String, Integer> indiceCabecalho) {
    int codigoCidadeArquivo = Integer.parseInt(valores[indiceCabecalho.get("SG_UE")]);
    int codigoCargo = Integer.parseInt(valores[indiceCabecalho.get("CD_CARGO")]);
    return codigoCidadeArquivo == this.codigoCidade && codigoCargo == 13;
  }

  private Partido getOrCreatePartido(String[] valores, Map<String, Integer> indiceCabecalho) {
    String numeroPartido = valores[indiceCabecalho.get("NR_PARTIDO")];
    String siglaPartido = valores[indiceCabecalho.get("SG_PARTIDO")];

    if (!partidos.containsKey(numeroPartido)) {
      partidos.put(numeroPartido, new Partido(numeroPartido, siglaPartido));
    }
    return partidos.get(numeroPartido);
  }

  private Candidato criaCandidato(String[] valores, Partido partido, Map<String, Integer> indiceCabecalho) {
    String numeroCandidato = valores[indiceCabecalho.get("NR_CANDIDATO")];
    String nomeCandidatoUrna = valores[indiceCabecalho.get("NM_URNA_CANDIDATO")];
    String dataNascimento = valores[indiceCabecalho.get("DT_NASCIMENTO")];

    int numFederacao = Integer.parseInt(valores[indiceCabecalho.get("NR_FEDERACAO")]);
    int genero = Integer.parseInt(valores[indiceCabecalho.get("CD_GENERO")]);
    int codigoCandidatoEleito = Integer.parseInt(valores[indiceCabecalho.get("CD_SIT_TOT_TURNO")]);

    boolean candidatoEleito = codigoCandidatoEleito == 2 || codigoCandidatoEleito == 3;

    Candidato candidato = new Candidato();
    candidato.setNumeroCandidato(numeroCandidato);
    candidato.setNomeUrna(nomeCandidatoUrna);
    candidato.setPartido(partido);
    candidato.setParticipaFederacao(numFederacao != -1);
    candidato.setDataNascimento(LocalDate.parse(dataNascimento, formatterDate));
    candidato.setEleito(candidatoEleito);

    if (genero == 2) {
      candidato.setGenero(Genero.MASCULINO);
    } else if (genero == 4) {
      candidato.setGenero(Genero.FEMININO);
    }

    return candidato;
  }

  public Collection<Candidato> getCandidatos() {
    return candidatos.values();
  }

  public Collection<Partido> getPartidos() {
    return partidos.values();
  }

  public int getQuantidadeEleitos() {
    return quantidadeEleitos;
  }

  public LocalDate getDataEleicao() {
    return dataEleicao;
  }
}
