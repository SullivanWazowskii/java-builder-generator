package org.idea.plugin.jbgenerator.ui;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.NonFocusableCheckBox;
import org.idea.plugin.jbgenerator.entity.Field;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class BuilderDialog {

    public static List<Field> selectFieldsAndOptions(List<PsiFieldMember> members, Project project) {

        if (members == null || members.isEmpty()) {
            return null;
        }

        List<Field> memberWrappers = members.stream().map(Field::new).collect(Collectors.toList());

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
        JCheckBox[] optionCheckBoxes = buildCheckBoxes(propertiesComponent);

        ElementsChooser<Field> chooser = new ElementsChooser<>(memberWrappers, true);

        chooser.selectElements(memberWrappers);

        JComponent contents = new JPanel(new VerticalFlowLayout());
        contents.add(chooser);

        JPanel optionsPanel = new JPanel(new VerticalFlowLayout());
        for (JComponent component : optionCheckBoxes) {
            optionsPanel.add(component);
        }
        contents.add(optionsPanel);


        DialogBuilder builder = new DialogBuilder(project);
        builder.setTitle("Select Fields and Options for the Builder");
        builder.addOkAction();
        builder.addCancelAction();
        builder.setCenterPanel(contents);
        builder.setPreferredFocusComponent(chooser);

        if (builder.show() == DialogWrapper.OK_EXIT_CODE) {

            List<Field> markedElements = chooser.getMarkedElements();
            List<Field> selectedElements = chooser.getSelectedElements();
            List<Field> result = new ArrayList<>();
            for (Field memberWrapper : selectedElements) {
                Field wrapper = new Field(memberWrapper.getPsiFieldMember());
                wrapper.setNullable(!markedElements.contains(memberWrapper));
                result.add(wrapper);
            }
            return result;
        }

        return Collections.emptyList();
    }

    private static JCheckBox[] buildCheckBoxes(PropertiesComponent propertiesComponent) {

        final JCheckBox[] checkBoxesArray = new JCheckBox[5];
        checkBoxesArray[0] = buildJacksonCheckbox(propertiesComponent);
        checkBoxesArray[1] = buildRequireNonNullConstructorCheckbox(propertiesComponent);
        checkBoxesArray[2] = buildConstructorPropertiesCheckbox(propertiesComponent);
        checkBoxesArray[3] = buildAddBuilderCheckbox(propertiesComponent);
        checkBoxesArray[4] = buildAddSwaggerApiAnnotationsCheckbox(propertiesComponent);
        return checkBoxesArray;
    }


    private static JCheckBox buildConstructorPropertiesCheckbox(PropertiesComponent propertiesComponent) {
        JCheckBox optionCheckBox = new NonFocusableCheckBox("Add @ConstructorProperties");
        optionCheckBox.setMnemonic('c');
        optionCheckBox.setToolTipText("ConstructorProperties annotation will be added in constructor");

        String property = BuilderOption.REQUIRE_CONSTRUCTOR_PROPERTIES_IN_CONSTRUCTOR.getProperty();
        optionCheckBox.setSelected(propertiesComponent.isTrueValue(property));
        optionCheckBox.addItemListener(itemEvent ->
                propertiesComponent.setValue(property, Boolean.toString(optionCheckBox.isSelected())));
        return optionCheckBox;

    }

    @NotNull
    private static JCheckBox buildJacksonCheckbox(PropertiesComponent propertiesComponent) {
        JCheckBox optionCheckBox = new NonFocusableCheckBox("Add Jackson annotation");
        optionCheckBox.setMnemonic('j');
        optionCheckBox.setToolTipText("Jackson annotation will be added in constructor and getters");

        String property = BuilderOption.IS_JACKSON_ENABLED.getProperty();
        optionCheckBox.setSelected(propertiesComponent.isTrueValue(property));
        optionCheckBox.addItemListener(itemEvent ->
                propertiesComponent.setValue(property, Boolean.toString(optionCheckBox.isSelected())));
        return optionCheckBox;
    }


    @NotNull
    private static JCheckBox buildRequireNonNullConstructorCheckbox(final PropertiesComponent propertiesComponent) {
        JCheckBox optionCheckBox = new NonFocusableCheckBox("Add requireNonNull in constructor");
        optionCheckBox.setMnemonic('c');
        optionCheckBox.setToolTipText("Objects.requireNonNull will be added in constructor");

        String property = BuilderOption.REQUIRE_NON_NULL_IN_CONSTRUCTOR.getProperty();
        optionCheckBox.setSelected(propertiesComponent.isTrueValue(property));
        optionCheckBox.addItemListener(itemEvent ->
                propertiesComponent.setValue(property, Boolean.toString(optionCheckBox.isSelected())));
        return optionCheckBox;
    }


    @NotNull
    private static JCheckBox buildAddBuilderCheckbox(final PropertiesComponent propertiesComponent) {
        JCheckBox optionCheckBox = new NonFocusableCheckBox("Add inner static builder");
//        optionCheckBox.setMnemonic('c');
        optionCheckBox.setToolTipText("Inner static builder will be added in constructor");

        String property = BuilderOption.ADD_BUILDER.getProperty();
        optionCheckBox.setSelected(propertiesComponent.isTrueValue(property));
        optionCheckBox.addItemListener(itemEvent ->
                propertiesComponent.setValue(property, Boolean.toString(optionCheckBox.isSelected())));
        return optionCheckBox;
    }

    @NotNull
    private static JCheckBox buildAddSwaggerApiAnnotationsCheckbox(final PropertiesComponent propertiesComponent) {
        JCheckBox optionCheckBox = new NonFocusableCheckBox("Add swagger api annotations builder");
//        optionCheckBox.setMnemonic('c');
        optionCheckBox.setToolTipText("Swagger api annotations will be added on class and fields");

        String property = BuilderOption.ADD_SWAGGER_API_ANNOTATIONS.getProperty();
        optionCheckBox.setSelected(propertiesComponent.isTrueValue(property));
        optionCheckBox.addItemListener(itemEvent ->
                propertiesComponent.setValue(property, Boolean.toString(optionCheckBox.isSelected())));
        return optionCheckBox;
    }

}

