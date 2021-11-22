package it.auties.whatsapp.utils;

import it.auties.whatsapp.api.WhatsappConfiguration;
import it.auties.whatsapp.binary.BinaryArray;
import it.auties.whatsapp.manager.WhatsappStore;
import it.auties.whatsapp.protobuf.contact.Contact;
import it.auties.whatsapp.protobuf.model.misc.Node;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This utility class provides helper functionality to easily extract data out of Whatsapp models or raw protobuf messages
 * The use of accessors in those classes is preferred if they are easily available
 */
@UtilityClass
public class WhatsappUtils {
    private int counter = 0;

    /**
     * Returns the phone number associated with a jid
     *
     * @param jid the input jid
     * @return a non-null String
     */
    public String phoneNumberFromJid(@NonNull String jid) {
        return jid.split("@", 2)[0];
    }

    /**
     * Parses c.us jids to standard whatsapp jids
     *
     * @param jid the input jid
     * @return a non-null String
     */
    public String parseJid(@NonNull String jid) {
        return jid.replaceAll("@c\\.us", "@s.whatsapp.net");
    }

    /**
     * Returns a random message id
     *
     * @return a non-null ten character String
     */
    public String randomId() {
        return BinaryArray.random(10).toHex();
    }

    /**
     * Returns a request tag built using {@code configuration}
     *
     * @param configuration the configuration to use to build the message
     * @return a non-null String
     */
    public String buildRequestTag(@NonNull WhatsappConfiguration configuration) {
        return "%s-%s".formatted(configuration.requestTag(), counter++);
    }

    /**
     * Returns a ZoneDateTime for {@code time}
     *
     * @param input the time in seconds since {@link Instant#EPOCH}
     * @return a non-null empty optional if the {@code time} isn't 0
     */
    public Optional<ZonedDateTime> parseWhatsappTime(long input) {
        return Optional.of(input)
                .filter(time -> time != 0)
                .map(time -> ZonedDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault()));
    }

    /**
     * Returns a boolean that determines whether {@code jid} is a group
     *
     * @param jid the input jid
     * @return true if {@code jid} is a group
     */
    public boolean isGroup(@NonNull String jid) {
        return jid.contains("-") || jid.contains("g.us");
    }

    /**
     * Returns a List of WhatsappNodes that represent {@code contacts}
     *
     * @param contacts any number of contacts to convert
     * @return a non-null List of WhatsappNodes
     * @throws IllegalArgumentException if {@code contacts} is empty
     */
    public List<Node> jidsToParticipantNodes(@NonNull Contact... contacts) {
        return jidsToParticipantNodes(Arrays.stream(contacts)
                .map(Contact::jid)
                .toArray(String[]::new));
    }

    /**
     * Returns a List of WhatsappNodes that represent {@code jids}
     *
     * @param jids any number of jids to convert
     * @return a non-null List of WhatsappNodes
     * @throws IllegalArgumentException if {@code jids} is empty
     */
    public List<Node> jidsToParticipantNodes(@NonNull String... jids) {
        return Arrays.stream(jids)
                .map(jid -> new Node("participant", Map.of("jid", jid), null))
                .toList();
    }

    /**
     * Returns a binary array containing an encrypted media
     *
     * @param url the url of the encrypted media to download
     * @return a non-empty optional if the media is available
     */
    public Optional<BinaryArray> readEncryptedMedia(@NonNull String url) {
        try {
            return Optional.of(BinaryArray.of(new URL(url).openStream().readAllBytes()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
