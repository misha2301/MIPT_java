package edu.phystech.hw5.service;

import edu.phystech.hw5.annotation.Cacheable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author kzlv4natoly
 */
public class CacheableInvocationHandler implements InvocationHandler {
    private final Object target;
    private final Map<MethodCallKey, Object> cache = new HashMap<>();

    public CacheableInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
        Method interfaceMethod = getInterfaceMethod(method);

        // Сделаем метод доступным явно (используем AccessibleObject)
        AccessibleObject accessibleMethod = method;
        accessibleMethod.setAccessible(true);

        if (interfaceMethod.isAnnotationPresent(Cacheable.class)) {
            MethodCallKey key = new MethodCallKey(interfaceMethod, arguments);
            if (cache.containsKey(key)) {
                return cache.get(key);
            } else {
                try {
                    Object result = method.invoke(target, arguments);
                    cache.put(key, result);
                    return result;
                } catch (InvocationTargetException e) {
                    // Извлекаем реальное исключение
                    throw e.getCause();
                }
            }
        } else {
            try {
                return method.invoke(target, arguments);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }

    private Method getInterfaceMethod(Method method) throws NoSuchMethodException {
        for (Class<?> iface : target.getClass().getInterfaces()) {
            try {
                return iface.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException ignored) {}
        }
        return method;
    }

    private static class MethodCallKey {
        private final Method method;
        private final Object[] args;

        public MethodCallKey(Method method, Object[] args) {
            this.method = method;
            this.args = args != null ? args.clone() : new Object[0];
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MethodCallKey)) return false;
            MethodCallKey that = (MethodCallKey) o;
            return method.equals(that.method) && Arrays.deepEquals(args, that.args);
        }

        @Override
        public int hashCode() {
            return Objects.hash(method, Arrays.deepHashCode(args));
        }
    }
}