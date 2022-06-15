package va.com.szkal.maymer.meme;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static va.com.szkal.maymer.meme.MemeRecord.*;

@Repository
public class MemeRepository {

    private final Jedis jedis;

    public MemeRepository(@Qualifier("meme") Jedis jedis) {
        this.jedis = jedis;
    }

    public Optional<String> getMemeUrl(long serverId, String memeName) {
        var id = prepareId(serverId, memeName);
        String memeUrl = jedis.hget(id, URL_FIELD_NAME);
        if (memeUrl == null) {
            return Optional.empty();
        }
        jedis.hincrBy(id, COUNT_FIELD_NAME, 1);
        return Optional.of(memeUrl);
    }

    public Collection<MemeRecord> getTopList(long serverId) {
        return getAll(serverId).stream()
                .filter(meme -> meme.getCount() > 0)
                .limit(10)
                .collect(Collectors.toList());
    }

    public Set<String> getAllNames(long serverId) {
        var result = new TreeSet<String>();
        String cursor = ScanParams.SCAN_POINTER_START;
        boolean cycleIsFinished = false;
        while (!cycleIsFinished) {
            ScanResult<String> scan = jedis.scan(cursor, getAllServerMemesMatch(serverId));
            scan.getResult().stream().map(id -> jedis.hget(id, MEME_NAME_FIELD_NAME)).forEach(result::add);
            cursor = scan.getCursor();
            if (cursor.equals("0")) {
                cycleIsFinished = true;
            }
        }
        return result;
    }

    public Collection<MemeRecord> getAll(long serverId) {
        var scanParams = getAllServerMemesMatch(serverId);
        return getAll(scanParams);
    }

    private ScanParams getAllServerMemesMatch(long serverId) {
        return new ScanParams().match(String.format("%s#*", serverId));
    }

    public Collection<MemeRecord> getAll() {
        return getAll(new ScanParams().match("*"));
    }

    @NotNull
    private Collection<MemeRecord> getAll(ScanParams scanParams) {
        var resultSet = new TreeSet<MemeRecord>();
        String cursor = ScanParams.SCAN_POINTER_START;
        boolean cycleIsFinished = false;
        while (!cycleIsFinished) {
            ScanResult<String> scan = jedis.scan(cursor, scanParams);
            scan.getResult().stream()
                    .map(jedis::hgetAll)
                    .map(MemeRecord::of)
                    .forEach(resultSet::add);
            cursor = scan.getCursor();
            if (cursor.equals("0")) {
                cycleIsFinished = true;
            }
        }
        return resultSet;
    }

    public boolean addMeme(long serverId, String memeName, String memeUrl) {
        var id = prepareId(serverId, memeName);
        if (jedis.exists(id)) {
            return false;
        }
        jedis.hset(id, URL_FIELD_NAME, memeUrl);
        jedis.hset(id, COUNT_FIELD_NAME, "0");
        jedis.hset(id, MEME_NAME_FIELD_NAME, memeName);
        return true;
    }

    public boolean updateMeme(long serverId, String memeName, String memeUrl) {
        var id = prepareId(serverId, memeName);
        if (!jedis.exists(id)) {
            return false;
        }
        jedis.hset(id, URL_FIELD_NAME, memeUrl);
        return true;
    }

    public boolean deleteMeme(long serverId, String memeName) {
        var id = prepareId(serverId, memeName);
        if (!jedis.exists(id)) {
            return false;
        }
        return jedis.del(id) != 0;
    }

    public void deleteMeme(String id) {
        jedis.del(id);
    }

    private String prepareId(long serverId, String memeName) {
        return String.format("%s#%s", serverId, memeName.toLowerCase());
    }
}
