package ru.otus.pw.library.misc;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw.library.mesages.CommandType;
import ru.otus.pw.library.mesages.MessageTransport;
import ru.otus.pw.library.model.UserData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SerializeMessageTransportByteTest {

    private static Logger logger = LoggerFactory.getLogger(SerializeMessageTransportByteTest.class);

    @Test
    void serializeObject() {
        MessageTransport messageTransport = new MessageTransport("","",CommandType.GENERATE_OTP);
        byte[] serializeMT = SerializeMessageTransport.serializeObject(messageTransport);
        assertNotNull(serializeMT);
        logger.info(new String(serializeMT));
    }

    @Test
    void deserializeBytes() {
        MessageTransport messageTransportExpected = new MessageTransport("","", CommandType.GENERATE_OTP);
        UserData userData = new UserData("1","2","3",4L);
        messageTransportExpected.setData(userData);
        byte[] serializeMT = SerializeMessageTransport.serializeObject(messageTransportExpected);

        MessageTransport messageTransportActual = SerializeMessageTransport.deserializeBytes(serializeMT);

        assert messageTransportActual != null;
        assertEquals(messageTransportExpected.toString(),messageTransportActual.toString());
    }
}
