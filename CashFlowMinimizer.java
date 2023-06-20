import java.util.*;

class CashFlowMinimizer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of people involved:");
        int numPeople = scanner.nextInt();

        List<String> people = new ArrayList<>();
        for (int i = 0; i < numPeople; i++) {
            System.out.println("Person " + (i + 1) + ":");
            String person = scanner.next();
            people.add(person);
        }

        System.out.println("Enter the total number of transactions:");
        int numTransactions = scanner.nextInt();

        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < numTransactions; i++) {
            System.out.println("Transaction " + (i + 1) + ":");

            System.out.println("Who paid and how much:");
            String payer = scanner.next();
            int amount = scanner.nextInt();

            System.out.println("Participants:");
            List<String> participants = new ArrayList<>();
            String participant;
            while (!(participant = scanner.next()).equals("end")) {
                participants.add(participant);
            }

            Transaction transaction = new Transaction(payer, amount, participants);
            transactions.add(transaction);
        }

        minimizeCashFlow(people, transactions);
    }

    public static void minimizeCashFlow(List<String> people, List<Transaction> transactions) {
        Map<String, Integer> netAmounts = new HashMap<>();

        for (String person : people) {
            netAmounts.put(person, 0);
        }

        for (Transaction transaction : transactions) {
            String payer = transaction.getPayer();
            int amount = transaction.getAmount();
            List<String> participants = transaction.getParticipants();

            netAmounts.put(payer, netAmounts.get(payer) - amount);

            int splitAmount = amount / participants.size();
            for (String participant : participants) {
                netAmounts.put(participant, netAmounts.get(participant) + splitAmount);
            }
        }

        List<String> positiveDebtors = new ArrayList<>();
        List<String> negativeDebtors = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : netAmounts.entrySet()) {
            String person = entry.getKey();
            int netAmount = entry.getValue();

            if (netAmount < 0) {
                negativeDebtors.add(person);
            } else if (netAmount > 0) {
                positiveDebtors.add(person);
            }
        }

        int negativeIndex = 0;
        int positiveIndex = 0;

        while (negativeIndex < negativeDebtors.size() && positiveIndex < positiveDebtors.size()) {
            String debtor = negativeDebtors.get(negativeIndex);
            String creditor = positiveDebtors.get(positiveIndex);

            int debt = netAmounts.get(debtor);
            int credit = netAmounts.get(creditor);

            int amount = Math.min(-debt, credit);
            netAmounts.put(debtor, debt + amount);
            netAmounts.put(creditor, credit - amount);

            System.out.println(debtor + " pays " + creditor + " an amount of " + amount);

            if (debt + amount == 0) {
                negativeIndex++;
            }
            if (credit - amount == 0) {
                positiveIndex++;
            }
        }
    }
}

class Transaction {
    private String payer;
    private int amount;
    private List<String> participants;

    public Transaction(String payer, int amount, List<String> participants) {
        this.payer = payer;
        this.amount = amount;
        this.participants = participants;
    }

    public String getPayer() {
        return payer;
    }

    public int getAmount() {
        return amount;
    }

    public List<String> getParticipants() {
        return participants;
    }
}
