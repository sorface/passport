package by.sorface.passport.web.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

@UtilityClass
public class NullUtils {

    public <T> void setIfNonNull(final T value, final Consumer<T> consumer) {
        if (Objects.nonNull(value)) {
            consumer.accept(value);
        }
    }

    public <T> void setIfNonNull(final Collection<T> value, final Consumer<Collection<T>> consumer) {
        if (Objects.nonNull(value) && !value.isEmpty()) {
            consumer.accept(value);
        }
    }

}
