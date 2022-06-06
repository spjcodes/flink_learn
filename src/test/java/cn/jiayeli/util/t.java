package cn.jiayeli.util;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.streaming.api.functions.sink.TwoPhaseCommitSinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

public class t extends TwoPhaseCommitSinkFunction<String, FlinkKafkaProducer.KafkaTransactionState,
        FlinkKafkaProducer.KafkaTransactionContext > {

    /**
     * Use default {@link ListStateDescriptor} for internal state serialization. Helpful utilities
     * for using this constructor are {@link TypeInformation#of(Class)}, {@link
     * TypeHint} and {@link TypeInformation#of(TypeHint)}.
     * Example:
     *
     * <pre>{@code
     * TwoPhaseCommitSinkFunction(TypeInformation.of(new TypeHint<State<TXN, CONTEXT>>() {}));
     * }</pre>
     *
     * @param transactionSerializer {@link TypeSerializer} for the transaction type of this sink
     * @param contextSerializer     {@link TypeSerializer} for the context type of this sink
     */
    public t(TypeSerializer<FlinkKafkaProducer.KafkaTransactionState> transactionSerializer, TypeSerializer<FlinkKafkaProducer.KafkaTransactionContext> contextSerializer) {
        super(transactionSerializer, contextSerializer);
    }

    @Override
    protected void invoke(FlinkKafkaProducer.KafkaTransactionState transaction, String value, Context context) throws Exception {

    }

    @Override
    protected FlinkKafkaProducer.KafkaTransactionState beginTransaction() throws Exception {
        return null;
    }

    @Override
    protected void preCommit(FlinkKafkaProducer.KafkaTransactionState transaction) throws Exception {

    }

    @Override
    protected void commit(FlinkKafkaProducer.KafkaTransactionState transaction) {

    }

    @Override
    protected void abort(FlinkKafkaProducer.KafkaTransactionState transaction) {

    }
}
