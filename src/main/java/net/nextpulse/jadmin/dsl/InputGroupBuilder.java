package net.nextpulse.jadmin.dsl;

import net.nextpulse.jadmin.ColumnDefinition;
import net.nextpulse.jadmin.ColumnType;
import net.nextpulse.jadmin.Resource;
import net.nextpulse.jadmin.elements.FormInput;
import net.nextpulse.jadmin.elements.FormInputGroup;
import net.nextpulse.jadmin.elements.FormSelect;
import net.nextpulse.jadmin.exceptions.ConfigurationException;
import net.nextpulse.jadmin.helpers.Tuple2;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * DSL class that provides a way to add a group of input fields to the resource.
 *
 * @author yholkamp
 */
public class InputGroupBuilder {

  private final FormInputGroup inputGroup;
  private final Resource resource;

  public InputGroupBuilder(FormInputGroup inputGroup, Resource resource) {
    this.inputGroup = inputGroup;
    this.resource = resource;
  }

  /**
   * Adds an input type field for the given column.
   *
   * @param column internal name of the column to add an input for
   * @return this instance
   */
  public InputGroupBuilder input(String column) {
    // ensure the column exists
    ColumnType columnType = getTypeForColumn(column);
    inputGroup.addInput(new FormInput(column, columnType));
    resource.addEditableColumn(column);
    return this;
  }

  /**
   * Adds an input type field for the given column, with an optional inputValidator function. This function will be
   * called when the user input is submitted, allowing the method to validatePostData or transform (i.e. hash) the user input.
   *
   * @param column internal name of the column to add an input for
   * @return this instance
   */
  public InputGroupBuilder input(String column, InputValidator inputValidator) {
    // ensure the column exists
    ColumnType columnType = getTypeForColumn(column);
    inputGroup.addInput(new FormInput(column, columnType));
    resource.addEditableColumn(column, inputValidator);
    return this;
  }

  /**
   * Retrieves the column type for the column of this table.
   *
   * @param column internal name of the column
   * @return the ColumnType of the provided column
   */
  private ColumnType getTypeForColumn(String column) {
    return resource.getColumnDefinitions().stream()
        .filter(x -> x.getName().equals(column))
        .map(ColumnDefinition::getType)
        .findFirst()
        .orElseThrow(() -> new ConfigurationException("Column " + column + " not found for table " + resource.getTableName()));
  }

  /**
   * Adds a select field to the form for the provided column, using the option values generated by the provided producer.
   *
   * @param column         name of the column to add as select
   * @param selectProducer function that may be called to obtain a list of options, provided as tuples of (internal value, friendly name)
   * @return this instance
   */
  public InputGroupBuilder select(String column, Supplier<List<Tuple2<String, String>>> selectProducer) {
    ColumnType columnType = getTypeForColumn(column);
    inputGroup.addInput(new FormSelect(column, columnType, selectProducer));
    // consider validating that the output of the producer appears to be valid for the type of this column
    // consider passing the primary keys of the current object to the producer function
    return this;
  }
}
