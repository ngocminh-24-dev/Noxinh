package org.company.noxinh.exception.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.company.noxinh.exception.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * The type Data existed exception.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class BadRequestException extends GlobalException {

  /**
   * Instantiates a new Resource not found exception.
   *
   * @param message the message
   */
  public BadRequestException(String message) {
    super(message);
  }

  /**
   * Instantiates a new Data existed exception.
   *
   * @param message the message
   * @param errors  the errors
   */
  public BadRequestException(String message, Map<String, String> errors) {
    super(message);
    this.setErrors(errors);
  }
}
