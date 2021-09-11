package va.com.szkal.maymer.meme;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class MemeRecord implements Comparable<MemeRecord> {
    public static final String COUNT_FIELD_NAME = "count";
    public static final String URL_FIELD_NAME = "url";
    public static final String MEME_NAME_FIELD_NAME = "name";

    private static final Comparator<MemeRecord> comparator =
            Comparator.comparing(MemeRecord::getCount).reversed()
                    .thenComparing(MemeRecord::getName);

    String name;
    String url;
    int count;

    public static MemeRecord of(Map<String, String> values) {
        return new MemeRecord(values.get(MEME_NAME_FIELD_NAME),
                values.get(URL_FIELD_NAME),
                Integer.parseInt(values.get(COUNT_FIELD_NAME)));
    }

    @Override
    public int compareTo(@NotNull MemeRecord other) {
        return comparator.compare(this, other);
    }
}
