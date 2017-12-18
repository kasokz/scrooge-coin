package scrooge;

import java.util.HashSet;

import scrooge.Transaction.Input;
import scrooge.Transaction.Output;

public class TxHandler {

    private UTXOPool utxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        this.utxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        double inputSum = 0;
        double outputSum = 0;
        if (tx.getInputs().size() != new HashSet<Input>(tx.getInputs()).size()) {
            return false;
        }
        for (Output txOutput : tx.getOutputs()) {
            if (txOutput.value < 0) {
                return false;
            }
            outputSum += txOutput.value;
        }
        for (int inputIndex = 0; inputIndex < tx.getInputs().size(); inputIndex++) {
            Input txInput = tx.getInput(inputIndex);
            if (this.utxoPool.contains(new UTXO(txInput.prevTxHash, txInput.outputIndex))) {
                Output txOutput = this.utxoPool.getTxOutput(new UTXO(txInput.prevTxHash, txInput.outputIndex));
                if (Crypto.verifySignature(txOutput.address, tx.getRawDataToSign(inputIndex), txInput.signature)) {
                    inputSum += txOutput.value;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (inputSum != outputSum) {
            return false;
        }
        return true;
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
        return new Transaction[] {};
    }

}
