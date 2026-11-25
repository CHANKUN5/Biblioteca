package pe.senati.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import pe.senati.config.SpringContext;
import pe.senati.service.PrestamoService;

@FacesValidator("limitePrestamo")
public class LimitePrestamoValidator implements Validator<Object>
{
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        if (value == null || value.toString().isBlank())
        {
            return;
        }
        Integer usuarioId = Integer.valueOf(value.toString());
        long sinDevolver = SpringContext.get(PrestamoService.class).contarSinDevolver(usuarioId);
        if (sinDevolver >= 3)
        {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "No puede llevar otro libro: tiene 3 sin devolver.", null));
        }
    }
}
