package br.com.codenation.repository;

import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;
import br.com.codenation.domain.Time;

import java.util.ArrayList;
import java.util.List;

public class TimeRepository extends AbstractRepository<Time>  {
    List<Time> times = new ArrayList<>();

    @Override
    public void incluir(Time time) {
        times.add(time);
    }

    @Override
    public void editar(Time time) {
        int index = times.indexOf(time);
        times.set(index, time);
    }

    @Override
    public void deletar(Time time) {
        times.remove(time);
    }

    @Override
    public Time recuperar(Long id) {
        return times.stream().filter(time -> time.getId().equals(id)).findFirst().orElseThrow(TimeNaoEncontradoException::new);
    }

    @Override
    public List<Time> listar() {
        return times;
    }
}
