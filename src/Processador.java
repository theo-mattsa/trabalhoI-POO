import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Processador {

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

  public Processador(String pathArquivoCandidatos, String pathArquivoVotacao, int codigoCidade, String dataEleicao) {
    this.pathArquivoCandidatos = pathArquivoCandidatos;
    this.pathArquivoVotacao = pathArquivoVotacao;
    this.codigoCidade = codigoCidade;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    this.dataEleicao = LocalDate.parse(dataEleicao, formatter);
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

        int codigoCidadeArquivo = Integer.parseInt(valores[indiceCabecalho.get("SG_UE")]);

        // Se o codigo lido é diferente da cidade vai pra proxima iteracao
        if (codigoCidadeArquivo != this.codigoCidade)
          continue;

        // Se não for um vereador vai pro proximo
        int codigoCargo = Integer.parseInt(valores[indiceCabecalho.get("CD_CARGO")]);
        if (codigoCargo != 13)
          continue;

        Boolean candidatoEleito = false;
        int codigoCandidatoEleito = Integer.parseInt(valores[indiceCabecalho.get("CD_SIT_TOT_TURNO")]);
        if (codigoCandidatoEleito == -1) { // Candidatura inválida
          continue;
        } else if (codigoCandidatoEleito == 2 || codigoCandidatoEleito == 3) { // Candidato eleito
          candidatoEleito = true;
          this.quantidadeEleitos++;
        }

        String numeroCandidato = valores[indiceCabecalho.get("NR_CANDIDATO")];
        String nomeCandidatoUrna = valores[indiceCabecalho.get("NM_URNA_CANDIDATO")];
        String numeroPartido = valores[indiceCabecalho.get("NR_PARTIDO")];
        String siglaPartido = valores[indiceCabecalho.get("SG_PARTIDO")];
        String numFederacao = valores[indiceCabecalho.get("NR_FEDERACAO")];
        String dataNascimento = valores[indiceCabecalho.get("DT_NASCIMENTO")];
        int genero = Integer.parseInt(valores[indiceCabecalho.get("CD_GENERO")]);

        // Cria partido se ainda nao foi criado
        Partido p;
        if (this.partidos.containsKey(numeroPartido)) {
          p = partidos.get(numeroPartido);
        } else {
          p = new Partido(numeroPartido, siglaPartido);
          partidos.put(numeroPartido, p);
        }

        if (candidatoEleito) {
          p.incrementaQuantidadeCandidatosEleitos();
        }

        Candidato candidato = new Candidato();

        candidato.setEleito(candidatoEleito);
        candidato.setNumeroCandidato(numeroCandidato);
        candidato.setNomeUrna(nomeCandidatoUrna);
        candidato.setNumeroPartido(numeroPartido);
        candidato.setNumeroFederacao(numFederacao);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        candidato.setDataNascimento(LocalDate.parse(dataNascimento, formatter));

        if (genero == 2) {
          candidato.setGenero("MASCULINO");
        } else if (genero == 4) {
          candidato.setGenero("FEMININO");
        }

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

        // Se o codigo lido é diferente da cidade vai pra proxima iteracao
        int codigoCidadeArquivo = Integer.parseInt(valores[indiceCabecalho.get("CD_MUNICIPIO")]);
        if (codigoCidadeArquivo != this.codigoCidade)
          continue;

        // Se não for um vereador vai pra proxima iteracao
        int codigoCargo = Integer.parseInt(valores[indiceCabecalho.get("CD_CARGO")]);
        if (codigoCargo != 13)
          continue;

        // Quantidade de votos (candidato ou partido)
        int quantidadeVotos = Integer.parseInt(valores[indiceCabecalho.get("QT_VOTOS")]);
        int numeroCandidato = Integer.parseInt(valores[indiceCabecalho.get("NR_VOTAVEL")]);

        Boolean numero5digitos = numeroCandidato >= 10000 && numeroCandidato <= 99999;
        Boolean numero2digitos = numeroCandidato >= 10 && numeroCandidato <= 99;

        if (numero5digitos) {
          Candidato c = this.candidatos.get(String.valueOf(numeroCandidato));
          c.incrementaQuantidadeVotos(quantidadeVotos);
          Partido p = partidos.get(c.getNumeroPartido());
          p.incrementaVotosNominais(quantidadeVotos);
        } else if (numero2digitos && !(numeroCandidato >= 95 && numeroCandidato <= 98)) {
          partidos.get(String.valueOf(numeroCandidato)).incrementaVotosLegenda(quantidadeVotos);
        }

      }

      br.close();
    } catch (IOException e) {
      System.out.println("Erro de I/O");
      e.printStackTrace();
    }
  }

  public void imprimeVereadoresEleitos() {

    LinkedList<Candidato> listaCandidatos = new LinkedList<>(this.candidatos.values());
    LinkedList<Partido> listaPartidos = new LinkedList<>(this.partidos.values());

    // Ordena por quantiade de votos e idade (em caso de empate de votos)
    Collections.sort(listaCandidatos);

    // Ordena por quantidade de votos (nominais e legenda) e numero (caso empate)
    Collections.sort(listaPartidos);

    for (Partido p : listaPartidos) {
      System.out.println(p);
    }

  }

}
