package br.com.codenation.repository;

import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.domain.Jogador;

import java.util.ArrayList;
import java.util.List;

public class JogadorRepository extends AbstractRepository<Jogador>  {
    List<Jogador> jogadores = new ArrayList<>();

    @Override
    public void incluir(Jogador jogador) {
        jogadores.add(jogador);
    }

    @Override
    public void editar(Jogador jogador) {
        int index = jogadores.indexOf(jogador);
        jogadores.set(index, jogador);
    }

    @Override
    public void deletar(Jogador jogador) {
        jogadores.remove(jogador);
    }

    @Override
    public Jogador recuperar(Long id) {
        return jogadores.stream().filter(jogador -> jogador.getId().equals(id)).findFirst().orElseThrow(JogadorNaoEncontradoException::new);
    }

    @Override
    public List<Jogador> listar() {
        return jogadores;
    }
}
