package shop.example.service;

public interface RedisService {
    void setString(String key,String value,Long expiration);
    String getString(String key);
    void setVersionAccessToken(String key,Long value,Long expiration);
    void deleteKey(String key);
    void incrementAccessTokenVersion(String key);

}
