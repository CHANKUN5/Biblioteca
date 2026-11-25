package pe.senati.config;

import jakarta.faces.context.FacesContext;

public final class Roles
{
    public static final String CLAVE = "rol";
    public static final String BIBLIOTECARIO = "BIBLIOTECARIO";
    public static final String ESTUDIANTE = "ESTUDIANTE";

    private Roles() {}

    public static boolean esBibliotecario()
    {
        FacesContext contexto = FacesContext.getCurrentInstance();
        if (contexto == null)
        {
            return false;
        }
        return BIBLIOTECARIO.equals(contexto.getExternalContext().getSessionMap().get(CLAVE));
    }

    public static String uid()
    {
        FacesContext contexto = FacesContext.getCurrentInstance();
        if (contexto == null)
        {
            return "";
        }
        Object uid = contexto.getExternalContext().getSessionMap().get("uidLdap");
        return uid == null ? "" : uid.toString();
    }
}
