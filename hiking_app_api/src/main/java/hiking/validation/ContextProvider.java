package hiking.validation;

// Chinmoy Acharjee @ SO - https://stackoverflow.com/questions/57204548/javax-validation-validationexception-hv000064-unable-to-instantiate-constraint

// You create a ContextProvider to provide the bean that you need.
// This ContextProvider allows you to have a bean like this
// ContextProvidcer.getBean(StorePoolRepository.class)
// This is a solution for the NullPointer Exception you get whenever you use the a repository class in the custom validator
// Even when it has been Autowired.

// Also allows for any components needed for the validations. But the caveat here is that I have to make sure that
// This is valid... ask @ tomorrow.

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        ContextProvider.applicationContext = applicationContext;
    }

    public static Object getBean(Class cls){
        return ContextProvider.applicationContext.getBean(cls);
    }
}
