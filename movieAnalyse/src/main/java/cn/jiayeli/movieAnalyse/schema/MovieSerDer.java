package cn.jiayeli.movieAnalyse.schema;

import cn.jiayeli.movieAnalyse.module.MovieModule;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MovieSerDer implements SerializationSchema<MovieModule>, DeserializationSchema<MovieModule> {

    private static MovieModule movieModule;
    private static SpecificDatumReader<MovieModule> datumReader = new SpecificDatumReader<>(MovieModule.getClassSchema());
    private static SpecificDatumWriter<MovieModule> writer = new SpecificDatumWriter<>(MovieModule.getClassSchema());


    @Override
    public MovieModule deserialize(byte[] message) throws IOException {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(message);
        BinaryDecoder decoder = DecoderFactory.get().directBinaryDecoder(inputStream, null);
        return datumReader.read(movieModule, decoder);
    }

    @Override
    public boolean isEndOfStream(MovieModule movieModule) {
        return false;
    }

    @Override
    public TypeInformation<MovieModule> getProducedType() {
        return TypeInformation.of(new TypeHint<MovieModule>() {});
    }


    @Override
    public byte[] serialize(MovieModule element) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Encoder encoder = EncoderFactory.get().directBinaryEncoder(output, null);
        try {
            writer.write(element, encoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toByteArray();
    }
}
