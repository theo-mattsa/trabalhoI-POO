import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Locale;

public class Partido implements Comparable<Partido> {

  private String sigla;
  private String numero;

  private int votosNominais;
  private int votosLegenda;
  private int quantidadeVotos;

  private LinkedList<Candidato> candidatos = new LinkedList<>();

  public void setCandidatos(LinkedList<Candidato> candidatos) {
    this.candidatos = candidatos;
  }

  public LinkedList<Candidato> getCandidatos() {
    return new LinkedList<>(this.candidatos);
  }

  public void insereCandidato(Candidato c) {
    this.candidatos.add(c);
  }

  public Partido(String numero, String sigla) {
    this.numero = numero;
    this.sigla = sigla;
  }

  @Override
  public int compareTo(Partido outroPartido) {
    int comparaVotos = Integer.compare(outroPartido.quantidadeVotos, this.quantidadeVotos);
    if (comparaVotos != 0)
      return comparaVotos;
    return this.numero.compareTo(outroPartido.numero);
  }

  public int getVotosNominais() {
    return votosNominais;
  }

  public int getVotosLegenda() {
    return votosLegenda;
  }

  public int getQuantidadeVotos() {
    return this.quantidadeVotos;
  }

  public int getQuantidadeCandidatosEleitos() {
    int count = 0;
    for (Candidato c : this.candidatos) {
      if (c.getEleito())
        count++;
    }
    return count;
  }

  public static Comparator<Partido> comparaPorCandidatoMaisVotado() {
    return new Comparator<Partido>() {
      @Override
      public int compare(Partido p1, Partido p2) {

        // Encontra a qtd de votos do candidato 1 mais votado
        int votosCandidato1 = 0;
        for (Candidato c : p1.getCandidatos()) {
          if (c.getQuantidadeVotos() > votosCandidato1) {
            votosCandidato1 = c.getQuantidadeVotos();
          }
        }
        // Encontra a qtd de votos do candidato 2 mais votado
        int votosCandidato2 = 0;
        for (Candidato c : p2.getCandidatos()) {
          if (c.getQuantidadeVotos() > votosCandidato2) {
            votosCandidato2 = c.getQuantidadeVotos();
          }
        }
        int comparaVotos = Integer.compare(votosCandidato2, votosCandidato1);
        if (comparaVotos != 0) {
          return comparaVotos;
        }
        return p1.getNumero().compareTo(p2.getNumero());
      }
    };
  }

  public void incrementaVotosNominais(int votos) {
    this.votosNominais += votos;
    this.quantidadeVotos += votos;
  }

  public void incrementaVotosLegenda(int votos) {
    this.votosLegenda += votos;
    this.quantidadeVotos += votos;
  }

  public String getSigla() {
    return this.sigla;
  }

  public void setSigla(String sigla) {
    this.sigla = sigla;
  }

  public String getNumero() {
    return this.numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  @Override
  public String toString() {
    NumberFormat brFormat = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
    String output = "";
    output += this.sigla + " - " + this.numero + ", " + brFormat.format(quantidadeVotos) + " votos ";
    output += "(" + brFormat.format(votosNominais) + " nominais" + " e " + brFormat.format(votosLegenda)
        + " de legenda), ";

    output += this.getQuantidadeCandidatosEleitos();
    if (this.getQuantidadeCandidatosEleitos() <= 1)
      output += " candidato eleito";
    else
      output += " candidatos eleitos";

    return output;
  }

  public String getRelatorioMaisEMenosCandidatoVotado() {
    Collections.sort(this.candidatos);
    Candidato maisVotado = this.candidatos.getFirst();
    Candidato menosVotado = this.candidatos.getLast();
    NumberFormat brFormat = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
    String output = this.sigla + " - " + this.numero + ", ";
    output += maisVotado.getNomeUrna() + " (" + maisVotado.getNumeroCandidato() + ", "
        + brFormat.format(maisVotado.getQuantidadeVotos()) + " votos) / ";
    output += menosVotado.getNomeUrna() + " (" + menosVotado.getNumeroCandidato() + ", "
        + brFormat.format(menosVotado.getQuantidadeVotos()) + " votos)";
    return output;
  }

}
