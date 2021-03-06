package DatabaseSystem;

import UserSystem.*;
import com.mongodb.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * This class is responsible for parsing through a database collection that stores user information and converting
 * that information back into a Registrar instance.
 *
 * @author Jesse
 */
public class ParseToRegistrar implements ParserStrategy {

    private User createUser(DBObject doc) {
        User user;
        Class partypes[] = new Class[3];
        partypes[0] = String.class;
        partypes[1] = String.class;
        partypes[2] = String.class;
        try {
            user = (User) (Class.forName((String) doc.get("type")).getConstructor(partypes).newInstance(
                    doc.get("name"), doc.get("userName"), doc.get("password")));
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException |
                InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        for (String event : (ArrayList<String>) doc.get("events")) {
            user.addEvent(Long.parseLong(event));
        }
        for (String friend : (ArrayList<String>) doc.get("friends")) {
            user.addFriend(friend);
        }
        user.setVipStatus((Boolean) doc.get("vip"));

        return user;
    }

    private Speaker createSpeaker(DBObject doc) {
        Speaker user = (Speaker) createUser(doc);
        for (Long talk: (ArrayList<Long>) doc.get("talks")) {
            user.addTalk(talk);
        }
        return user;
    }

    /**
     * Returns a new Registrar instance based on the given collection.
     *
     * @param collection    The collection that stores user information.
     * @return              A new Registrar instance.
     */
    @Override
    public Savable parseCollection(DBCollection collection) {
        ArrayList<User> users = new ArrayList<>();
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            if (doc.get("type").equals("UserSystem.Speaker")) {
                users.add(createSpeaker(doc));
            } else {
                users.add(createUser(doc));
            }

        }

        return new Registrar(users);
    }
}
