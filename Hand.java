import java.util.*;

/**
 * A poker hand is a list of cards, which can be of some "kind" (pair, straight, etc.)
 * Author: kkaddoura
 */
public class Hand implements Comparable<Hand> {

    public enum Kind {HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, 
        FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH}

    private final List<Card> cards;

    /**
     * Create a hand from a string containing all cards (e,g, "5C TD AH QS 2D")
     */
    public Hand(String c) {
        cards = new ArrayList<>();
        String[] temp = c.split(" ");

        for (int i = 0; i < temp.length; i++) {
            cards.add(new Card(temp[i]));
        }
    }
    
    /**
     * @returns true if the hand has n cards of the same rank
	 * e.g., "TD TC TH 7C 7D" returns True for n=2 and n=3, and False for n=1 and n=4
     */
    protected boolean hasNKind(int n) {
        Map<Card.Rank, Integer> rankCounter = new HashMap<>();
        for (Card c : cards) {
            Integer count = rankCounter.get(c.getRank());
            rankCounter.put(c.getRank(), count == null ? 1 : count + 1);
        }

        for (HashMap.Entry<Card.Rank, Integer> entry : rankCounter.entrySet()) {
            if (entry.getValue() == n) {
                return true;
            }
        }
        return false;
    }
    
    /**
	 * Optional: you may skip this one. If so, just make it return False
     * @returns true if the hand has two pairs
     */
    public boolean isTwoPair() {
        Map<Card.Rank, Integer> rankCounter = new HashMap<>();
        for (Card c : cards) {
            Integer count = rankCounter.get(c.getRank());
            rankCounter.put(c.getRank(), count == null ? 1 : count + 1);
        }

        int count = 0;
        for (HashMap.Entry<Card.Rank, Integer> entry : rankCounter.entrySet()) {
            if (entry.getValue() == 2) {
                count++;
            }
        }
        return count == 2;
    }   
    
    /**
     * @returns true if the hand is a straight 
     */
    public boolean isStraight() {
        List<Integer> rankOrder = new ArrayList<>();
        List<Integer> aceFirst = new ArrayList<>(Arrays.asList(0,1,2,3,12)); // AX 2X 3X 4X 5X

        for (Card c : cards) {
            rankOrder.add(c.getRank().ordinal());
        }
        Collections.sort(rankOrder);

        int i = rankOrder.size() - 1;
        while (i > 0) {
            if (rankOrder.get(i) == rankOrder.get(i - 1) + 1) {
                i--;
            }
            else {
                return rankOrder.equals(aceFirst);
            }
        }
        return true;
    }
    
    /**
     * @returns true if the hand is a flush
     */
    public boolean isFlush() {
        Map<Card.Suit, Integer> suitCounter = new HashMap<>();
        for (Card c : cards) {
            Integer count = suitCounter.get(c.getSuit());
            suitCounter.put(c.getSuit(), count == null ? 1 : count + 1);
        }

        for (HashMap.Entry<Card.Suit, Integer> entry : suitCounter.entrySet()) {
            if (entry.getValue() == 5) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Hand h) {
        return this.kind().compareTo(h.kind());
    }
    
    /**
	 * This method is already implemented and could be useful! 
     * @returns the "kind" of the hand: flush, full house, etc.
     */
    public Kind kind() {
        if (isStraight() && isFlush()) return Kind.STRAIGHT_FLUSH;
        else if (hasNKind(4)) return Kind.FOUR_OF_A_KIND; 
        else if (hasNKind(3) && hasNKind(2)) return Kind.FULL_HOUSE;
        else if (isFlush()) return Kind.FLUSH;
        else if (isStraight()) return Kind.STRAIGHT;
        else if (hasNKind(3)) return Kind.THREE_OF_A_KIND;
        else if (isTwoPair()) return Kind.TWO_PAIR;
        else if (hasNKind(2)) return Kind.PAIR; 
        else return Kind.HIGH_CARD;
    }

    public static void main(String[] args) {
        Hand h1 = new Hand("TD TC TH 7C 7D");
        System.out.println("Test 1:");
        System.out.println("Kind: " + h1.kind() + ", hasNKind: " + h1.hasNKind(2));

        Hand h2 = new Hand("6C 6D TH TS AD");
        System.out.println("Test 2:");
        System.out.println("Kind: " + h2.kind() + ", isTwoPair: " + h2.isTwoPair());

        Hand h3 = new Hand("5C 2D 3H AS 4D");
        System.out.println("Test 3:");
        System.out.println("Kind: " + h3.kind() + ", isStraight: " + h3.isStraight());

        Hand h4 = new Hand("TD 2D 3D AD 4D");
        System.out.println("Test 4:");
        System.out.println("Kind: " + h4.kind() + ", isFlush: " + h4.isFlush());

        System.out.println("Test 5:");
        System.out.println(h4.compareTo(h3));
    }

}
