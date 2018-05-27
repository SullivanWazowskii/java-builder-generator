import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.ConstructorProperties;

import static java.util.Objects.requireNonNull;

public class MyTestClass {
    private final String value;
    private final long value2;
    private final List<Text> valu3;
    private final int value4;

    @ConstructorProperties({"value", "value2", "valu3", "value4"})
    @JsonCreator
    private MyTestClass(@Nonnull @JsonProperty("value") String value,
                        @JsonProperty("value2") long value2,
                        @Nullable @JsonProperty("valu3") List<Text> valu3,
                        @JsonProperty("value4") int value4) {
        this.value = requireNonNull(value, "value");
        this.value2 = value2;
        this.valu3 = valu3;
        this.value4 = value4;
    }

    /**
     * Создает новый объект билдера для {@link MyTestClass}
     *
     * @return new Builder()
     */
    public static Builder builder() {
        return new Builder();
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

    @JsonProperty("valu3")
    public Optional<List<Text>> getValu3() {
        return Optional.ofNullable(valu3);
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

    /**
     * Билдер для {@link MyTestClass}
     */
    public static final class Builder {
        private String value;
        private long value2;
        private List<Text> valu3;
        private int value4;

        public Builder withValue(String value) {
            this.value = value;
            return this;
        }

        public Builder withValue2(long value2) {
            this.value2 = value2;
            return this;
        }

        public Builder withValu3(List<Text> valu3) {
            this.valu3 = valu3;
            return this;
        }

        public Builder withValue4(int value4) {
            this.value4 = value4;
            return this;
        }

        public MyTestClass build() {
            return new MyTestClass(value, value2, valu3, value4);
        }
    }
}
