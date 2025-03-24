#include "eleicao-relatorio.hpp"

#include <iomanip>
#include <locale>
#include <sstream>

EleicaoRelatorio::EleicaoRelatorio(Eleicao& eleicao) : eleicao(eleicao) {
  // Cria um vetor de partidos e candidatos a partir do map de partidos
  for (auto& par : eleicao.getPartidos())
    this->listaPartidos.push_back(par.second);
  for (auto& par : eleicao.getCandidatos())
    this->listaCandidatos.push_back(par.second);

  // Faz sort de candidatos e partidos das novas listas
  sort(this->listaCandidatos.begin(), this->listaCandidatos.end(), [](Candidato* c1, Candidato* c2) {
    return c1->compare(*c2, false);
  });

  sort(this->listaPartidos.begin(), this->listaPartidos.end(), [](Partido* p1, Partido* p2) {
    return p1->compare(*p2);
  });
}

static void imprimeCandidato(Candidato& c, locale loc) {
  if (c.getPartido() != nullptr) {
    ostringstream oss;
    oss.imbue(loc);
    oss << fixed << c.getQuantidadeVotos();
    string qtdVotos = oss.str();
    string output = "";
    if (c.getParticipaFederacao())
      output += "*";
    output += c.getNomeUrna();
    output += " (" + c.getPartido()->getSigla() + ", " + qtdVotos + " votos)";
    cout << output << endl;
  }
}

static void imprimePartido(Partido& p, locale loc) {
  ostringstream ossTotal, ossNominais, ossLegenda, ossEleitos;
  ossTotal.imbue(loc);
  ossNominais.imbue(loc);
  ossLegenda.imbue(loc);
  ossEleitos.imbue(loc);

  ossTotal << fixed << p.getQtdVotos();
  ossNominais << fixed << p.getVotosNominais();
  ossLegenda << fixed << p.getVotosLegenda();
  ossEleitos << fixed << p.getQtdCandidatosEleitos();

  string palavraVoto = (p.getQtdVotos() <= 1) ? " voto " : " votos ";
  string palavraNominal = (p.getVotosNominais() <= 1) ? " nominal" : " nominais";
  string palavraEleitos = (p.getQtdCandidatosEleitos() <= 1) ? " candidato eleito" : " candidatos eleitos";

  string output = p.getSigla() + " - " + p.getNumero() + ", " +
                  ossTotal.str() + palavraVoto + "(" +
                  ossNominais.str() + palavraNominal + " e " +
                  ossLegenda.str() + " de legenda), " +
                  ossEleitos.str() + palavraEleitos;

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
    if (candidato->getEleito()) {
      cout << index << " - ";
      imprimeCandidato(*candidato, this->loc);
      index++;
    }
  }
}

void EleicaoRelatorio::imprimeCandidatosMaisVotados() {
  cout << endl;
  cout << "Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):" << endl;
  for (int i = 0; i < eleicao.getQuantidadeEleitos(); i++) {
    cout << i + 1 << " - ";
    imprimeCandidato(*(this->listaCandidatos[i]), this->loc);
  }
}

void EleicaoRelatorio::imprimeCandidatosEleitosCasoMajoritario() {
  cout << endl;
  cout << "Teriam sido eleitos se a votação fosse majoritária, e não foram eleitos:" << endl;
  cout << "(com sua posição no ranking de mais votados)" << endl;
  for (int i = 0; i < eleicao.getQuantidadeEleitos(); i++) {
    if (!this->listaCandidatos[i]->getEleito()) {
      cout << i + 1 << " - ";
      imprimeCandidato(*(this->listaCandidatos[i]), this->loc);
    }
  }
}

void EleicaoRelatorio::imprimeCandidatosEleitosCasoProporcional() {
  cout << endl;
  cout << "Eleitos, que se beneficiaram do sistema proporcional:" << endl;
  cout << "(com sua posição no ranking de mais votados)" << endl;

  // Candidatos que foram eleitos e não estariam se fosse majoritário
  for (size_t i = 0; i < listaCandidatos.size(); i++) {
    Candidato& c = *listaCandidatos[i];
    if (c.getEleito() && (int)i >= eleicao.getQuantidadeEleitos()) {
      cout << i + 1 << " - ";
      imprimeCandidato(c, this->loc);
    }
  }
}

void EleicaoRelatorio::imprimeVotacaoPartidos() {
  cout << endl;
  cout << "Votação dos partidos e número de candidatos eleitos:" << endl;
  int index = 1;
  for (auto& partido : this->listaPartidos) {
    cout << index << " - ";
    imprimePartido(*partido, this->loc);
    index++;
  }
}

void EleicaoRelatorio::imprimePrimeiroUltimoPartido() {
  // Realiza reordenação especial para imprimir o primeiro e o último mais votado de cada partido
  sort(this->listaPartidos.begin(), this->listaPartidos.end(),
       [](Partido* p1, Partido* p2) {
         return p1->comparaPartidosPorCandidatosMaisVotados(*p2);
       });

  cout << endl;
  cout << "Primeiro e último colocados de cada partido:" << endl;
  int index = 1;
  for (auto& partido : this->listaPartidos) {
    if (partido->getCandidatos().size() <= 1)
      continue;
    cout << index << " - ";

    // Faz uma cópia dos candidatos do partido e ordena (e dessa vez considera o numeroPartido, flag = true)
    vector<Candidato*> candidatosCopy;
    for (auto& candidato : partido->getCandidatos())
      candidatosCopy.push_back(candidato);

    // Ordena os candidatos do partido
    sort(candidatosCopy.begin(), candidatosCopy.end(),
         [](Candidato* c1, Candidato* c2) {
           return c1->compare(*c2, true);
         });

    Candidato maisVotado = *(candidatosCopy[0]);
    Candidato menosVotado = *(candidatosCopy[candidatosCopy.size() - 1]);

    string palavraVoto1 = (maisVotado.getQuantidadeVotos() <= 1) ? "voto" : "votos";
    string palavraVoto2 = (menosVotado.getQuantidadeVotos() <= 1) ? "voto" : "votos";

    ostringstream ossMaisVotado, ossMenosVotado;
    ossMaisVotado.imbue(this->loc);
    ossMenosVotado.imbue(this->loc);

    ossMaisVotado << fixed << maisVotado.getQuantidadeVotos();
    ossMenosVotado << fixed << menosVotado.getQuantidadeVotos();

    string output = partido->getSigla() + " - " + (partido->getNumero()) + ", " +
                    maisVotado.getNomeUrna() + " (" + (maisVotado.getNumeroCandidato()) +
                    ", " + ossMaisVotado.str() + " " + palavraVoto1 + ") / " +
                    menosVotado.getNomeUrna() + " (" + (menosVotado.getNumeroCandidato()) +
                    ", " + ossMenosVotado.str() + " " + palavraVoto2 + ")";

    cout << output << endl;
    index++;
  }
}

void EleicaoRelatorio::eleitosFaixaEtaria() {
  cout << endl;
  cout << "Eleitos, por faixa etária (na data da eleição):" << endl;

  vector<int> faixasEtarias = {0, 0, 0, 0, 0};
  ostringstream oss;
  oss.imbue(this->loc);

  for (auto& candidato : this->listaCandidatos) {
    if (candidato->getEleito()) {
      int idade = candidato->getIdade(this->eleicao.getDataEleicao());

      if (idade < 30) {
        faixasEtarias[0]++;
      } else if (idade >= 30 && idade < 40) {
        faixasEtarias[1]++;
      } else if (idade >= 40 && idade < 50) {
        faixasEtarias[2]++;
      } else if (idade >= 50 && idade < 60) {
        faixasEtarias[3]++;
      } else {
        faixasEtarias[4]++;
      }
    }
  }

  oss << "      Idade < 30: " << faixasEtarias[0] << " ("
      << fixed << setprecision(2)
      << (double)faixasEtarias[0] / eleicao.getQuantidadeEleitos() * 100
      << "%)" << endl;
  string resultado = oss.str();
  cout << resultado;

  oss.str("");

  oss << "30 <= Idade < 40: " << faixasEtarias[1] << " ("
      << fixed << setprecision(2)
      << (double)faixasEtarias[1] / eleicao.getQuantidadeEleitos() * 100
      << "%)" << endl;
  resultado = oss.str();
  cout << resultado;

  oss.str("");

  oss << "40 <= Idade < 50: " << faixasEtarias[2] << " ("
      << fixed << setprecision(2)
      << (double)faixasEtarias[2] / eleicao.getQuantidadeEleitos() * 100
      << "%)" << endl;
  resultado = oss.str();
  cout << resultado;

  oss.str("");

  oss << "50 <= Idade < 60: " << faixasEtarias[3] << " ("
      << fixed << setprecision(2)
      << (double)faixasEtarias[3] / eleicao.getQuantidadeEleitos() * 100
      << "%)" << endl;
  resultado = oss.str();
  cout << resultado;

  oss.str("");

  oss << "60 <= Idade     : " << faixasEtarias[4] << " ("
      << fixed << setprecision(2)
      << (double)faixasEtarias[4] / eleicao.getQuantidadeEleitos() * 100
      << "%)" << endl;
  resultado = oss.str();
  cout << resultado;
}

void EleicaoRelatorio::imprimeRelatorioGenero() {
  cout << endl;
  cout << "Eleitos, por gênero:" << endl;

  int qtdMasculino = 0;
  int qtdFeminino = 0;

  for (auto& candidato : this->listaCandidatos) {
    if (candidato->getEleito()) {
      if (candidato->getGenero() == Genero::MASCULINO)
        qtdMasculino++;
      else
        qtdFeminino++;
    }
  }

  ostringstream oss;
  oss.imbue(this->loc);

  oss << "Feminino:  "
      << qtdFeminino << " ("
      << fixed << setprecision(2)
      << (double)qtdFeminino / eleicao.getQuantidadeEleitos() * 100
      << "%)";
  cout << oss.str() << endl;
  oss.str("");

  oss << "Masculino: "
      << qtdMasculino << " ("
      << fixed << setprecision(2)
      << (double)qtdMasculino / eleicao.getQuantidadeEleitos() * 100
      << "%)";
  cout << oss.str() << endl;
}

void EleicaoRelatorio::imprimeRelatorioGeral() {
  cout << endl;

  int totalVotosValidos = 0, totalVotosNominais = 0, totalVotosLegenda = 0;
  for (auto& partido : this->listaPartidos) {
    totalVotosValidos += partido->getQtdVotos();
    totalVotosNominais += partido->getVotosNominais();
    totalVotosLegenda += partido->getVotosLegenda();
  }

  ostringstream oss;
  oss.imbue(this->loc);

  oss << "Total de votos válidos:    "
      << fixed << setprecision(0)
      << totalVotosValidos;
  cout << oss.str() << endl;
  oss.str("");

  oss << "Total de votos nominais:   "
      << fixed << setprecision(0)
      << totalVotosNominais << " ("
      << fixed << setprecision(2)
      << ((double)totalVotosNominais / totalVotosValidos) * 100.0
      << "%)";
  cout << oss.str() << endl;
  oss.str("");

  oss << "Total de votos de legenda: "
      << fixed << setprecision(0)
      << totalVotosLegenda << " ("
      << fixed << setprecision(2)
      << ((double)totalVotosLegenda / totalVotosValidos) * 100.0
      << "%)";
  cout << oss.str() << endl;
  cout << endl;
}

void EleicaoRelatorio::imprimeTodosRelatorios() {
  imprimeNumeroVagas();
  imprimeCandidatosEleitos();
  imprimeCandidatosMaisVotados();
  imprimeCandidatosEleitosCasoMajoritario();
  imprimeCandidatosEleitosCasoProporcional();
  imprimeVotacaoPartidos();
  imprimePrimeiroUltimoPartido();
  eleitosFaixaEtaria();
  imprimeRelatorioGenero();
  imprimeRelatorioGeral();
}
