package ru.otus.pw.library.misc;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw.library.message.CommandType;
import ru.otus.pw.library.message.MessageTransport;
import ru.otus.pw.library.model.UserData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SerializeMessageTransportJsonTest {

    private static Logger logger = LoggerFactory.getLogger(SerializeMessageTransportByteTest.class);

    @Test
    void serializeToJson() {
        MessageTransport messageTransport = new MessageTransport("","", CommandType.GENERATE_OTP);
        String json = new Gson().toJson(messageTransport);
        assertNotNull(json);
        logger.info(json);
    }

    @Test
    void deserializeFromJson() {
        MessageTransport messageTransportExpected = new MessageTransport("","", CommandType.GENERATE_OTP);
        UserData userDataExpected = new UserData("1","2","3",4L);
        messageTransportExpected.setData(new Gson().toJson(userDataExpected));
        String json = new Gson().toJson(messageTransportExpected);
        assertNotNull(json);

        MessageTransport messageTransportActual = new Gson().fromJson(json,MessageTransport.class);
        UserData userDataActual = new Gson().fromJson((String) messageTransportActual.getData(),UserData.class);

        assertEquals(messageTransportExpected.toString(),messageTransportActual.toString());
        assertEquals(userDataExpected.toString(),userDataActual.toString());
    }
}
