package pillihuaman.com.pe.support.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    // Definimos los nombres de nuestras cachés como constantes para evitar errores de tipeo.
    public static final String PRODUCTOS_POPULARES_CACHE = "productosPopulares";
    public static final String PRODUCTO_BY_ID_CACHE = "getViewsByIdProduct";
    public static final String SYSTEM_MENU_TREE_CACHE = "systemMenuTree";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // --- POLÍTICA DE CACHÉ PARA PRODUCTOS POPULARES ---
        // Datos que cambian con más frecuencia, pero son costosos.
        // Expiración corta: 5 minutos.
        Caffeine<Object, Object> productosPopularesBuilder = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(100); // Solo una entrada ('allProducts'), pero es buena práctica limitarlo.
        cacheManager.registerCustomCache(PRODUCTOS_POPULARES_CACHE, productosPopularesBuilder.build());

        // --- POLÍTICA DE CACHÉ PARA DETALLE DE PRODUCTO ---
        // Datos de productos individuales. Pueden ser muchos.
        // Expiración media: 30 minutos.
        Caffeine<Object, Object> productoByIdBuilder = Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(1000); // Cachear hasta 1000 productos individuales.
        cacheManager.registerCustomCache(PRODUCTO_BY_ID_CACHE, productoByIdBuilder.build());

        // --- POLÍTICA DE CACHÉ PARA EL ÁRBOL DE MENÚS ---
        // Datos muy estables, cambian rara vez.
        // Expiración larga: 2 horas.
        Caffeine<Object, Object> systemMenuTreeBuilder = Caffeine.newBuilder()
                .expireAfterWrite(2, TimeUnit.HOURS)
                .maximumSize(50); // Probablemente solo unos pocos árboles de menú.
        cacheManager.registerCustomCache(SYSTEM_MENU_TREE_CACHE, systemMenuTreeBuilder.build());

        return cacheManager;
    }
}