package net.uniquepixels.social.friends;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.uniquepixels.coreapi.database.MongoDatabase;
import org.bson.Document;

import java.util.Optional;
import java.util.UUID;

public record FriendManager(MongoDatabase database) {

    public boolean addFriend(FriendDto player, FriendDto newFriend) {

        if (player.friends().contains(newFriend.playerId()) ||
        newFriend.friends().contains(player.playerId())) {
            return false;
        }

        player.friends().add(newFriend.playerId());
        newFriend.friends().add(player.playerId());

        this.saveDto(player);
        this.saveDto(newFriend);

        return true;
    }

    private boolean existsDto(UUID playerId) {
        return this.collection().countDocuments(Filters.eq("playerId", playerId.toString())) > 0L;
    }

    private void saveDto(FriendDto dto) {

        if (!this.existsDto(dto.playerId())) {
            this.collection().insertOne(new Gson().fromJson(dto.toPlain().toString(), Document.class));
            return;
        }

        this.collection().updateOne(Filters.eq("playerId", dto.playerId().toString()),
                Updates.set("friends", dto.toPlain().friends()));

    }

    private MongoCollection<Document> collection() {
        return this.database.collection("friends", Document.class);
    }

    public Optional<FriendDto> getDto(UUID playerId) {

        Document first = this.collection().find(Filters.eq("playerId", playerId.toString())).first();

        if (first == null) {
            return Optional.empty();
        }

        return Optional.of(new Gson().fromJson(first.toJson(), FriendDto.PlainFriendDto.class).toNormal());
    }

}
