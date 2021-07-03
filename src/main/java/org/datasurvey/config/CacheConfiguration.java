package org.datasurvey.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, org.datasurvey.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, org.datasurvey.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, org.datasurvey.domain.User.class.getName());
            createCache(cm, org.datasurvey.domain.Authority.class.getName());
            createCache(cm, org.datasurvey.domain.User.class.getName() + ".authorities");
            createCache(cm, org.datasurvey.domain.ParametroAplicacion.class.getName());
            createCache(cm, org.datasurvey.domain.UsuarioExtra.class.getName());
            createCache(cm, org.datasurvey.domain.UsuarioExtra.class.getName() + ".encuestas");
            createCache(cm, org.datasurvey.domain.UsuarioExtra.class.getName() + ".usuarioEncuestas");
            createCache(cm, org.datasurvey.domain.UsuarioExtra.class.getName() + ".plantillas");
            createCache(cm, org.datasurvey.domain.Encuesta.class.getName());
            createCache(cm, org.datasurvey.domain.Encuesta.class.getName() + ".usuarioEncuestas");
            createCache(cm, org.datasurvey.domain.Encuesta.class.getName() + ".ePreguntaAbiertas");
            createCache(cm, org.datasurvey.domain.Encuesta.class.getName() + ".ePreguntaCerradas");
            createCache(cm, org.datasurvey.domain.EPreguntaAbierta.class.getName());
            createCache(cm, org.datasurvey.domain.EPreguntaAbierta.class.getName() + ".ePreguntaAbiertaRespuestas");
            createCache(cm, org.datasurvey.domain.EPreguntaAbiertaRespuesta.class.getName());
            createCache(cm, org.datasurvey.domain.EPreguntaCerrada.class.getName());
            createCache(cm, org.datasurvey.domain.EPreguntaCerrada.class.getName() + ".ePreguntaCerradaOpcions");
            createCache(cm, org.datasurvey.domain.EPreguntaCerradaOpcion.class.getName());
            createCache(cm, org.datasurvey.domain.UsuarioEncuesta.class.getName());
            createCache(cm, org.datasurvey.domain.Categoria.class.getName());
            createCache(cm, org.datasurvey.domain.Categoria.class.getName() + ".encuestas");
            createCache(cm, org.datasurvey.domain.Categoria.class.getName() + ".plantillas");
            createCache(cm, org.datasurvey.domain.Factura.class.getName());
            createCache(cm, org.datasurvey.domain.Plantilla.class.getName());
            createCache(cm, org.datasurvey.domain.Plantilla.class.getName() + ".pPreguntaCerradas");
            createCache(cm, org.datasurvey.domain.Plantilla.class.getName() + ".pPreguntaAbiertas");
            createCache(cm, org.datasurvey.domain.Plantilla.class.getName() + ".usuarioExtras");
            createCache(cm, org.datasurvey.domain.PPreguntaAbierta.class.getName());
            createCache(cm, org.datasurvey.domain.PPreguntaCerrada.class.getName());
            createCache(cm, org.datasurvey.domain.PPreguntaCerrada.class.getName() + ".pPreguntaCerradaOpcions");
            createCache(cm, org.datasurvey.domain.PPreguntaCerradaOpcion.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
