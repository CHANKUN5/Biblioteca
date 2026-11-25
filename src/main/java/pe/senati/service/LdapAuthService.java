package pe.senati.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

@Service
public class LdapAuthService
{
    @Autowired
    private LdapTemplate ldapTemplate;

    public boolean autenticar(String uid, String clave)
    {
        if (uid == null || clave == null || uid.isBlank() || clave.isBlank())
        {
            return false;
        }
        if (!uid.matches("[A-Za-z0-9]+"))
        {
            return false;
        }
        String dn = "uid=" + uid + ",ou=personas,dc=universidad,dc=edu";
        try
        {
            ldapTemplate.getContextSource().getContext(dn, clave).close();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}
