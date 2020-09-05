package wendergalan.github.io.desafiosambatech.config.converter.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;

import static wendergalan.github.io.desafiosambatech.utility.Utility.DATE_FORMAT;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return LocalDate.parse(parser.getValueAsString(), DATE_FORMAT);
    }
}
