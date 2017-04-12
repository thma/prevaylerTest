package org.prevayler.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.Query;
import org.prevayler.Transaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;

/**
 * Created by thma on 13.06.2016.
 */
public class PrevaylerImpl<P> implements Prevayler<P> {

    private final P prevalentSystem;

    public PrevaylerImpl(final P newPrevalentSystem) {
        prevalentSystem = newPrevalentSystem;
        rebuildFromJournal();
    }

    private void rebuildFromJournal() {
        Path path = FileSystems.getDefault().getPath(".", prevalentSystem.getClass().getSimpleName() + ".journal");
        Gson gson = getGson();
        try {
            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
            reader.lines().forEach(line -> executeOnly(gson.fromJson(line, Transaction.class)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new InterfaceAdapter<Transaction<P>>())
                .create();
    }

    /**
     * Returns the object which holds direct or indirect references to all other Business Objects in the system.
     */
    @Override
    public P prevalentSystem() {
        return prevalentSystem;
    }

    /**
     * Executes the given Transaction on the prevalentSystem(). ALL operations that alter the observable state of the prevalentSystem() must be implemented as Transaction or TransactionWithQuery objects and must be executed using the Prevayler.execute() methods. This method synchronizes on the prevalentSystem() to execute the Transaction. It is therefore guaranteed that only one Transaction is executed at a time. This means the prevalentSystem() does not have to worry about concurrency issues among Transactions.
     * Implementations of this interface can log the given Transaction for crash or shutdown recovery, for example, or execute it remotely on replicas of the prevalentSystem() for fault-tolerance and load-balancing purposes.
     *
     * @param transaction
     * @see PrevaylerFactory
     */
    @Override
    public void execute(Transaction<? super P> transaction) {
        System.out.println(getGson().toJson(transaction));
        writeJournalEntry(transaction);
        executeOnly(transaction);
    }

    private void executeOnly(Transaction<? super P> transaction) {
        transaction.executeOn(prevalentSystem(), new Date());
    }

    private void writeJournalEntry(Transaction<? super P> transaction) {
        try {
            Path path = FileSystems.getDefault().getPath(".", prevalentSystem.getClass().getSimpleName() + ".journal");
            BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            writer.append(getGson().toJson(transaction, Transaction.class));
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Executes the given sensitiveQuery on the prevalentSystem(). A sensitiveQuery is a Query that would be affected by the concurrent execution of a Transaction or other sensitiveQuery. This method synchronizes on the prevalentSystem() to execute the sensitiveQuery. It is therefore guaranteed that no other Transaction or sensitiveQuery is executed at the same time.
     * <br> Robust Queries (queries that do not affect other operations and that are not affected by them) can be executed directly as plain old method calls on the prevalentSystem() without the need of being implemented as Query objects. Examples of Robust Queries are queries that read the value of a single field or historical queries such as: "What was this account's balance at mid-night?".
     *
     * @param sensitiveQuery
     * @return The result returned by the execution of the sensitiveQuery on the prevalentSystem().
     * @throws Exception The Exception thrown by the execution of the sensitiveQuery on the prevalentSystem().
     */
    @Override
    public <R> R execute(Query<? super P, R> sensitiveQuery) throws Exception {
        System.out.println(new Gson().toJson(sensitiveQuery));
        return sensitiveQuery.query(this.prevalentSystem(), new Date());
    }

    /**
     * Produces a complete serialized image of the underlying PrevalentSystem.
     * This will accelerate future system startups. Taking a snapshot once a day is enough for most applications.
     * This method synchronizes on the prevalentSystem() in order to take the snapshot. This means that transaction execution will be blocked while the snapshot is taken.
     *
     * @return The file to which the snapshot was written. This file should be left where it is, so that Prevayler can read it during startup. You can copy it to another location for backup purposes if desired.
     * @throws Exception if there is trouble writing to the snapshot file or serializing the prevalent system.
     */
    @Override
    public File takeSnapshot() throws Exception {
        return null;
    }

    /**
     * Closes any files or other system resources opened by this Prevayler.
     *
     * @throws java.io.IOException if there is trouble closing a file or some other system resource.
     */
    @Override
    public void close() throws IOException {

    }
}
