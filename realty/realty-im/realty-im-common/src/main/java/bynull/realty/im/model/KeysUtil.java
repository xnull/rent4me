package bynull.realty.im.model;

import com.google.common.base.Joiner;
import lombok.Value;
import org.springframework.stereotype.Component;

/**
 * Утилита получения уникального ключа из идентификатора
 *
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@Component
public class KeysUtil {
    /**
     * Главный неймспейс для всей подсистемы сообщений
     */
    public static final String IM_NS = "im";

    /**
     * Chat namespace
     */
    public static final String CHAT_NS = "chats";
    public static final String MESSAGE_NS = "messages";

    /**
     * Класс для формирования ключей для системы сообщений
     * Для коллекций
     */
    @Value
    public static class ImKey<T> {
        private final Class<T> keyType;
        private final String key;

        public static <KeyType> ImKey<KeyType> get(Class<KeyType> type, Object... keyChunks) {
            return new ImKey<>(type, Joiner.on(":").join(keyChunks));
        }
    }

    /**
     * Класс для формирования ключей для Map коллекций
     *
     * @param <K>
     * @param <V>
     */
    @Value
    public static class ImMapKey<K, V> {
        private final Class<K> keyType;
        private final Class<V> valueType;
        private final String key;

        public static <KeyType, ValueType> ImMapKey<KeyType, ValueType> get(Class<KeyType> keyType, Class<ValueType> valueType,
                                                                            Object... keyChunks) {
            return new ImMapKey<>(keyType, valueType, Joiner.on(":").join(keyChunks));
        }
    }
}
