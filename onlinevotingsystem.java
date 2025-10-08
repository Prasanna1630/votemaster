import java.util.*;

class Candidate {
    String name;
    int votes = 0;

    Candidate(String name) {
        this.name = name;
    }
}

class Voter {
    String name;
    String pin;
    boolean voted = false;

    Voter(String name, String pin) {
        this.name = name;
        this.pin = pin;
    }
}

public class onlinevotingsystem {
    static Scanner sc = new Scanner(System.in);
    static Map<String, Candidate> candidates = new LinkedHashMap<>();
    static Map<String, Voter> voters = new LinkedHashMap<>();
    static String electionId;

    public static void main(String[] args) {
        generateElectionId();
        System.out.println("===== SmartVote System =====");
        System.out.println("Election ID: " + electionId);

        // Predefined candidates
        candidates.put("1", new Candidate("Arjun Reddy"));
        candidates.put("2", new Candidate("Chitti Babu"));
        candidates.put("3", new Candidate("Radhika"));
        candidates.put("4", new Candidate("Bapta La Durga"));
        candidates.put("5", new Candidate("Other"));

        while (true) {
            System.out.println("\n1. Register Voter");
            System.out.println("2. Add Candidate (Optional)");
            System.out.println("3. Vote");
            System.out.println("4. Show Results");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    registerVoter();
                    break;
                case 2:
                    addCandidate();
                    break;
                case 3:
                    castVote();
                    break;
                case 4:
                    showResults();
                    break;
                case 5:
                    System.out.println("\nThank you for using SmartVote!");
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    static void generateElectionId() {
        electionId = "EL" + (1000 + new Random().nextInt(9000));
    }

    static void addCandidate() {
        System.out.print("Enter Candidate Name: ");
        String name = sc.nextLine().trim();
        if (candidates.containsKey(name.toLowerCase())) {
            System.out.println("Candidate already exists!");
            return;
        }
        candidates.put(name.toLowerCase(), new Candidate(name));
        System.out.println("Candidate added: " + name);
    }

    static void registerVoter() {
        System.out.print("Enter Voter Name: ");
        String name = sc.nextLine().trim();
        if (voters.containsKey(name.toLowerCase())) {
            System.out.println("Voter already registered!");
            return;
        }
        String pin = generatePin();
        voters.put(name.toLowerCase(), new Voter(name, pin));
        System.out.println("Voter registered successfully!");
        System.out.println("Your Secret PIN: " + pin);
    }

    static String generatePin() {
        Random r = new Random();
        return String.format("%04d", r.nextInt(10000));
    }

    static void castVote() {
        System.out.print("Enter Voter Name: ");
        String name = sc.nextLine().trim().toLowerCase();

        Voter voter = voters.get(name);
        if (voter == null) {
            System.out.println("You are not registered!");
            return;
        }
        if (voter.voted) {
            System.out.println("You have already voted!");
            return;
        }

        System.out.print("Enter your Secret PIN: ");
        String pin = sc.nextLine().trim();
        if (!voter.pin.equals(pin)) {
            System.out.println("Incorrect PIN!");
            return;
        }

        // Show predefined candidates
        System.out.println("\nCandidates:");
        for (Map.Entry<String, Candidate> entry : candidates.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().name);
        }

        System.out.print("Enter candidate number to vote: ");
        String voteNum = sc.nextLine().trim();

        Candidate c = candidates.get(voteNum);
        if (c == null) {
            System.out.println("Candidate not found!");
            return;
        }

        c.votes++;
        voter.voted = true;
        System.out.println("Vote cast successfully for " + c.name);

        showTopCandidate();
    }

    static void showTopCandidate() {
        Candidate top = null;
        for (Candidate c : candidates.values()) {
            if (top == null || c.votes > top.votes) top = c;
        }
        if (top != null) {
            System.out.println("Current Leader: " + top.name + " (" + top.votes + " votes)");
        }
    }

    static void showResults() {
        System.out.println("\n===== Final Results =====");
        int totalVotes = candidates.values().stream().mapToInt(c -> c.votes).sum();

        Candidate winner = null;
        for (Candidate c : candidates.values()) {
            double percent = totalVotes == 0 ? 0 : (c.votes * 100.0 / totalVotes);
            System.out.printf("%-15s : %2d votes (%.1f%%)%n", c.name, c.votes, percent);
            if (winner == null || c.votes > winner.votes) winner = c;
        }

        if (winner != null && totalVotes > 0)
            System.out.println("\nWinner: " + winner.name);
        else
            System.out.println("\nNo votes cast yet!");
    }
}
