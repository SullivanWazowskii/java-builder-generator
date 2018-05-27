package org.idea.plugin.jbgenerator.settings;

public class ClassGenerationOptions {

    private boolean isJacksonEnabled;

    private boolean isRequireNonNullEnabled;

    private boolean isConstructorPropertiesEnabled;


    private ClassGenerationOptions(boolean isJacksonEnabled,
                                   boolean isRequireNonNullEnabled,
                                   boolean isConstructorPropertiesEnabled) {
        this.isJacksonEnabled = isJacksonEnabled;
        this.isRequireNonNullEnabled = isRequireNonNullEnabled;
        this.isConstructorPropertiesEnabled = isConstructorPropertiesEnabled;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isJacksonEnabled() {
        return isJacksonEnabled;
    }

    public boolean isRequireNonNullEnabled() {
        return isRequireNonNullEnabled;
    }

    public boolean isConstructorPropertiesEnabled() {
        return isConstructorPropertiesEnabled;
    }

    @Override
    public String toString() {
        return "ClassGenerationOptions{" +
                "isJacksonEnabled=" + isJacksonEnabled +
                ", isRequireNonNullEnabled=" + isRequireNonNullEnabled +
                ", isConstructorPropertiesEnabled=" + isConstructorPropertiesEnabled +
                '}';
    }

    public static final class Builder {
        private boolean isJacksonEnabled;
        private boolean isRequireNonNullEnabled;
        private boolean isConstructorPropertiesEnabled;

        public Builder withIsJacksonEnabled(boolean isJacksonEnabled) {
            this.isJacksonEnabled = isJacksonEnabled;
            return this;
        }

        public Builder withIsRequireNonNullEnabled(boolean isRequireNonNullEnabled) {
            this.isRequireNonNullEnabled = isRequireNonNullEnabled;
            return this;
        }

        public Builder withIsConstructorPropertiesEnabled(boolean isConstructorPropertiesEnabled) {
            this.isConstructorPropertiesEnabled = isConstructorPropertiesEnabled;
            return this;
        }

        public ClassGenerationOptions build() {
            return new ClassGenerationOptions(isJacksonEnabled, isRequireNonNullEnabled, isConstructorPropertiesEnabled);
        }
    }
}
