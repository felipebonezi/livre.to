package models.finders;

import com.avaje.ebean.ExpressionList;
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

}
