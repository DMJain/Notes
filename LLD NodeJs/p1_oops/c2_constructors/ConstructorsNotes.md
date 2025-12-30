# Constructors & Memory Model Notes (JavaScript)

## Constructors

In JavaScript, classes have a single `constructor` method. To simulate multiple constructors, use default parameters or type checking.

### Patterns

1. **Default Constructor Pattern**: Use default parameters
2. **Parameterized Constructor**: Pass arguments to constructor
3. **Copy Constructor Pattern**: Check if argument is an instance of the class

```javascript
class Student {
    constructor(nameOrStudent = "Unknown", age = 18, scores = [0, 0, 0]) {
        if (nameOrStudent instanceof Student) {
            // Copy constructor behavior
            this.name = nameOrStudent.name;
            this.examScores = [...nameOrStudent.examScores]; // Deep copy
        } else {
            this.name = nameOrStudent;
            this.age = age;
            this.examScores = scores;
        }
    }
}
```

## Shallow Copy vs Deep Copy

### Shallow Copy
- Copies **references** for objects/arrays.
- **Result**: Both objects share the same nested objects. Modifying one affects the other.

```javascript
const s2 = {};
s2.examScores = s1.examScores; // Same array reference!
```

### Deep Copy Methods in JavaScript
1. **Spread Operator** (1 level deep): `[...array]` or `{...object}`
2. **JSON.parse/stringify** (deep, but loses methods/functions): `JSON.parse(JSON.stringify(obj))`
3. **structuredClone()** (modern browsers/Node 17+): `structuredClone(obj)`
4. **Manual copying**: Loop through and copy each property

```javascript
// Spread operator (arrays)
this.examScores = [...other.examScores];

// Deep clone objects
const copy = structuredClone(original);
```

## JavaScript Memory Model

- **Primitive Types** (number, string, boolean, null, undefined, symbol, bigint): Stored **by value**
- **Reference Types** (objects, arrays, functions): Stored **by reference** (address in heap)

### Pass-by-Value of Reference
JavaScript always passes the **value** of the variable. For objects, this value is the **reference**.

```javascript
function modifyReference(s) {
    s = new Student("New");  // Changes local copy of reference
}
// Original unchanged!

function modifyObject(s) {
    s.name = "Changed";  // Follows reference to actual object
}
// Original changed!
```

## Reference Assignment vs Cloning

| Operation | Code | Result |
| :--- | :--- | :--- |
| Reference Copy | `let s2 = s1` | Same object |
| Shallow Copy | `{...s1}` or `Object.assign({}, s1)` | New object, shared nested refs |
| Deep Copy | `structuredClone(s1)` | Completely independent |
