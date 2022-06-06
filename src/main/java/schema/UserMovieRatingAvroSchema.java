package schema;

import cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
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

public class UserMovieRatingAvroSchema implements SerializationSchema<UserMovieRatingInfoModule>, DeserializationSchema<UserMovieRatingInfoModule> {

/*
    transient  SpecificDatumReader<UserMovieRatingInfoModule> reader = new SpecificDatumReader<>(UserMovieRatingInfoModule.getClassSchema());
    transient UserMovieRatingInfoModule userMovieRatingInModule = new UserMovieRatingInfoModule();
    transient SpecificDatumWriter writer = new SpecificDatumWriter(UserMovieRatingInfoModule.getClassSchema());
    transient ByteArrayOutputStream out = new ByteArrayOutputStream();

*/


    @Override
    public UserMovieRatingInfoModule deserialize(byte[] message) throws IOException {
        SpecificDatumReader<UserMovieRatingInfoModule> reader = new SpecificDatumReader<>(UserMovieRatingInfoModule.getClassSchema());
        BinaryDecoder in = DecoderFactory.get().directBinaryDecoder(new ByteArrayInputStream(message), null);
        return reader.read(new UserMovieRatingInfoModule(), in);
    }


    @Override
    public byte[] serialize(UserMovieRatingInfoModule element) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SpecificDatumWriter writer = new SpecificDatumWriter(UserMovieRatingInfoModule.getClassSchema());
        try {
            writer.write(element, EncoderFactory.get().directBinaryEncoder(out, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    @Override
    public boolean isEndOfStream(UserMovieRatingInfoModule nextElement) {
        return false;
    }


    @Override
    public TypeInformation<UserMovieRatingInfoModule> getProducedType() {
        return TypeInformation.of(new TypeHint<UserMovieRatingInfoModule>() {});
    }
}
