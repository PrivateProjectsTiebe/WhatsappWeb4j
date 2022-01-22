package it.auties.whatsapp.protobuf.contact;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.auties.whatsapp.binary.BinaryFlag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.Arrays;

/**
 * The constants of this enumerated type describe the various status that a {@link Contact} can be in
 */
@AllArgsConstructor
@Accessors(fluent = true)
public enum ContactStatus {
    /**
     * When the contact is online
     */
    AVAILABLE(BinaryFlag.AVAILABLE),

    /**
     * When the contact is offline
     */
    UNAVAILABLE(BinaryFlag.UNAVAILABLE),

    /**
     * When the contact is writing a text message
     */
    COMPOSING(BinaryFlag.COMPOSING),

    /**
     * When the contact is recording an audio message
     */
    RECORDING(BinaryFlag.RECORDING),

    /**
     * When the contact stops writing or recording
     */
    PAUSED(BinaryFlag.PAUSED);

    @Getter
    private final BinaryFlag flag;

    /**
     * Returns the name of this enumerated constant
     *
     * @return a lowercase non-null String
     */
    public String data() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static ContactStatus forValue(@NonNull String jsonValue) {
        return Arrays.stream(values())
                .filter(entry -> entry.name().equalsIgnoreCase(jsonValue))
                .findFirst()
                .orElseThrow();
    }
}
