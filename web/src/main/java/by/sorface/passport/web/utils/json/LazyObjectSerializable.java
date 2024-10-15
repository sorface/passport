package by.sorface.passport.web.utils.json;

import java.util.function.Supplier;

public class LazyObjectSerializable {

    private final Supplier<String> stringSupplier;

    /**
     * The LazyObjectSerializable function is a wrapper for the Supplier interface.
     * It allows us to pass in a function that will be called when we need to serialize an object,
     * but not before. This way, we can avoid unnecessary computation and memory usage by only computing
     * the string representation of our objects when it's needed. The LazyObjectSerializable class also implements Serializable
     * so that it can be passed around as an argument or return value without losing its state (i.e., its reference to the original function).
     *
     * @param stringSupplier Create a new instance of the lazyobjectserializable class
     */
    public LazyObjectSerializable(final Supplier<String> stringSupplier) {
        this.stringSupplier = stringSupplier;
    }

    /**
     * The toString function returns a string representation of the object.
     *
     * @return The stringsupplier
     */
    @Override
    public String toString() {
        return stringSupplier.get();
    }

}
