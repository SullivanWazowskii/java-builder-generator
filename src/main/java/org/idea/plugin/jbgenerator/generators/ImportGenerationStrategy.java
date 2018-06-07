package org.idea.plugin.jbgenerator.generators;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;

public class ImportGenerationStrategy {

    public static final String JSON_PROPERTY = "com.fasterxml.jackson.annotation.JsonProperty";
    public static final String JSON_CREATOR = "com.fasterxml.jackson.annotation.JsonCreator";
    public static final String JAVAX_ANNOTATION_NONNULL = "javax.annotation.Nonnull";
    public static final String JAVAX_ANNOTATION_NULLABLE = "javax.annotation.Nullable";
    public static final String OBJECTS_REQUIRE_NON_NULL = "java.util.Objects.requireNonNull";
    public static final String API_MODEL = "io.swagger.annotations.ApiModel";
    public static final String API_PROPERTY = "io.swagger.annotations.ApiModelProperty";

    private final Project project;

    private final PsiElementFactory psiElementFactory;
    private final PsiFileFactory psiFileFactory;
    private final PsiJavaParserFacade psiJavaParserFacade;
    private final JavaPsiFacade javaPsiFacade;

    public ImportGenerationStrategy(Project project) {
        this.project = project;
        this.psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        this.javaPsiFacade = JavaPsiFacade.getInstance(project);
        this.psiFileFactory = PsiFileFactory.getInstance(project);
        this.psiJavaParserFacade = javaPsiFacade.getParserFacade();
    }

    public void addImport(PsiClass targetClass, String fullQualifiedName) {
        // TODO: instanceof check
        PsiJavaFile containingFile = (PsiJavaFile) targetClass.getContainingFile();
        Pair<String, String> packageAndClassName = getPackageAndClassName(fullQualifiedName);
        String packageName = packageAndClassName.getLeft();
        String className = packageAndClassName.getRight();

        PsiJavaFile aFile = createDummyJavaFile("package " + packageName + "; public class " + className + "{}");
        PsiClass psiClass = aFile.getClasses()[0];
        containingFile.importClass(psiClass);
    }

    public void addStaticMethodImport(PsiClass targetClass, String fullQualifiedName) {
        // TODO: instanceof check
        PsiJavaFile containingFile = (PsiJavaFile) targetClass.getContainingFile();
        Pair<String, String> packageAndClassName = getPackageAndClassName(fullQualifiedName);
        String fullQualifiedClassName = packageAndClassName.getLeft();
        String methodName = packageAndClassName.getRight();

        PsiClass objectsClass = javaPsiFacade.findClass(fullQualifiedClassName, GlobalSearchScope.allScope(project));
        PsiImportStaticStatement objectsImport = psiElementFactory.createImportStaticStatement(objectsClass, methodName);
        PsiImportStaticStatement[] importStaticStatements = containingFile.getImportList().getImportStaticStatements();
        for (PsiImportStaticStatement psiImportStaticStatement : importStaticStatements) {
            if (Objects.equals(psiImportStaticStatement.getReferenceName(), objectsImport.getReferenceName())) {
                return;
            }
        }
        containingFile.addBefore(objectsImport, targetClass);
    }

    // TODO: find more appropriate method name
    private Pair<String, String> getPackageAndClassName(String fullQualifiedName) {
        int lastIndexOf = fullQualifiedName.lastIndexOf('.');
        String packageName = fullQualifiedName.substring(0, lastIndexOf);
        String className = fullQualifiedName.substring(lastIndexOf + 1);
        return Pair.of(packageName, className);
    }


    private PsiJavaFile createDummyJavaFile(String text) {
        String fileName = "_Dummy_." + JavaFileType.INSTANCE.getDefaultExtension();
        return (PsiJavaFile) psiFileFactory.createFileFromText(fileName, JavaFileType.INSTANCE, text);
    }

    /*
    private static PsiImportStatementBase extractImport(PsiJavaFile aFile, boolean isStatic) {
        PsiImportList importList = aFile.getImportList();
        assert importList != null : aFile;
        PsiImportStatementBase[] statements = isStatic ? importList.getImportStaticStatements() : importList.getImportStatements();
        assert ((Object[]) statements).length == 1 : aFile.getText();
        return (PsiImportStatementBase) ((Object[]) statements)[0];
    }
    */


}
