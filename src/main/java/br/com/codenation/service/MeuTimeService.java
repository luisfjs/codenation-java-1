package br.com.codenation.service;

import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;
import br.com.codenation.domain.Jogador;
import br.com.codenation.domain.Time;
import br.com.codenation.repository.JogadorRepository;
import br.com.codenation.repository.TimeRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MeuTimeService {

    private TimeRepository timeRepository;
    private JogadorRepository jogadorRepository;

    public MeuTimeService() {
        this.timeRepository =  new TimeRepository();
        this.jogadorRepository = new JogadorRepository();
    }

    public void incluirTime(Time time) {
        if(buscarTimes().contains(time.getId()))
            throw new IdentificadorUtilizadoException();

        timeRepository.incluir(time);
    }

    public void incluirJogador(Jogador jogador) {
        boolean match = buscarTimes().stream().noneMatch(time -> time.equals(jogador.getIdTime()));
        if(match)
            throw new TimeNaoEncontradoException();

        if(buscarJogadoresDoTime(jogador.getIdTime()).contains(jogador.getId()))
            throw new IdentificadorUtilizadoException();

        jogadorRepository.incluir(jogador);
    }

    public void definirCapitao(Long idJogador) {
        if(jogadorRepository.listar().stream().noneMatch(jogador -> jogador.getId().equals(idJogador)))
            throw new JogadorNaoEncontradoException();

        List<Long> idTimes = buscarTimes();
        Optional<Long> timeJogador = idTimes.stream()
                .filter(idTime -> buscarJogadoresDoTime(idTime).contains(idJogador))
                .findFirst();

        if(timeJogador.isPresent()){
            Time time = timeRepository.recuperar(timeJogador.get());
            time.setIdCapitao(idJogador);
            timeRepository.editar(time);
        }
    }

    public Long buscarCapitaoDoTime(Long idTime) {
        Time time = timeRepository.recuperar(idTime);
        if(Objects.isNull(time.getIdCapitao()))
            throw new CapitaoNaoInformadoException();

        return time.getIdCapitao();
    }

    public String buscarNomeJogador(Long idJogador) {
        return jogadorRepository.recuperar(idJogador).getNome();
    }

    public String buscarNomeTime(Long idTime) {
        return timeRepository.recuperar(idTime).getNome();
    }

    public List<Long> buscarJogadoresDoTime(Long idTime) {
        Time time = timeRepository.recuperar(idTime);
        return jogadorRepository.listar().stream()
                .filter(jogador -> jogador.getIdTime().equals(time.getId()))
                .map(Jogador::getId)
                .sorted()
                .collect(Collectors.toList());
    }

    public Long buscarMelhorJogadorDoTime(Long idTime) {
        return buscarJogadoresDoTime(idTime)
                .stream()
                .map(jogadorRepository::recuperar)
                .max(Comparator.comparing(Jogador::getNivelHabilidade))
                .map(Jogador::getId).orElseThrow(JogadorNaoEncontradoException::new);
    }

    public Long buscarJogadorMaisVelho(Long idTime) {
        return buscarJogadoresDoTime(idTime)
                .stream()
                .map(jogadorRepository::recuperar)
                .min(Comparator.comparing(Jogador::getDataNascimento))
                .map(Jogador::getId).orElseThrow(JogadorNaoEncontradoException::new);
    }

    public List<Long> buscarTimes() {
        return timeRepository.listar().stream().map(Time::getId).collect(Collectors.toList());
    }

    public Long buscarJogadorMaiorSalario(Long idTime) {
        Time time = timeRepository.recuperar(idTime);
        Comparator<Jogador> jogadorComparator = Comparator.comparing(Jogador::getSalario).thenComparing(Jogador::getId, Comparator.reverseOrder());
        return buscarJogadoresDoTime(time.getId()).stream()
                .map(jogadorRepository::recuperar)
                .max(jogadorComparator)
                .map(Jogador::getId).orElse(0l);
    }

    public BigDecimal buscarSalarioDoJogador(Long idJogador) {
        return jogadorRepository.recuperar(idJogador).getSalario();
    }

    public List<Long> buscarTopJogadores(Integer top) {
        Comparator<Jogador> jogadorComparator = Comparator.comparing(Jogador::getNivelHabilidade, Comparator.reverseOrder()).thenComparing(Jogador::getId);
        return jogadorRepository.listar().stream().sorted(jogadorComparator).limit(top).map(Jogador::getId).collect(Collectors.toList());
    }

    public String buscarCorCamisaTimeDeFora(Long timeDaCasa, Long timeDeFora) {
        Time timeCasa = timeRepository.recuperar(timeDaCasa);
        Time timeFora = timeRepository.recuperar(timeDeFora);
        if(timeCasa.getCorUniformePrincipal().equals(timeFora.getCorUniformePrincipal())){
            return timeFora.getCorUniformeSecundario();
        }else {
            return timeFora.getCorUniformePrincipal();
        }
    }

}
