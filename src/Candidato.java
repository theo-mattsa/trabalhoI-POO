import java.time.LocalDate;

public class Candidato implements Comparable<Candidato> {

  private String nomeUrna;
  private String numeroCandidato;
  private String numeroPartido;
  private String numeroFederacao; // -1 nao participa de nenhuma
  private LocalDate dataNascimento;
  private String genero;
  private Boolean eleito;
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

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public int getQuantidadeVotos() {
    return quantidadeVotos;
  }

  public void incrementaQuantidadeVotos(int quantidadeVotos) {
    this.quantidadeVotos += quantidadeVotos;
  }

  public Boolean getEleito() {
    return eleito;
  }

  public void setEleito(Boolean eleito) {
    this.eleito = eleito;
  }

  @Override
  public int compareTo(Candidato outroCandidato) {
    int comparaVotos = Integer.compare(outroCandidato.quantidadeVotos, this.quantidadeVotos);
    if (comparaVotos != 0)
      return comparaVotos;
    return this.dataNascimento.compareTo(outroCandidato.dataNascimento);
  }

}
