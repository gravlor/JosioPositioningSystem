package com.gravlor.josiopositioningsystem.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GateSerializer extends StdSerializer<GateEntity> {

        public GateSerializer() {
            this(null);
        }

        public GateSerializer(Class<GateEntity> t) {
            super(t);
        }

        @Override
        public void serialize(GateEntity entity, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStartObject();
            jgen.writeStringField("from", entity.getKey().getFrom().getName());
            jgen.writeStringField("to", entity.getKey().getTo().getName());
            if (entity.getUntil() != null) {
                jgen.writeStringField("until", entity.getUntil().toString());
            }
            jgen.writeEndObject();
        }
}
