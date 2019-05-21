package site.clzblog.qrcode.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtils {
    private final StringRedisTemplate stringRedisTemplate;

    public RedisUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Boolean setNx(String key, String value, Long timeout) {
        Boolean setIfAbsent = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        if (timeout != null) {
            stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        }
        return setIfAbsent;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public void setList(String key, List<String> listToken) {
        stringRedisTemplate.opsForList().leftPushAll(key, listToken);
    }


    public void setString(String key, String data, Long timeout) {
        try {
            stringRedisTemplate.opsForValue().set(key, data);
            if (timeout != null) stringRedisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        } catch (Exception ignored) {
        }
    }

    public void begin() {
        stringRedisTemplate.setEnableTransactionSupport(true);
        stringRedisTemplate.multi();
    }

    public void exec() {
        stringRedisTemplate.exec();
    }

    public void discard() {
        stringRedisTemplate.discard();
    }


    public void setString(String key, String data) {
        setString(key, data, null);
    }

    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Boolean delKey(String key) {
        return stringRedisTemplate.delete(key);
    }
}
