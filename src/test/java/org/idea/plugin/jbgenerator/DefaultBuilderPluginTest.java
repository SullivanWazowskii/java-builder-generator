package org.idea.plugin.jbgenerator;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiJavaFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.idea.plugin.jbgenerator.entity.Field;
import org.idea.plugin.jbgenerator.settings.BuilderGenerationOptions;
import org.idea.plugin.jbgenerator.settings.ClassGenerationOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DefaultBuilderPluginTest extends LightCodeInsightFixtureTestCase {

    private static final Logger log = LoggerFactory.getLogger(DefaultBuilderPluginTest.class);

    @Override
    protected String getTestDataPath() {
        return "src/test/resources/testData";
    }

    @Override
    protected void setUp() throws Exception {
        ConsoleAppender consoleAppender1 = new ConsoleAppender(new PatternLayout("%r [%t] %p %c %x - %m%n"));
        consoleAppender1.setThreshold(Level.INFO);

        BasicConfigurator.configure(consoleAppender1);
        log.info("Hello test!");
        super.setUp();
    }

    public void testProcess() {

        myFixture.configureByFile("MyTestClass.java");
        myFixture.copyFileToProject("Objects.java");
        myFixture.copyFileToProject("JsonProperty.java");

        PsiJavaFile file = (PsiJavaFile) myFixture.getFile();

        Project project = myFixture.getProject();
        log.info("project={}", project);


        BuilderPlugin defaultBuilderPlugin = DefaultBuilderPlugin.builder()
                .withProject(project)
                .withClassGenerationOptions(ClassGenerationOptions.builder()
                        .withIsConstructorPropertiesEnabled(true)
                        .withIsRequireNonNullEnabled(true)
                        .withIsJacksonEnabled(true)
                        .build())
                .withBuilderGenerationOptions(BuilderGenerationOptions.builder().build())
                .build();

        PsiClass myTestClass = file.getClasses()[0];

        PsiField[] allFields = myTestClass.getFields();
        List<Field> psiFieldMemberList = new ArrayList<>();
        for (PsiField psiField : allFields) {
            psiFieldMemberList.add(new Field(new PsiFieldMember(psiField)));
        }
        psiFieldMemberList.get(1).setNullable(true);
        psiFieldMemberList.get(2).setNullable(true);


        WriteCommandAction.runWriteCommandAction(project, () -> {
            defaultBuilderPlugin.process(myTestClass, psiFieldMemberList);
        });

        assertEquals(myTestClass.getInnerClasses().length, 1);
        log.info(myTestClass.getInnerClasses()[0].getName());
    }
}