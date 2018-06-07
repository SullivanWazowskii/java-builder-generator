package org.idea.plugin.jbgenerator.settings;

public class ClassGenerationOptions {

    private boolean isJacksonEnabled;

    private boolean isRequireNonNullEnabled;

    private boolean isConstructorPropertiesEnabled;

    private boolean isAddSwaggerAnnotationsEnabled;


    private ClassGenerationOptions(boolean isJacksonEnabled,
                                   boolean isRequireNonNullEnabled,
                                   boolean isConstructorPropertiesEnabled,
                                   boolean isAddSwaggerAnnotationsEnabled) {
        this.isJacksonEnabled = isJacksonEnabled;
        this.isRequireNonNullEnabled = isRequireNonNullEnabled;
        this.isConstructorPropertiesEnabled = isConstructorPropertiesEnabled;
        this.isAddSwaggerAnnotationsEnabled = isAddSwaggerAnnotationsEnabled;
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


    public boolean isAddSwaggerAnnotationsEnabled() {
        return isAddSwaggerAnnotationsEnabled;
    }

    @Override
    public String toString() {
        return "ClassGenerationOptions{" +
                "isJacksonEnabled=" + isJacksonEnabled +
                ", isRequireNonNullEnabled=" + isRequireNonNullEnabled +
                ", isConstructorPropertiesEnabled=" + isConstructorPropertiesEnabled +
                ", isAddSwaggerAnnotationsEnabled=" + isAddSwaggerAnnotationsEnabled +
                '}';
    }

    public static final class Builder {
        private boolean isJacksonEnabled;
        private boolean isRequireNonNullEnabled;
        private boolean isConstructorPropertiesEnabled;
        private boolean isAddSwaggerAnnotationsEnabled;

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

        public Builder withIsAddSwaggerAnnotationsEnabled(boolean isAddSwaggerAnnotationsEnabled) {
            this.isAddSwaggerAnnotationsEnabled = isAddSwaggerAnnotationsEnabled;
            return this;
        }


        public ClassGenerationOptions build() {
            return new ClassGenerationOptions(isJacksonEnabled, isRequireNonNullEnabled,
                    isConstructorPropertiesEnabled, isAddSwaggerAnnotationsEnabled);
        }
    }
}
