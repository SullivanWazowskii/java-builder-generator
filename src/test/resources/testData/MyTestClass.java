import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

public class MyTestClass {
    private final String value;
    private final long value2;
    private final List<Text> valu3;
    private final int value4;

    @java.beans.ConstructorProperties({"value", "value2", "valu3", "value4"})
    @JsonCreator
    private MyTestClass(@Nonnull @JsonProperty("value") String value,
                        @JsonProperty("value2") long value2,
                        @Nonnull @JsonProperty("valu3") List<Text> valu3,
                        @JsonProperty("value4") int value4) {
        this.value = requireNonNull(value, "value");
        this.value2 = value2;
        this.valu3 = requireNonNull(valu3, "valu3");
        this.value4 = value4;
    }

    @Nonnull
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value2")
    public long getValue2() {
        return value2;
    }

    @Nonnull
    @JsonProperty("valu3")
    public List<Text> getValu3() {
        return valu3;
    }

    @JsonProperty("value4")
    public int getValue4() {
        return value4;
    }

    @Override
    public String toString() {
        return "MyTestClass{" +
                "value='" + value + '\'' +
                ", value2=" + value2 +
                ", valu3=" + valu3 +
                ", value4=" + value4 +
                '}';
    }
}
