package site.golets.springbootecommerce.config;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import site.golets.springbootecommerce.entity.Country;
import site.golets.springbootecommerce.entity.Product;
import site.golets.springbootecommerce.entity.ProductCategory;
import site.golets.springbootecommerce.entity.State;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;

@Configuration
@AllArgsConstructor
public class DataRestConfig implements RepositoryRestConfigurer {

    public static final HttpMethod[] ONLY_READ = new HttpMethod[]{HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        configureUnsupportedActions(config, ONLY_READ, Product.class);
        configureUnsupportedActions(config, ONLY_READ, ProductCategory.class);
        configureUnsupportedActions(config, ONLY_READ, State.class);
        configureUnsupportedActions(config, ONLY_READ, Country.class);
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config){
        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));
    }

    private void configureUnsupportedActions(RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions, Class claz) {
        config.getExposureConfiguration()
                .forDomainType(claz)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)))
                .withCollectionExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)));
    }

}
