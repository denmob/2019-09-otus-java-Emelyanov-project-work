package ru.otus.pw.library.misc;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw.library.mesages.CommandType;
import ru.otus.pw.library.mesages.MessageTransport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SerializeMessageTransportTest {

    private static Logger logger = LoggerFactory.getLogger(SerializeMessageTransportTest.class);

    @Test
    void serializeObject() {
        MessageTransport messageTransport = new MessageTransport(CommandType.GENERATE_OTP,"GENERATE_OTP");
        byte[] serializeMT = SerializeMessageTransport.serializeObject(messageTransport);
        assertNotNull(serializeMT);
        logger.info(new String(serializeMT));
    }

    @Test
    void deserializeBytes() {
        MessageTransport messageTransport = new MessageTransport( CommandType.GENERATE_OTP,"GENERATE_OTP");
        byte[] serializeMT = SerializeMessageTransport.serializeObject(messageTransport);
        MessageTransport messageTransportNew = SerializeMessageTransport.deserializeBytes(serializeMT);
        assertEquals(messageTransport.toString(),messageTransportNew.toString());
    }
}
