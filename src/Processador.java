import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Processador {

  private String pathArquivoCandidatos;
  private String pathArquivoVotacao;
  private int codigoCidade;

  private LinkedList<Candidato> candidatos;

  public Processador(String pathArquivoCandidatos, String pathArquivoVotacao, int codigoCidade) {
    this.pathArquivoCandidatos = pathArquivoCandidatos;
    this.pathArquivoVotacao = pathArquivoVotacao;
    this.codigoCidade = codigoCidade;
    this.candidatos = new LinkedList<>();
  }

  public void processaArquivoCandidato() {

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

        int codigoCargo = Integer.parseInt(valores[indiceCabecalho.get("CD_CARGO")]);
        // Se não for um vereador vai pro proximo
        if (codigoCargo != 13)
          continue;

        String codigoCandidatoEleito = valores[indiceCabecalho.get("CD_SIT_TOT_TURNO")];

        // Candidatura inválida (2 ou 3 para candidato eleito)
        if (codigoCandidatoEleito == "-1")
          continue;

        String numeroCandidato = valores[indiceCabecalho.get("NR_CANDIDATO")];
        String nomeCandidatoUrna = valores[indiceCabecalho.get("NM_URNA_CANDIDATO")];
        String numeroPartido = valores[indiceCabecalho.get("NR_PARTIDO")];
        String siglaPartido = valores[indiceCabecalho.get("SG_PARTIDO")];
        String numFederacao = valores[indiceCabecalho.get("NR_FEDERACAO")];
        String dataNascimento = valores[indiceCabecalho.get("DT_NASCIMENTO")];

        int genero = Integer.parseInt(valores[indiceCabecalho.get("CD_GENERO")]);

        Candidato candidato = new Candidato();

        candidato.setNumeroCandidato(numeroCandidato);
        candidato.setNomeUrna(nomeCandidatoUrna);
        candidato.setNumeroPartido(numeroPartido);
        candidato.setNumeroFederacao(numFederacao);
        candidato.setDataNascimento(dataNascimento);

        if (genero == 2) {
          candidato.setGenero("MASCULINO");
        } else if (genero == 4) {
          candidato.setGenero("FEMININO");
        }

        System.out.println(candidato);
        this.candidatos.add(candidato);
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

        // Se o codigo lido é diferente da cidade vai pra proxima iteracao
        if (codigoCidadeArquivo != this.codigoCidade)
          continue;

        int codigoCargo = Integer.parseInt(valores[indiceCabecalho.get("CD_CARGO")]);

        // Se não for um vereador vai pra proxima iteracao
        if (codigoCargo != 13)
          continue;

        int quantidadeVotos = Integer.parseInt(valores[indiceCabecalho.get("QT_VOTOS")]);
        int numeroCandidato = Integer.parseInt(valores[indiceCabecalho.get("NR_CANDIDATO")]);

        // TO-DO: Realizar tratamento de acordo com o numero do candidato

      }

      br.close();
    } catch (IOException e) {
      System.out.println("Erro de I/O");
      e.printStackTrace();
    }
  }

}
