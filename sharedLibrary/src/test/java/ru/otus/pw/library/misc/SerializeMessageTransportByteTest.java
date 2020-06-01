package ru.otus.pw.library.misc;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw.library.message.CommandType;
import ru.otus.pw.library.message.MessageTransport;
import ru.otus.pw.library.model.UserData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SerializeMessageTransportByteTest {

    private static Logger logger = LoggerFactory.getLogger(SerializeMessageTransportByteTest.class);

    @Test
    void serializeObject() {
        MessageTransport messageTransport = new MessageTransport("", "", CommandType.GENERATE_OTP);
        byte[] serializeMT = SerializeMessageTransport.serializeMessageTransportToByteArray(messageTransport);
        assertNotNull(serializeMT);
        logger.info(new String(serializeMT));
    }

    @Test
    void deserializeBytes() {
        MessageTransport messageTransportExpected = new MessageTransport("", "", CommandType.GENERATE_OTP);
        UserData userData = new UserData("1", "2", "3", 4L);
        messageTransportExpected.setData(userData);
        byte[] serializeMT = SerializeMessageTransport.serializeMessageTransportToByteArray(messageTransportExpected);

        MessageTransport messageTransportActual = SerializeMessageTransport.deserializeByteArrayToMessageTransport(serializeMT);

        assert messageTransportActual != null;
        assertEquals(messageTransportExpected.toString(), messageTransportActual.toString());
    }
}
