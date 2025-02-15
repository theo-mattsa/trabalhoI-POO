public class Candidato {

  private String nomeUrna;
  private String numeroCandidato;
  private String numeroPartido;
  private String numeroFederacao; // -1 nao participa de nenhuma
  private String dataNascimento;
  private String genero;

  private int quantidadeVotos;

  public String getGenero() {
    return genero;
  }

  public void setGenero(String genero) {
    this.genero = genero;
  }

  public String getNomeUrna() {
    return nomeUrna;
  }

  public void setNomeUrna(String nome) {
    this.nomeUrna = nome;
  }

  public String getNumeroCandidato() {
    return numeroCandidato;
  }

  public void setNumeroCandidato(String numeroCandidatoUrna) {
    this.numeroCandidato = numeroCandidatoUrna;
  }

  public String getNumeroPartido() {
    return numeroPartido;
  }

  public void setNumeroPartido(String numeroPartido) {
    this.numeroPartido = numeroPartido;
  }

  public String getNumeroFederacao() {
    return numeroFederacao;
  }

  public void setNumeroFederacao(String numeroFederacao) {
    this.numeroFederacao = numeroFederacao;
  }

  public String getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(String dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public int getQuantidadeVotos() {
    return quantidadeVotos;
  }

  public void setQuantidadeVotos(int quantidadeVotos) {
    this.quantidadeVotos = quantidadeVotos;
  }

  @Override
  public String toString() {
    return "Candidato{" +
        "nomeUrna='" + nomeUrna + '\'' +
        ", numeroCandidato=" + numeroCandidato +
        ", numeroPartido=" + numeroPartido +
        ", numeroFederacao=" + numeroFederacao +
        ", dataNascimento='" + dataNascimento + '\'' +
        ", genero=" + genero +
        '}';
  }

}
