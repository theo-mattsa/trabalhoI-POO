import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;

public class Candidato {

  private String nomeUrna;
  private String numeroCandidato;
  private Boolean participaFederacao;
  private LocalDate dataNascimento;
  private Genero genero;
  private Boolean eleito;
  private int quantidadeVotos;

  private Partido partido;

  public Genero getGenero() {
    return genero;
  }

  public void setGenero(Genero genero) {
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

  public int getIdade(LocalDate referencia) {
    int idade = referencia.getYear() - dataNascimento.getYear();

    // Caso o aniversario ainda nao ocorreu
    if (referencia.getMonthValue() < dataNascimento.getMonthValue() ||
        (referencia.getMonthValue() == dataNascimento.getMonthValue()
            && referencia.getDayOfMonth() < dataNascimento.getDayOfMonth())) {
      idade--;
    }
    return idade;
  }

  public void setEleito(Boolean eleito) {
    this.eleito = eleito;
  }

  /**
   * @Override
   *           public int compareTo(Candidato outroCandidato) {
   *           int comparaVotos = Integer.compare(outroCandidato.quantidadeVotos,
   *           this.quantidadeVotos);
   *           if (comparaVotos != 0)
   *           return comparaVotos;
   *           return
   *           this.dataNascimento.compareTo(outroCandidato.dataNascimento);
   *           }
   */

  public static class CandidatoComparator implements Comparator<Candidato> {

    private final boolean compararNumeroPartido;

    public CandidatoComparator(boolean compararNumeroPartido) {
      this.compararNumeroPartido = compararNumeroPartido;
    }

    @Override
    public int compare(Candidato c1, Candidato c2) {
      // Comparar votos de forma decrescente
      int comparaVotos = Integer.compare(c2.getQuantidadeVotos(), c1.getQuantidadeVotos());
      if (comparaVotos != 0) {
        return comparaVotos;
      }

      // Caso de empate no número de votos, comparar pelo número partidário (se
      // habilitado)
      if (compararNumeroPartido) {
        int comparaNumeroPartidario = c1.getPartido().getNumero().compareTo(c2.getPartido().getNumero());
        if (comparaNumeroPartidario != 0) {
          return comparaNumeroPartidario;
        }
      }

      // Caso de empate em votos (e número partidário se considerado), comparar pela
      // data de nascimento
      return c1.getDataNascimento().compareTo(c2.getDataNascimento());
    }
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
