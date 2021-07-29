package me.justapie.cakedj.database.collections;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sedmelluq.lava.extensions.youtuberotator.tools.ip.IpBlock;
import com.sedmelluq.lava.extensions.youtuberotator.tools.ip.Ipv6Block;
import me.justapie.cakedj.Constants;
import me.justapie.cakedj.database.models.ConfigModel;
import org.bson.Document;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ConfigCollection {
    private static MongoCollection<Document> collection;
    public static void init(MongoDatabase database) {
        collection = database.getCollection(Constants.configCollection);
    }

    public static ConfigModel getConfig() {
        Document document = collection.find().first();

        if (document == null) {
            LoggerFactory.getLogger(ConfigCollection.class).info(Constants.noConfigDefined);
            System.exit(0);
        }

        return new ConfigModel() {
            @Override
            public String token() {
                return document.getString(Constants.tokenKey);
            }

            @Override
            public String ownerID() {
                return document.getString(Constants.ownerIDKey);
            }

            @Override
            public String dblToken() {
                return document.getString(Constants.dblTokenKey);
            }

            @Override
            public List<IpBlock> ipv6Block() {
                List<String> cidr = document.getList("ipv6Block", String.class);
                List<IpBlock> blocks = new ArrayList<>();
                for (String s : cidr)
                    blocks.add(new Ipv6Block(s));

                return blocks;
            }
        };
    }
}
