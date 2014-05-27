package models;

/**
 * Created by felipebonezi on 27/05/14.
 */
public abstract class AbstractFactory<K, T> {

    /**
     * Retorna a entidade baseada em seu identificador.
     * @param id
     * @return <T> - entidade
     */
    public abstract T get(K id);

}
