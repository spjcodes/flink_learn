package schema;

import cn.jiayeli.movieAnalyse.module.RatingModule;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
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

public class RatingSchema implements SerializationSchema<RatingModule>, DeserializationSchema<RatingModule> {



    @Override
    public RatingModule deserialize(byte[] message) throws IOException {
        SpecificDatumReader<RatingModule> datumReader = new SpecificDatumReader<>(RatingModule.getClassSchema());
        RatingModule ratingModule = new RatingModule();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(message);
        BinaryDecoder binaryDecoder = DecoderFactory.get().directBinaryDecoder(inputStream, null);
        ratingModule = datumReader.read(ratingModule, binaryDecoder);
        return ratingModule;
    }

    @Override
    public boolean isEndOfStream(RatingModule nextElement) {
        return false;
    }

    @Override
    public byte[] serialize(RatingModule element) {
        SpecificDatumWriter<RatingModule> datumWriter = new SpecificDatumWriter<>(RatingModule.getClassSchema());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            BinaryEncoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(output, null);
            datumWriter.write(element, binaryEncoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toByteArray();
    }

    @Override
    public TypeInformation<RatingModule> getProducedType() {
        return TypeInformation.of(new TypeHint<RatingModule>() {});
    }

}
