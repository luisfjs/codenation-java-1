package br.com.codenation.repository;

import java.util.List;

public abstract class AbstractRepository<T> {
    public abstract void incluir(T entity);
    public abstract void editar(T entity);
    public abstract void deletar(T entity);
    public abstract T recuperar(Long id);
    public abstract List<T> listar();
}
