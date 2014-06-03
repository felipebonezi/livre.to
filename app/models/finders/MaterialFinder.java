package models.finders;

import java.util.List;

import models.classes.Material;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

/**
 * Entidade para acesso ao Finder da entidade Material.class
 */
public class MaterialFinder extends AbstractFinder<Material> implements
	IFinder<Material> {

    /**
     * Construtor para acesso apenas do FinderFactory.class
     */
    MaterialFinder() {
	super(Material.class);
    }

    @Override
    public Material selectUnique(Long id) {
	ExpressionList<Material> expressionList = super
		.generateEqualExpressions(new String[] { ID },
			new Object[] { id });
	return expressionList.findUnique();
    }

    @Override
    public Material selectUnique(String[] columns, Object[] columnsArgs) {
	ExpressionList<Material> expressionList = super
		.generateEqualExpressions(columns, columnsArgs);
	return (expressionList != null ? expressionList.findUnique() : null);
    }

    @Override
    public List<Material> selectAll() {
	return getFinder().findList();
    }

    @Override
    public List<Material> selectAll(String[] columns, Object[] columnsArgs) {
	ExpressionList<Material> expressionList = super
		.generateEqualExpressions(columns, columnsArgs);
	return (expressionList != null ? expressionList.findList() : null);
    }

    /**
     * Retorna uma página de materiais
     * 
     * @param page Número da página a ser exibida
     * @param pageSize Quantidade de itens por página
     * @param sortBy
     * @param order asc ou desc
     * @param filter Filtro aplicado ao título
     */
    public Page<Material> page(int page, int pageSize, String sortBy,
	    String order, String filter) {
	
	return getFinder()
		.where()
		.ilike("title", "%" + filter + "%")
		.orderBy(sortBy + " " + order)
		.fetch("author")
		.findPagingList(pageSize)
		.setFetchAhead(false)
		.getPage(page);
    }

}
