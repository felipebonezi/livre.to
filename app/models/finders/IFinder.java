package models.finders;

import java.util.List;

import models.classes.Material;

import com.avaje.ebean.Page;

/**
 * Interface utilizada por todos os Finder para acesso ao banco.
 */
public interface IFinder<K> {

    /**
     * Select para uma entidade única baseado em seu id.
     * @param id
     * @return
     */
    K selectUnique(Long id);

    /**
     * Select para uma entidade única baseado em colunas e argumentos.
     * @param columns
     * @param columnsArgs
     * @return
     */
    K selectUnique(String[] columns, Object[] columnsArgs);

    /**
     * Select para todas as entidades em questão.
     * @return
     */
    List<K> selectAll();

    /**
     * Select de todas as entidades baseado em colunas e argumentos.
     * @param columns
     * @param columnsArgs
     * @return
     */
    List<K> selectAll(String[] columns, Object[] columnsArgs);
    
    /**
     * Retorna uma página da entidade
     * 
     * @param page Número da página a ser exibida
     * @param pageSize Quantidade de itens por página
     * @param sortBy
     * @param order asc ou desc
     * @param filter Filtro aplicado ao título
     */
    Page<K> page(int page, int pageSize, String sortBy, String order, String filter);
}
