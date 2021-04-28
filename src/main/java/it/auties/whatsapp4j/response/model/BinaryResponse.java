package it.auties.whatsapp4j.response.model;

import it.auties.whatsapp4j.model.WhatsappNode;
import jakarta.validation.constraints.NotNull;

import java.lang.reflect.InvocationTargetException;

/**
 * A record that wraps a WhatsappNode sent by WhatsappWeb's WebSocket as response for a request.
 * This WhatsappNode can be converted to a ResponseModel using {@link BinaryResponse#toModel(Class)}.
 * This class is final, this means that it cannot be extended.
 */
public final class BinaryResponse extends Response<WhatsappNode> {
    public BinaryResponse(@NotNull String tag, @NotNull WhatsappNode content) {
        super(tag, null, content);
    }

    /**
     * Converts this object to a BinaryResponseModel
     *
     * @param clazz a class that represents {@code <T>}
     * @param <T>   the specific raw type of the model
     * @return an instance of the type of model requested
     * @throws IllegalArgumentException if the node that message wraps cannot be converted to the specified class
     */
    @Override
    public <T extends ResponseModel> @NotNull T toModel(@NotNull Class<T> clazz) {
        try {
            return clazz.getConstructor(WhatsappNode.class).newInstance(content);
        } catch (InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("Cannot convert %s to %s as an exception occurred while initializing said class".formatted(content, clazz.getName()), e);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot convert %s to %s as the latter doesn't provide a node constructor".formatted(content, clazz.getName()));
        }
    }
}
