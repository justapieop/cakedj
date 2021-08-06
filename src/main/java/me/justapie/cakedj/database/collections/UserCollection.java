package me.justapie.cakedj.database.collections;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.database.models.AudioTrackMin;
import me.justapie.cakedj.database.models.UserModel;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

import java.util.List;

public class UserCollection {
    private static MongoCollection<Document> collection;
    public static void init(MongoDatabase database) {
        collection = database.getCollection(Constants.userCollection);
    }

    public static UserModel getUserData(User user) {
        Document document = collection.find(Filters.eq(Constants.userIDKey, user.getId())).first();

        if (document == null) {
            createUser(user);
            document = collection.find(Filters.eq(Constants.userIDKey, user.getId())).first();
        }

        return parseDocument(document);
    }

    public static void modifyUserConfig(User user, String key, Object value) {
        Document document = collection.find(
                Filters.eq(Constants.userIDKey, user.getId())
        ).first();
        if (document == null) {
            createUser(user);
        }
        document = new Document("$set", new Document(key, value));
        collection.findOneAndUpdate(Filters.eq(Constants.userIDKey, user.getId()), document);
    }

    public static void createUser(User user) {
        collection.insertOne(Constants.getDefaultUserSetting(user));
    }

    public static void deleteUser(User user) {
        collection.findOneAndDelete(Filters.eq(Constants.userIDKey, user.getId()));
    }

    private static UserModel parseDocument(Document document) {
        return new UserModel() {
            @Override
            public String userID() {
                return document.getString(Constants.userIDKey);
            }

            @Override
            public List<List<AudioTrackMin>> playlists() {
                return (List<List<AudioTrackMin>>) document.get(Constants.playlistKey);
            }
        };
    }
}
