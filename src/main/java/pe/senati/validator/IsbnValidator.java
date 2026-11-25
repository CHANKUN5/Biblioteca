package pe.senati.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("isbnValidator")
public class IsbnValidator implements Validator<String>
{
    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException
    {
        String isbn = value == null ? "" : value.replace("-", "").replace(" ", "");
        if (!isbn.matches("\\d{13}"))
        {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "El ISBN debe tener 13 dígitos.", null));
        }
    }
}
