# Poker Hand Evaluator – Test Checklist

A reference checklist to ensure thorough coverage of poker hand evaluation logic.

---

## ✅ Coverage Checklist

### 1. Hand Category Detection

* [ ] High Card (no pairs, no straights, no flushes)
* [ ] One Pair (exactly one pair, no better hand)
* [ ] Two Pair (exactly two distinct pairs, not three of a kind / full house)
* [ ] Three of a Kind (exactly three, not full house)
* [ ] Straight (all suits, Ace both low `A-2-3-4-5` and high `10-J-Q-K-A`)
* [ ] Flush (all suits, no straight)
* [ ] Full House (exactly 3 + 2)
* [ ] Four of a Kind
* [ ] Straight Flush (non-royal)
* [ ] Royal Flush (`10-J-Q-K-A` same suit)

---

### 2. Comparison Within Same Category

* [ ] Pair vs. Pair (higher pair wins; kicker breaks ties)
* [ ] Two Pair vs. Two Pair (highest pair wins; then lower pair; then kicker)
* [ ] Three of a Kind vs. Three of a Kind (higher trips wins; kicker if needed)
* [ ] Straight vs. Straight (`A-2-3-4-5` vs. `2-3-4-5-6`; highest card decides)
* [ ] Flush vs. Flush (compare top card down to kicker)
* [ ] Full House vs. Full House (higher trips wins; if equal, compare pair)
* [ ] Four of a Kind vs. Four of a Kind (compare quads; then kicker)
* [ ] Straight Flush vs. Straight Flush (highest card wins; Ace-low vs Ace-high edge case)

---

### 3. Cross-Category Comparisons

* [ ] Verify correct hierarchy (e.g., Flush beats Straight, Full House beats Flush)
* [ ] Ensure no misclassification (e.g., Full House not treated as Two Pair)

---

### 4. Edge & Special Cases

* [ ] Duplicate cards (should not occur; defensive handling if needed)
* [ ] Multiple players tie completely (split pot logic, if relevant)
* [ ] Large inputs (stress test with all 52 cards)
* [ ] Randomized hands (fuzz testing to detect misclassifications)

---

### 5. Maintainability / Sanity Checks

* [ ] Tests have **clear, descriptive names** (e.g., `testFullHouseBeatsFlush`)
* [ ] No “magic numbers” or unclear hands — use helpers (`cards("Ah", "Ad", "As", "Ks", "Kd")`)
* [ ] Assertion messages describe **what failed** (e.g., `"Expected full house to beat flush"`)

---
