package io.github.patrikostberg.jdbi.v3.cdi.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;
import javax.enterprise.util.AnnotationLiteral;

import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bean defining a JDBI SQL Object.
 */
public class JdbiBean implements Bean<Object>, PassivationCapable {
    private final static Logger logger = LoggerFactory.getLogger(JdbiBean.class);
    private final static Set<Annotation> qualifiers = Set.of(DefaultLiteral.INSTANCE, AnyLiteral.INSTANCE);

    private final Class<?> clazz;
    private final Set<Type> types;
    private final BeanManager beanManager;

    public JdbiBean(final AnnotatedType<?> at, final BeanManager manager) {
        clazz = at.getJavaClass();
        types = Set.of(clazz, Object.class);
        beanManager = manager;
    }

    @Override
    public Object create(CreationalContext<Object> context) {
        logger.debug("Creating JDBI sql object bean for interface: " + clazz.getName());

        //Find and resolve JDBI bean.
        Set<Bean<?>> beans = beanManager.getBeans(Jdbi.class);
        Bean<?> bean = beanManager.resolve(beans);

        //Get JDBI bean reference.
        Jdbi jdbiRef = (Jdbi) beanManager.getReference(bean, Jdbi.class, context);

        return jdbiRef.onDemand(clazz);
    }

    @Override
    public void destroy(Object instance, CreationalContext<Object> creationalContext) {
    }

    @Override
    public String getId() {
        return clazz.getName();
    }

    @Override
    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return ApplicationScoped.class;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    public static class AnyLiteral extends AnnotationLiteral<Any> implements Any {
        private static final long serialVersionUID = 1L;
        public static final Any INSTANCE = new AnyLiteral();

        private AnyLiteral() {
        }
    }

    public static class DefaultLiteral extends AnnotationLiteral<Default> implements Default {
        private static final long serialVersionUID = 1L;
        public static final Default INSTANCE = new DefaultLiteral();

        private DefaultLiteral() {
        }
    }
}
