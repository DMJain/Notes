# Constructors & Memory Model Notes

## Constructors
A constructor is a special method used to initialize objects.

### Types
1.  **Default Constructor**: No arguments. Initializes fields to default values (null, 0, false).
2.  **Parameterized Constructor**: Accepts arguments to set specific values.
3.  **Copy Constructor**: Creates a new object by copying values from another object.

## Shallow Copy vs Deep Copy

### Shallow Copy
- Copies the **values** of fields.
- For references (Arrays, Objects), it copies the **memory address**.
- **Result**: Both objects point to the SAME internal object/array. Modifying one affects the other.

### Deep Copy
- Creates a **new instance** for referenced objects/arrays.
- Copies the **actual data**, not just the address.
- **Result**: Objects are independent. Modifying one DOES NOT affect the other.

## Java Memory Model & Pass-by-Value

- **Stack Memory**: Stores method calls and **local variables** (primitives and **references**).
- **Heap Memory**: Stores **Objects**.
- **Pass-by-Value**: Java always passes the **value** of the variable. For objects, this value is the **reference** (address).
    - Reassigning the reference inside a method (`s = new Student()`) does **NOT** affect the caller.
    - Modifying the object via the reference (`s.name = "..."`) **DOES** affect the caller.
