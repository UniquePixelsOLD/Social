package net.uniquepixels.social.friends;

import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record FriendDto(UUID playerId, List<UUID> friends) {

    public PlainFriendDto toPlain() {
        return new PlainFriendDto(playerId.toString(), friends.stream().map(UUID::toString).collect(Collectors.toList()));
    }

    public record PlainFriendDto(String playerId, List<String> friends) {

        public FriendDto toNormal() {

            return new FriendDto(UUID.fromString(playerId),
                    friends.stream().map(UUID::fromString).collect(Collectors.toList()));

        }

        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

}
