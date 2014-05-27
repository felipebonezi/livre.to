package models.utils;

import models.classes.User;

/**
 * Created by felipebonezi on 27/05/14.
 */
public abstract class UserUtil {

    /**
     * Retorna se o usuário está apto para realizar determinada ação baseado em seu STATUS.
     * @param user
     * @return true
     */
    public static boolean isAvailable(User user) {
        User.Status status = user.getStatus();
        switch (status) {
            case ACTIVE:
                return true;

            case BLOCKED:
            default:
                return false;
        }
    }

}
