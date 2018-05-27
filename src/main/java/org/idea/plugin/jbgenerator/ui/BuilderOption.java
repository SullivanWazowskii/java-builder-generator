package org.idea.plugin.jbgenerator.ui;

public enum BuilderOption {

    IS_JACKSON_ENABLED("IS_JACKSON_ENABLED"),
    REQUIRE_NON_NULL_IN_CONSTRUCTOR("REQUIRE_NON_NULL_IN_CONSTRUCTOR"),
    REQUIRE_CONSTRUCTOR_PROPERTIES_IN_CONSTRUCTOR("REQUIRE_CONSTRUCTOR_PROPERTIES_IN_CONSTRUCTOR"),;

    private final String property;

    BuilderOption(final String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
