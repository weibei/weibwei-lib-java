package com.weibei.java8.example;

import com.weibei.client.Client;
import com.weibei.client.subscriptions.ServerInfo;
import com.weibei.client.subscriptions.SubscriptionManager;
import com.weibei.client.transport.impl.JavaWebSocketTransportImpl;
import com.weibei.core.coretypes.Amount;
import com.weibei.core.coretypes.STObject;
import com.weibei.core.types.known.sle.entries.Offer;
import com.weibei.core.types.known.tx.result.TransactionResult;

import java.util.logging.Level;

import static com.weibei.java8.utils.Print.print;

/**
 * This example subscribes to all transactions and prints executed
 * offers.
 */
public class OffersExecuted {
    public static void main(String[] args) {
        Client.logger.setLevel(Level.OFF);
        new Client(new JavaWebSocketTransportImpl())
            .connect("wss://s-east.weibei.com",
                    OffersExecuted::onceConnected);
    }

    private static void onceConnected(Client c) {
        c.subscriptions.addStream(SubscriptionManager.Stream.transactions);
        c.onLedgerClosed(OffersExecuted::onLedgerClosed)
         .onValidatedTransaction((tr) -> tr.meta.affectedNodes().forEach((an) -> {
             if (an.isOffer() && an.wasPreviousNode()) {
                 printTrade(tr, (Offer) an.nodeAsPrevious(),
                                (Offer) an.nodeAsFinal());
             }
         }));
    }

    private static void onLedgerClosed(ServerInfo serverInfo) {
        print("Ledger {0} closed @ {1} with {2} transactions",
              serverInfo.ledger_index, serverInfo.date(),
                    serverInfo.txn_count);
    }

    private static void printTrade(TransactionResult tr,
                                   Offer before,
                                   Offer after) {
        // Executed define non negative amount of Before - After
        STObject executed = after.executed(before);
        Amount takerGot = executed.get(Amount.TakerGets);

        // Only print trades that executed
        if (!takerGot.isZero()) {
            print("In {0} tx: {1}, Offer owner {2}, was paid: {3}, gave: {4} ",
                  tr.transactionType(), tr.hash, before.account(),
                    executed.get(Amount.TakerPays), takerGot);
        }
    }
}
