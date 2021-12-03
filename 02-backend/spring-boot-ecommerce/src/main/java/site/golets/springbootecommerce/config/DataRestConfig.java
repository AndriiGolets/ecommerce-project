package site.golets.springbootecommerce.config;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import site.golets.springbootecommerce.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;
import java.util.Arrays;
import java.util.List;

@Configuration
@AllArgsConstructor
public class DataRestConfig implements RepositoryRestConfigurer {

    public static final HttpMethod[] ONLY_READ = new HttpMethod[]{HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH};
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        Class[] entityClasses = new Class[]{
                Product.class,
                ProductCategory.class,
                State.class,
                Country.class,
                Order.class
        };
        Arrays.stream(entityClasses).forEach(c -> configureUnsupportedActions(config, ONLY_READ, c));
        exposeIds(config);

        cors.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200");
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));
    }

    private void configureUnsupportedActions(RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions, Class claz) {
        config.getExposureConfiguration()
                .forDomainType(claz)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));
    }

}
