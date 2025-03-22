#include "eleicao-relatorio.hpp"

#include <iomanip>

EleicaoRelatorio::EleicaoRelatorio(Eleicao& eleicao) : eleicao(eleicao) {
  // Cria um vetor de partidos a partir do map de partidos
  for (auto& par : eleicao.getPartidos())
    this->listaPartidos.push_back(*par.second);
  for (auto& par : eleicao.getCandidatos())
    this->listaCandidatos.push_back(*par.second);

  // Faz o sort de candidatos
  sort(this->listaCandidatos.begin(), this->listaCandidatos.end(), [](const Candidato& c1, const Candidato& c2) {
    return c1.compare(c2, false);
  });

  // Faz o sort de partidos
  sort(this->listaPartidos.begin(), this->listaPartidos.end(), [](const Partido& p1, const Partido& p2) {
    return p1.compare(p2);
  });
}

static void imprimeCandidato(Candidato& c) {
  string output = "";
  if (c.getPartido() != nullptr) {
    string qtdVotos = to_string(c.getQuantidadeVotos());
    if (c.getParticipaFederacao())
      output += "*";
    output += c.getNomeUrna();
    output += " (" + c.getPartido()->getSigla() + ", " + qtdVotos + " votos)";
  }
  cout << output << endl;
}

static void imprimePartido(Partido& p) {
  string output = "";

  string palavraVoto = " votos ";
  string palavraNominal = " nominais";

  if (p.getQtdVotos() == 0)
    palavraVoto = " voto ";
  if (p.getVotosNominais() == 0)
    palavraNominal = " nominal";

  string qtdVotos = to_string(p.getQtdVotos());
  string qtdVotosNominais = to_string(p.getVotosNominais());
  string qtdVotosLegenda = to_string(p.getVotosLegenda());
  string qtdCandidatosEleitos = to_string(p.getQtdCandidatosEleitos());

  output += p.getSigla() + " - " + p.getNumero() + ", " + qtdVotos + palavraVoto;
  output += "(" + qtdVotosNominais + palavraNominal + " e " + qtdVotosLegenda + " de legenda), ";

  output += qtdCandidatosEleitos;
  if (p.getQtdCandidatosEleitos() <= 1)
    output += " candidato eleito";
  else
    output += " candidatos eleitos";

  cout << output << endl;
}

void EleicaoRelatorio::imprimeNumeroVagas() {
  cout << "Número de vagas: " << eleicao.getQuantidadeEleitos() << endl;
  cout << endl;
  cout << "Vereadores eleitos:" << endl;
}

void EleicaoRelatorio::imprimeCandidatosEleitos() {
  int index = 1;
  for (auto& candidato : this->listaCandidatos) {
    if (candidato.getEleito()) {
      cout << index << " - ";
      imprimeCandidato(candidato);
      index++;
    }
  }
}

void EleicaoRelatorio::imprimeCandidatosMaisVotados() {
  cout << endl;
  cout << "Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):" << endl;
  for (int i = 0; i < eleicao.getQuantidadeEleitos(); i++) {
    cout << i + 1 << " - ";
    imprimeCandidato(this->listaCandidatos[i]);
  }
}

void EleicaoRelatorio::imprimeCandidatosEleitosCasoMajoritario() {
  cout << endl;
  cout << "Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:" << endl;
  cout << "(com sua posição no ranking de mais votados)" << endl;
  for (int i = 0; i < eleicao.getQuantidadeEleitos(); i++) {
    if (!this->listaCandidatos[i].getEleito()) {
      cout << i + 1 << " - ";
      imprimeCandidato(this->listaCandidatos[i]);
    }
  }
}

void EleicaoRelatorio::imprimeCandidatosEleitosCasoProporcional() {
  cout << endl;
  cout << "Eleitos, que se beneficiaram do sistema proporcional:" << endl;
  cout << "(com sua posição no ranking de mais votados)" << endl;
  int index = 1;
  for (size_t i = 0; i < listaCandidatos.size(); ++i) {
    Candidato& c = listaCandidatos[i];
    // Verifica se o candidato foi eleito e se está além da quantidade de eleitos
    if (c.getEleito() && index >= eleicao.getQuantidadeEleitos()) {
      cout << index << " - ";
      imprimeCandidato(c);
    }
    index++;
  }
}

void EleicaoRelatorio::imprimeVotacaoPartidos() {
  cout << endl;
  cout << "Votação dos partidos e número de candidatos eleitos:" << endl;
  int index = 1;
  for (auto& partido : this->listaPartidos) {
    cout << index << " - ";
    imprimePartido(partido);
    index++;
  }
}

void EleicaoRelatorio::imprimePrimeiroUltimoPartido() {
  // Realiza re-ordernacao para imprimir o primeiro e o ultimo partido
  sort(this->listaPartidos.begin(), this->listaPartidos.end(), [](const Partido& p1, const Partido& p2) {
    return p1.comparaPartidosPorCandidatosMaisVotados(p2);
  });

  cout << endl;
  cout << "Primeiro e último colocados de cada partido:" << endl;
  int index = 1;
  for (auto& partido : this->listaPartidos) {
    if (partido.getCandidatos().size() <= 1)
      continue;
    cout << index << " - ";

    // Faz uma copia dos candidatos do partido
    vector<Candidato> candidatosCopy = {};
    for (auto& candidato : partido.getCandidatos())
      candidatosCopy.push_back(*candidato);

    // Ordena os candidatos do partido
    sort(candidatosCopy.begin(), candidatosCopy.end(), [](const Candidato& c1, const Candidato& c2) {
      return c1.compare(c2, true);
    });

    Candidato maisVotado = candidatosCopy[0];
    Candidato menosVotado = candidatosCopy[candidatosCopy.size() - 1];

    string palavraVoto1 = maisVotado.getQuantidadeVotos() <= 1 ? "voto" : "votos";
    string palavraVoto2 = menosVotado.getQuantidadeVotos() <= 1 ? "voto" : "votos";

    string output = "";
    output += partido.getSigla() + " - " + partido.getNumero() + ", ";
    output += maisVotado.getNomeUrna();
    output += " (" + maisVotado.getNumeroCandidato() + ", " + to_string(maisVotado.getQuantidadeVotos()) + " " + palavraVoto1 + ")";
    output += " / ";
    output += menosVotado.getNomeUrna();
    output += " (" + maisVotado.getNumeroCandidato() + ", " + to_string(menosVotado.getQuantidadeVotos()) + " " + palavraVoto2 + ")";

    cout << output << endl;
    index++;
  }
}
