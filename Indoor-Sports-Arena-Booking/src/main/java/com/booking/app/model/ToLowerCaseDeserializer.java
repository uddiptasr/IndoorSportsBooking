package com.booking.app.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
public class ToLowerCaseDeserializer extends StdDeserializer<String> {
   // private static final long serialVersionUID = 7527542687158493910L;

    public ToLowerCaseDeserializer() {

        super(String.class);log.info("1");
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        log.info("2");
        return _parseString(p, ctxt).toLowerCase();
    }

}
