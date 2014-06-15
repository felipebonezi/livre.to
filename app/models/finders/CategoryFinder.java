package models.finders;

import java.util.List;

import models.classes.Category;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

/**
 * Entidade para acesso ao Finder da entidade Category.class
 */
public class CategoryFinder extends AbstractFinder<Category> implements IFinder<Category> {

	/**
	 * Construtor para acesso apenas do FinderFactory.class
	 */
	CategoryFinder() {
		super(Category.class);
	}

	@Override
	public Category selectUnique(Long id) {
		ExpressionList<Category> expressionList = super.generateEqualExpressions(new String[] {ID}, new Object[] {id});
		return expressionList.findUnique();
	}

	@Override
	public Category selectUnique(String[] columns, Object[] columnsArgs) {
		ExpressionList<Category> expressionList = super.generateEqualExpressions(columns, columnsArgs);
		return (expressionList != null ? expressionList.findUnique() : null);
	}

	@Override
	public List<Category> selectAll() {
		return getFinder().findList();
	}

	@Override
	public List<Category> selectAll(String[] columns, Object[] columnsArgs) {
		ExpressionList<Category> expressionList = super.generateEqualExpressions(columns, columnsArgs);
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
	public Page<Category> page(int page, int pageSize, String sortBy, String order, String filter) {
		return getFinder()
			.where()
			.ilike("name", "%" + filter + "%")
			.orderBy(sortBy + " " + order)
			.findPagingList(pageSize)
			.setFetchAhead(false)
			.getPage(page);
	}

	public Page<Category> page() {
		return page(0, 10, "name", "asc", "");
	}

}
