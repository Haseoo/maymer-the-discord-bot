package va.com.szkal.maymer.meme;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class MemeCleanupCron {
    private final MemeRepository memeRepository;

    @Scheduled(cron = "0 0 0 ? * WED")
    public void cleanupMemes() {
        var memes = memeRepository.getAll();
        for (MemeRecord meme : memes) {
            if (!isMemeAvailable(meme.getUrl())) {
                memeRepository.deleteMeme(meme.getName());
            }
        }
    }

    private boolean isMemeAvailable(String memeUrl) {
        try {
            var url = new URL(memeUrl);
            var huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            return huc.getResponseCode() == HttpURLConnection.HTTP_OK;

        } catch (Exception ignored) {
            return false;
        }

    }
}
