package models.utils;

import models.classes.Material;
import models.classes.User;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by felipebonezi on 27/05/14.
 */
public abstract class UserUtil {

    private static final SecureRandom random = new SecureRandom();

    public static String generateAlphaNumeric() {
        return new BigInteger(130, random).toString(32);
    }

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

            case REMOVED:
            default:
                return false;
        }
    }

    public static boolean isOwner(Material material, User user) {
        return material != null && user != null && material.getAuthor().getId() == user.getId();
    }

}
