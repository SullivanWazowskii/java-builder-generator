package org.idea.plugin.jbgenerator.settings;

import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

public class BuilderGenerationOptions {


    private static final String DEFUALT_BUILDER_CLASS_NAME = "Builder";
    private static final String DEFUALT_BUILDER_SETTER_PREFIX = "with";
    private static final String DEFUALT_BUILDER_METHOD_NAME = "builder";


    private final String builderClassName;
    private final String builderMethodName;
    private final String builderSetterPrefix;


    private BuilderGenerationOptions(@Nonnull String builderClassName,
                                     @Nonnull String builderMethodName,
                                     @Nonnull String builderSetterPrefix) {
        this.builderClassName = requireNonNull(builderClassName, "builderClassName");
        this.builderMethodName = requireNonNull(builderMethodName, "builderMethodName");
        this.builderSetterPrefix = requireNonNull(builderSetterPrefix, "builderSetterPrefix");
    }

    public static Builder builder() {
        return new Builder();
    }

    @Nonnull
    public String getBuilderClassName() {
        return builderClassName;
    }

    @Nonnull
    public String getBuilderMethodName() {
        return builderMethodName;
    }

    @Nonnull
    public String getBuilderSetterPrefix() {
        return builderSetterPrefix;
    }

    @Override
    public String toString() {
        return "BuilderGenerationOptions{" +
                "builderClassName='" + builderClassName + '\'' +
                ", builderMethodName='" + builderMethodName + '\'' +
                ", builderSetterPrefix='" + builderSetterPrefix + '\'' +
                '}';
    }

    public static final class Builder {

        private String builderClassName;
        private String builderMethodName;
        private String builderSetterPrefix;

        public Builder withBuilderClassName(String builderClassName) {
            this.builderClassName = builderClassName;
            return this;
        }

        public Builder withBuilderMethodName(String builderMethodName) {
            this.builderMethodName = builderMethodName;
            return this;
        }

        public Builder withBuilderSetterPrefix(String builderSetterPrefix) {
            this.builderSetterPrefix = builderSetterPrefix;
            return this;
        }

        public BuilderGenerationOptions build() {
            return new BuilderGenerationOptions(
                    StringUtils.isEmpty(builderClassName) ? DEFUALT_BUILDER_CLASS_NAME : builderClassName,
                    StringUtils.isEmpty(builderMethodName) ? DEFUALT_BUILDER_METHOD_NAME : builderMethodName,
                    builderSetterPrefix == null ? DEFUALT_BUILDER_SETTER_PREFIX : builderSetterPrefix
            );
        }
    }
}
