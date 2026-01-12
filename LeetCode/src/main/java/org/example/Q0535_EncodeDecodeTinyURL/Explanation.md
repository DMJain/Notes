# Encode and Decode TinyURL - Explanation

> **Related Problems**: This is a companion to the System Design problem "Design TinyURL". Understanding this helps with [System Design interviews](https://leetcode.com/discuss/interview-question/system-design/124658/Design-a-URL-Shortener-(-TinyURL-)-System).

> **Honorable Mention**: *Random string generation* is another valid approach — generate random 6-char strings and check for collisions. Simpler but needs collision handling.

## Problem in Simple Words

Build a URL shortener like TinyURL:
- `encode(longUrl)` → returns a short URL
- `decode(shortUrl)` → returns the original long URL

**Example**: `"https://leetcode.com/problems/..."` → `"http://tiny.url/abc123"`

---

## Solution 1: Use URL as Key Directly ❌ (Trivial / Misses the Point)

### Approach
Just store the URL with a simple incrementing integer.

```java
Map<Integer, String> map = new HashMap<>();
int id = 0;

String encode(String url) {
    map.put(id, url);
    return "http://tiny.url/" + id++;
}

String decode(String shortUrl) {
    int id = extractId(shortUrl);
    return map.get(id);
}
```

### Why It's Not Ideal
- Short URLs are predictable: `.../1`, `.../2`, `.../3`
- Easy to enumerate all URLs (security risk)
- Integer IDs can get very long for billions of URLs

> 💭 **We need unpredictable, compact short codes. What if we encoded the counter in a higher base like Base62 (a-z, A-Z, 0-9)?**

---

## Solution 2: Counter + Base62 Encoding ✅ (Optimal for This Problem)

### The Connection 🔗
Let's trace our thinking:
- **Simple counter** was predictable and could get long
- **What we need**: compact, less predictable codes → **Base62 encoding!**

### The Key Insight 💡
Convert the counter to Base62:
- Base10: `0-9` (10 chars)
- Base62: `0-9, a-z, A-Z` (62 chars)

With 6 Base62 characters: `62^6 = 56+ billion` unique URLs!

### The Algorithm

```
encode(longUrl):
    1. If already encoded, return cached short URL
    2. Increment counter
    3. Convert counter to Base62 string
    4. Store mapping: shortCode ↔ longUrl
    5. Return domain + shortCode

decode(shortUrl):
    1. Extract shortCode from URL
    2. Look up in map
    3. Return original URL
```

### Why Two HashMaps?

```
longToShort: "https://leetcode.com/..." → "abc123"
shortToLong: "abc123" → "https://leetcode.com/..."

- encode() checks longToShort first → avoids duplicates
- decode() uses shortToLong → O(1) lookup
```

---

## Step-by-Step Walkthrough

### Encode

```
Input: "https://leetcode.com/problems/design-tinyurl"

1. Check longToShort: not found (new URL)

2. counter.incrementAndGet() → 1001

3. encodeBase62(1001):
   1001 % 62 = 9  → '9'
   1001 / 62 = 16
   16 % 62 = 16   → 'g'
   16 / 62 = 0    → done
   Reverse: "g9"

4. Store mappings:
   shortToLong["g9"] = "https://leetcode.com/..."
   longToShort["https://leetcode.com/..."] = "g9"

5. Return: "http://tiny.url/g9"
```

### Decode

```
Input: "http://tiny.url/g9"

1. Extract shortCode: "g9"

2. Look up shortToLong["g9"] → "https://leetcode.com/..."

3. Return: "https://leetcode.com/problems/design-tinyurl"
```

---

## Visual: Base62 Encoding

```
Decimal → Base62

Number: 1001

Step 1: 1001 % 62 = 9  → BASE62[9] = '9'
        1001 / 62 = 16

Step 2: 16 % 62 = 16   → BASE62[16] = 'g'
        16 / 62 = 0

Result: reverse("9g") = "g9"

BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
          0         10        20        30        40        50      61
```

---

## Alternative Approaches

### Random String Generation

```java
String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

String encode(String url) {
    String code;
    do {
        code = generateRandom(6);  // 6 random chars
    } while (shortToLong.containsKey(code));  // Collision check
    
    shortToLong.put(code, url);
    return DOMAIN + code;
}
```

**Pros**: More "random" looking
**Cons**: Need collision handling, can't guarantee uniqueness without checking

### Hashing (MD5/SHA)

```java
String encode(String url) {
    String hash = md5(url).substring(0, 6);  // Take first 6 chars
    shortToLong.put(hash, url);
    return DOMAIN + hash;
}
```

**Pros**: Deterministic for same URL
**Cons**: Hash collisions possible, same 6 chars for different URLs

---

## Design Considerations (System Design Perspective)

| Aspect | Counter + Base62 | Random | Hash |
|--------|-----------------|--------|------|
| Uniqueness | Guaranteed | Need collision check | Possible collisions |
| Predictability | Sequential (can shuffle) | Random | Deterministic |
| Duplicate URLs | Use `longToShort` map | Need separate check | Same hash = same short |
| Scalability | ∞ (just increase counter) | 62^6 = 56B | Hash length limits |

---

## Complexity Analysis

| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| `encode()` | O(log n) | O(1) | Base62 conversion is O(log₆₂ n) |
| `decode()` | O(1) | O(1) | HashMap lookup |
| **Total Space** | — | O(n) | Store n URL mappings |

---

## Key Takeaways

1. **Base62** = Compact encoding using `0-9a-zA-Z` (62 chars)
2. **Two HashMaps** = Bidirectional lookup for encode/decode
3. **Counter-based** = Guaranteed unique, no collision handling needed
4. **AtomicLong** = Thread-safe counter for concurrent requests
5. **Real TinyURL** = Would also need database persistence, distributed counters, analytics

---

## The Journey (TL;DR)

```
🐢 Simple Counter: Predictable, long IDs → NOT IDEAL
         ↓
💡 "Need compact, less predictable codes..."
         ↓
✅ Counter + Base62: 6 chars = 56B URLs, unique, O(1) operations
```
