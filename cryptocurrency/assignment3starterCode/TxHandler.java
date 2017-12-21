import java.nio.ByteBuffer;
import java.util.*;

public class TxHandler {

    private UTXOPool utxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        this.utxoPool = utxoPool;
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     * values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        Set<UTXO> set = new HashSet<>();
        double totalOut = 0;
        double totalIn = 0;
        for (int i = 0; i < tx.numInputs(); i++) {
            Transaction.Input in = tx.getInput(i);
            UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);
            Transaction.Output output = utxoPool.getTxOutput(utxo);
            if (!utxoPool.contains(utxo)) {
                return false;
            }
            if (!Crypto.verifySignature(output.address, tx.getRawDataToSign(i), in.signature)) {
                return false;
            }
            if (set.contains(utxo)) {
                return false;
            }
            set.add(utxo);
            totalOut += output.value;
        }
        for (Transaction.Output out : tx.getOutputs()) {
            if (out.value < 0) {
                return false;
            }
            totalIn += out.value;
        }
        return totalOut >= totalIn;
    }

    private static byte[] toByteArray(double value) {
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        Set<Transaction> result = new HashSet<>();
        for (Transaction trans : possibleTxs) {
            if (isValidTx(trans)) {
                result.add(trans);
                for (Transaction.Input in : trans.getInputs()) {
                    UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);
                    utxoPool.removeUTXO(utxo);
                }
                for (int i = 0; i < trans.numOutputs(); i++) {
                    Transaction.Output out = trans.getOutput(i);
                    UTXO utxo = new UTXO(trans.getHash(), i);
                    utxoPool.addUTXO(utxo, out);
                }
            }
        }
        return result.toArray(new Transaction[result.size()]);
    }

    public UTXOPool getUTXOPool() {
        return utxoPool;
    }

}
