package models.finders;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;

import models.classes.Material;
import models.classes.User;

import java.util.List;

/**
 * Entidade para acesso ao Finder da entidade User.class
 */
public class UserFinder extends AbstractFinder<User> implements IFinder<User> {

	/**
	 * Construtor para acesso apenas do FinderFactory.class
	 */
	UserFinder() {
		super(User.class);
	}

	@Override
	public User selectUnique(Long id) {
		ExpressionList<User> expressionList = super.generateEqualExpressions(new String[] { ID }, new Object[] { id });
		return expressionList.findUnique();
	}

	@Override
	public User selectUnique(String[] columns, Object[] columnsArgs) {
		ExpressionList<User> expressionList = super.generateEqualExpressions(columns, columnsArgs);
		return (expressionList != null ? expressionList.findUnique() : null);
	}

	@Override
	public List<User> selectAll() {
		return getFinder().findList();
	}

	@Override
	public List<User> selectAll(String[] columns, Object[] columnsArgs) {
		ExpressionList<User> expressionList = super.generateEqualExpressions(columns, columnsArgs);
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
	public Page<User> page(int page, int pageSize, String sortBy,
		String order, String filter) {
	
	return getFinder()
		.where()
		.ilike("login", "%" + filter + "%")
		.orderBy(sortBy + " " + order)
		.findPagingList(pageSize)
		.setFetchAhead(false)
		.getPage(page);
	}

	public Page<User> page() {
		return page(0, 10, "id", "asc", "");
	}
}
