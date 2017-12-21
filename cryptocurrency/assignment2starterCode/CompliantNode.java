import java.util.HashSet;
import java.util.Set;

public class CompliantNode implements Node {


    private boolean[] followees;

    private Set<Transaction> pendingTransactions;

    private boolean[] blackList;

    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        // WHAT ??????!!!!!!
    }

    public void setFollowees(boolean[] followees) {
        this.followees = followees;
        this.blackList = new boolean[followees.length];
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        this.pendingTransactions = pendingTransactions;
    }

    public Set<Transaction> sendToFollowers() {
        Set<Transaction> toSend = new HashSet<>(pendingTransactions);
        pendingTransactions.clear();
        return toSend;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        Set<Integer> senders = new HashSet();
        for (Candidate c : candidates) {
            senders.add(c.sender);
        }
        for (int i = 0; i < followees.length; i++) {
            if (followees[i] && !senders.contains(i))
                blackList[i] = true;
        }
        for (Candidate c : candidates) {
            if (!blackList[c.sender]) {
                pendingTransactions.add(c.tx);
            }
        }
    }
}