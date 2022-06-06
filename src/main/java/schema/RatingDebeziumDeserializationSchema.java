package schema;

import cn.jiayeli.movieAnalyse.module.RatingModule;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;
import org.apache.kafka.connect.source.SourceRecord;

public class RatingDebeziumDeserializationSchema implements com.ververica.cdc.debezium.DebeziumDeserializationSchema<RatingModule> {
    @Override


    public void deserialize(SourceRecord sourceRecord, Collector<RatingModule> collector) throws Exception {

    }

    @Override
    public TypeInformation<RatingModule> getProducedType() {
        return null;
    }
}
