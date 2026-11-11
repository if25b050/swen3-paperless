package at.fh.technikum.paperless_rest.common.dal;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionRollbackHelper {

    public static void registerRollbackFunction(Runnable compensationFunction) {
        // Grafana-Stack (+Loki) vs ElasticSearch + Kibana

        // Register compensation if the transaction fails/rolls back
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                        // Execute your compensation logic
                        compensationFunction.run();
                    }
                }
            });
        }
    }
}
