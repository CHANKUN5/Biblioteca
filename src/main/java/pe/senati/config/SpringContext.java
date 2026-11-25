package pe.senati.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class SpringContext implements ApplicationContextAware
{
    private static ApplicationContext contexto;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        contexto = applicationContext;
    }

    public static <T> T get(Class<T> tipo)
    {
        return contexto.getBean(tipo);
    }
}
