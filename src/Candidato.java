import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class Candidato implements Comparable<Candidato> {

  private String nomeUrna;
  private String numeroCandidato;
  private Boolean participaFederacao;
  private LocalDate dataNascimento;
  private String genero;
  private Boolean eleito;
  private int quantidadeVotos;

  private Partido partido;

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

  public Boolean getParticipaFederacao() {
    return this.participaFederacao;
  }

  public void setParticipaFederacao(Boolean participaFederacao) {
    this.participaFederacao = participaFederacao;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public Partido getPartido() {
    return partido;
  }

  public void setPartido(Partido partido) {
    this.partido = partido;
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

  @Override
  public String toString() {
    NumberFormat brFormat = NumberFormat.getInstance(Locale.forLanguageTag("pt-BR"));
    String output = "";
    if (this.partido != null) {
      if (this.participaFederacao)
        output += "*";
      output += this.nomeUrna;
      output += " (" + this.partido.getSigla() + ", " + brFormat.format(this.quantidadeVotos) + " votos)";
    }

    return output;
  }

}
