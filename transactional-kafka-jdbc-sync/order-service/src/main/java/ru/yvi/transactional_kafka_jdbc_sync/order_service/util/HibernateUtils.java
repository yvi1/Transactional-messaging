package ru.yvi.transactional_kafka_jdbc_sync.order_service.util;

import lombok.experimental.UtilityClass;
import org.hibernate.proxy.HibernateProxy;

@UtilityClass
public class HibernateUtils {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getEffectiveClass(T o) {
        return (Class<T>) (
                o instanceof HibernateProxy proxy
                        ? proxy.getHibernateLazyInitializer().getPersistentClass()
                        : o.getClass());
    }
}
