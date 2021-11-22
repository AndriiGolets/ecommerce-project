package site.golets.springbootecommerce.config;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import site.golets.springbootecommerce.entity.Product;
import site.golets.springbootecommerce.entity.ProductCategory;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Type;

@Configuration
@AllArgsConstructor
public class DataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};
        configureUnsupportedActions(config, theUnsupportedActions, Product.class);
        configureUnsupportedActions(config, theUnsupportedActions, ProductCategory.class);
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
