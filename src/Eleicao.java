import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Eleicao {

  private String pathArquivoCandidatos;
  private String pathArquivoVotacao;

  // Data da eleicao
  private LocalDate dataEleicao;

  private int codigoCidade;
  private int quantidadeEleitos;

  // Partidos (numero partido -> Partido)
  private Map<String, Partido> partidos = new HashMap<>();

  // Candidatos (numero candidato -> Candidato)
  private Map<String, Candidato> candidatos = new HashMap<>();

  public Map<String, Candidato> getCandidatos() {
    return new HashMap<>(candidatos);
  }

  public Map<String, Partido> getPartidos() {
    return new HashMap<>(partidos);
  }

  public int getQuantidadeEleitos() {
    return quantidadeEleitos;
  }

  public LocalDate getDataEleicao() {
    return dataEleicao;
  }

  // Formatter de data
  private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public Eleicao(String pathArquivoCandidatos, String pathArquivoVotacao, int codigoCidade, String dataEleicao) {
    this.pathArquivoCandidatos = pathArquivoCandidatos;
    this.pathArquivoVotacao = pathArquivoVotacao;
    this.codigoCidade = codigoCidade;
    this.dataEleicao = LocalDate.parse(dataEleicao, formatterDate);
  }

  public void processaCandidatos() {

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

  public void processaVotacao() {
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
}
