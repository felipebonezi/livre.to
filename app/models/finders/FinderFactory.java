package models.finders;

import models.AbstractFactory;
import models.classes.*;

/**
 * Factory de Finder para banco de dados.
 */
public class FinderFactory extends AbstractFactory<Class<?>, IFinder> {

    /**
     * Singleton para o factory.
     */
    private static FinderFactory INSTANCE;

    private FinderFactory() {
        // Do nothing...
    }

    public static FinderFactory getInstance() {
        return (INSTANCE == null ? (INSTANCE = new FinderFactory()) : INSTANCE);
    }

    @Override
    public IFinder get(Class<?> id) {
        IFinder<?> finder = null;

        if (id == User.class) {
            finder = new UserFinder();
        }

        return finder;
    }

}