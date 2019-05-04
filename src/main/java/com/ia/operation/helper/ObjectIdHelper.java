package com.ia.operation.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.axonframework.common.Assert;
import org.bson.types.ObjectId;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ObjectIdHelper {

    public static String id(String id) {
        return id == null ? id() : new ObjectId(id.getBytes()).toHexString();
    }

    public static String id() {
        return new ObjectId(UUID.randomUUID().toString().getBytes()).toHexString();
    }

    public static Collection<String> id(Collection<String> ids) {
        Assert.isTrue(!CollectionUtils.isEmpty(ids), () -> "Ids should not be null or empty.");
        return ids.stream().filter(i -> !StringUtils.isEmpty(i)).map(ObjectIdHelper::id).collect(Collectors.toList());
    }

    public static Collection<String> ids2String(Collection<ObjectId> ids) {
        return ids.stream().map(ObjectId::toHexString).collect(Collectors.toList());
    }

    public static Optional<Message> toMessage(Object events) {
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(events);
            return Optional.of(MessageBuilder.withBody(bos.toByteArray()).build());
        } catch (IOException e) {
            log.error("unable to convert events to byte.", e);
            return Optional.empty();
        }
    }

}
