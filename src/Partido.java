public class Partido implements Comparable<Partido> {

  private String sigla;
  private String numero;

  private int votosNominais;
  private int votosLegenda;
  private int quantidadeVotos;
  private int quantidadeCandidatosEleitos;

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
    return quantidadeCandidatosEleitos;
  }

  public void incrementaQuantidadeCandidatosEleitos() {
    this.quantidadeCandidatosEleitos++;
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
    return "Partido: " + this.sigla + " Votos: " + this.quantidadeVotos + "Quantidade eleitos: "
        + this.quantidadeCandidatosEleitos;
  }

}
