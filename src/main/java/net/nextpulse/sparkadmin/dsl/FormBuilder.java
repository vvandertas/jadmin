package net.nextpulse.sparkadmin.dsl;

import net.nextpulse.sparkadmin.Resource;
import net.nextpulse.sparkadmin.elements.FormButtons;
import net.nextpulse.sparkadmin.elements.FormInputGroup;
import net.nextpulse.sparkadmin.elements.ParagraphElement;

import java.util.function.Consumer;

/**
 * @author yholkamp
 */
public class FormBuilder {

  private final Resource resource;

  public FormBuilder(Resource resource) {
    this.resource = resource;
  }

  public FormBuilder inputGroup(String header, Consumer<InputGroupBuilder> consumer) {
    FormInputGroup inputGroup = new FormInputGroup();
    inputGroup.setHeader(header);
    resource.getFormPage().add(inputGroup);
    InputGroupBuilder builder = new InputGroupBuilder(inputGroup, resource);
    consumer.accept(builder);
    return this;
  }

  public FormBuilder paragraph(String content) {
    resource.getFormPage().add(new ParagraphElement(content));
    return this;
  }

  public FormBuilder actions() {
    resource.getFormPage().add(new FormButtons());
    return this;
  }
}