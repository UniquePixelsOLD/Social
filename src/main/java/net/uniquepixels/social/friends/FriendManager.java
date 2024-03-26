package net.uniquepixels.social.friends;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FriendManager {

  private final OkHttpClient httpClient = new OkHttpClient();
  private final String requestUrl = System.getenv("BASE_URL") + "/friends/";

  public CompletableFuture<Boolean> addFriend(UUID dto1, UUID dto2) {

    CompletableFuture<Boolean> future = new CompletableFuture<>();

    Request url = new Request.Builder()
      .put(RequestBody.create("", MediaType.get("application/json")))
      .url(this.requestUrl + dto1.toString() + "/add/" + dto2.toString()).build();

    try {
      Response execute = this.httpClient.newCall(url).execute();
      future.complete(execute.code() == 200);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return future;
  }

  public CompletableFuture<Boolean> removeFriend(FriendDto dto1, FriendDto dto2) {

    CompletableFuture<Boolean> future = new CompletableFuture<>();

    Request url = new Request.Builder()
      .put(RequestBody.create(dto1.toPlain().toString(), MediaType.get("application/json")))
      .url(this.requestUrl + dto1.playerId().toString() + "/add/" + dto2.playerId().toString()).build();

    try {
      Response execute = this.httpClient.newCall(url).execute();
      future.complete(execute.code() == 200);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return future;
  }

  public Optional<FriendDto> getFriendDto(UUID uuid) {

    Request url = new Request.Builder()
      .post(RequestBody.create("", MediaType.get("application/json")))
      .url(this.requestUrl + "create/" + uuid.toString()).build();

    try {
      Response execute = this.httpClient.newCall(url).execute();

      if (execute.code() != 200 && execute.body() == null) {
        return Optional.empty();
      }

      FriendDto friendDto = new Gson().fromJson(
        execute.body().string(), FriendDto.class);

      return Optional.of(friendDto);

    } catch (IOException e) {
      e.fillInStackTrace();
    }

    return Optional.empty();
  }

  public boolean createFriendDto(FriendDto dto) {

    Request.Builder url = new Request.Builder()
      .post(RequestBody.create(dto.toPlain().toString(), MediaType.get("application/json")))
      .url(this.requestUrl + "create/" + dto.playerId().toString());

    try {
      Response execute = this.httpClient.newCall(url.build()).execute();

      return execute.code() == 202;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

}
