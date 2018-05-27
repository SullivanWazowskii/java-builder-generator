package org.idea.plugin.jbgenerator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.idea.plugin.jbgenerator.entity.Field;
import org.idea.plugin.jbgenerator.generators.BuilderGenerationStrategy;
import org.idea.plugin.jbgenerator.generators.ConstructorGenerationStrategy;
import org.idea.plugin.jbgenerator.generators.GettersGenerationStrategy;
import org.idea.plugin.jbgenerator.generators.ToStringGenerationStrategy;
import org.idea.plugin.jbgenerator.settings.BuilderGenerationOptions;
import org.idea.plugin.jbgenerator.settings.BuilderGenerationSettings;
import org.idea.plugin.jbgenerator.settings.ClassGenerationOptions;
import org.idea.plugin.jbgenerator.settings.ClassGenerationSettings;

import javax.annotation.Nonnull;
import java.util.List;

import static org.idea.plugin.jbgenerator.utils.PsiAdderReplacePolicy.*;

public class DefaultBuilderPlugin implements BuilderPlugin {

    private final Project project;

    private final ClassGenerationOptions classGenerationOptions;
    private final BuilderGenerationOptions builderGenerationOptions;

    private final ConstructorGenerationStrategy constructorGenerationStrategy;
    private final BuilderGenerationStrategy builderGenerationStrategy;
    private final GettersGenerationStrategy gettersGenerationStrategy;

    private DefaultBuilderPlugin(@Nonnull Project project,
                                 @Nonnull ClassGenerationOptions classGenerationOptions,
                                 @Nonnull BuilderGenerationOptions builderGenerationOptions) {
        this.project = project;

        this.classGenerationOptions = classGenerationOptions;
        this.builderGenerationOptions = builderGenerationOptions;

        this.constructorGenerationStrategy = new ConstructorGenerationStrategy(project);
        this.builderGenerationStrategy = new BuilderGenerationStrategy(project);
        this.gettersGenerationStrategy = new GettersGenerationStrategy(project);
    }

    @Override
    public void process(final PsiClass targetClass, final List<Field> fields) {
        ClassGenerationSettings classGenerationSettings =
                new ClassGenerationSettings(targetClass, fields, classGenerationOptions);
        BuilderGenerationSettings builderGenerationSettings =
                new BuilderGenerationSettings(targetClass, fields, builderGenerationOptions);

        PsiMethod constructor = constructorGenerationStrategy.createConstructor(classGenerationSettings);
        addMethod(targetClass, constructor);

        List<PsiMethod> getters = gettersGenerationStrategy.createGetters(classGenerationSettings);
        addMethods(targetClass, getters);

        // TODO: should be not static
        ToStringGenerationStrategy.generateToString(targetClass);

        PsiMethod builderMethod = builderGenerationStrategy.createBuilderMethod(builderGenerationSettings);
        addMethod(targetClass, builderMethod);

        PsiClass builderClass = builderGenerationStrategy.createBuilderClass(builderGenerationSettings);
        addClass(targetClass, builderClass);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Project project;
        private ClassGenerationOptions classGenerationOptions;
        private BuilderGenerationOptions builderGenerationOptions;

        public Builder withProject(Project project) {
            this.project = project;
            return this;
        }

        public Builder withClassGenerationOptions(ClassGenerationOptions classGenerationOptions) {
            this.classGenerationOptions = classGenerationOptions;
            return this;
        }

        public Builder withBuilderGenerationOptions(BuilderGenerationOptions builderGenerationOptions) {
            this.builderGenerationOptions = builderGenerationOptions;
            return this;
        }

        public BuilderPlugin build() {
            return new DefaultBuilderPlugin(project, classGenerationOptions, builderGenerationOptions);
        }
    }
}
