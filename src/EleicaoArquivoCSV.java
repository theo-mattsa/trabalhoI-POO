import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EleicaoArquivoCSV {

  private String pathArquivo;
  private String padraoCodificacao;
  private Map<String, Integer> indiceCabecalho;
  private List<String[]> linhas;

  public EleicaoArquivoCSV(String pathArquivo, String padraoCodificacao) {
    this.pathArquivo = pathArquivo;
    this.padraoCodificacao = padraoCodificacao;
    this.linhas = new ArrayList<>();
    this.indiceCabecalho = new HashMap<>();
  }

  public void leArquivo() {
    try {
      FileInputStream fin = new FileInputStream(this.pathArquivo);
      InputStreamReader r = new InputStreamReader(fin, this.padraoCodificacao);
      BufferedReader br = new BufferedReader(r);
      String linha = br.readLine();
      // Remove aspas e divide por ";"
      String[] cabecalhos = linha.replace("\"", "").split(";");
      for (int i = 0; i < cabecalhos.length; i++) {
        this.indiceCabecalho.put(cabecalhos[i], i);
      }
      // Lê os dados restantes do CSV e armazena os dados
      while ((linha = br.readLine()) != null) {
        String[] valores = linha.replace("\"", "").split(";");
        linhas.add(valores);
      }
      br.close();
    } catch (IOException e) {
      System.out.println("Erro de I/O");
      e.printStackTrace();
    }
  }

  public List<String[]> getLinhas() {
    return this.linhas;
  }

  public Map<String, Integer> getIndiceCabecalho() {
    return this.indiceCabecalho;
  }

}
