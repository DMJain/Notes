---
description: How to implement LLD/HLD concept notes with score-driven analysis and proper structure
---

# LLD/HLD Concept Implementation Workflow

Use this prompt template to implement comprehensive notes for any LLD or HLD concept.

---

## The Prompt Template

```
Let's implement in LLD @[source-file.md]
p[X]_[module_name], c[Y] and c[Z].

Explain it in a way with Donkey-level clarity for reader has zero context, 
Visuals, detailed explanation. Answer WHY, WHY NOT, when to use, what and how questions.

Remember it's a progression from [previous modules]. 
So users are expected to have some understanding and we are writing progressive notes.

Requirements:
- Divide the task into many sub-tasks
- Give confidence score (0.0 to 1.0) for each sub-task
- When sub-task returns with score < 0.8, retry and improve it
- At the end, join the sub-tasks together in a meaningful way
- Analyze the final task and give confidence score
- If final confidence is < 0.8, fix the joining of sub-tasks
```

---

## Expected Output Structure

### 1. Directory Structure

```
p[X]_[module_name]/
└── c[Y]_[concept]/
    │
    ├── [Concept]Notes.md              ← Comprehensive documentation
    ├── Main.java                      ← Orchestration entry point
    │
    ├── contracts/                     ← LAYER 1: Abstractions (Interfaces/Abstract Classes)
    │   ├── I[Concept].java            ← Primary interface defining the contract
    │   ├── I[SubConcept].java         ← Supporting interfaces
    │   └── Abstract[Concept].java     ← Shared abstract base (if applicable)
    │
    ├── impl/                          ← LAYER 2: Concrete Implementations
    │   ├── Default[Concept].java      ← Default/standard implementation
    │   ├── [Variant1][Concept].java   ← Alternative implementation #1
    │   └── [Variant2][Concept].java   ← Alternative implementation #2
    │
    ├── model/                         ← LAYER 3: Domain Models / DTOs
    │   ├── [Entity].java              ← Core domain entity
    │   ├── [ValueObject].java         ← Immutable value objects
    │   └── enums/                     ← Enum types
    │       └── [ConceptType].java
    │
    ├── service/                       ← LAYER 4: Business Logic / Orchestration
    │   ├── [Concept]Service.java      ← Service coordinating implementations
    │   └── [Concept]Factory.java      ← Factory for creating instances (if needed)
    │
    ├── exception/                     ← LAYER 5: Custom Exceptions
    │   ├── [Concept]Exception.java    ← Base exception for this concept
    │   └── Invalid[X]Exception.java   ← Specific exception types
    │
    ├── util/                          ← LAYER 6: Utilities / Helpers
    │   ├── [Concept]Utils.java        ← Static utility methods
    │   └── [Concept]Constants.java    ← Constants and configuration
    │
    └── runner/                        ← LAYER 7: Demonstration & Testing
        ├── [Concept]Runner.java       ← Primary demo runner
        ├── scenarios/                 ← Scenario-based demonstrations
        │   ├── BasicScenario.java     ← Basic usage scenario
        │   ├── AdvancedScenario.java  ← Complex use cases
        │   └── EdgeCaseScenario.java  ← Edge cases and gotchas
        └── comparison/                ← Comparative demonstrations
            └── [Before]Vs[After]Demo.java  ← Traditional vs Modern approach
```

### 1.1 Structure Principles

| Principle | Application |
|-----------|-------------|
| **Separation of Concerns** | Each layer has distinct responsibility |
| **Dependency Inversion** | High-level modules depend on abstractions (`contracts/`) |
| **Single Responsibility** | One class = One purpose |
| **Open/Closed** | Extend via new `impl/` classes, not modification |
| **Interface Segregation** | Small, focused interfaces in `contracts/` |

### 1.2 When to Use Each Layer

| Layer | When to Include | Skip If... |
|-------|-----------------|------------|
| `contracts/` | Always | Never skip |
| `impl/` | Always | Never skip |
| `model/` | Working with domain objects | Concept is purely behavioral |
| `service/` | Complex orchestration needed | Simple direct usage |
| `exception/` | Custom error handling needed | Standard exceptions suffice |
| `util/` | Shared helper logic exists | Logic is self-contained |
| `runner/` | Always (demonstrations) | Never skip |

### 2. Notes.md Must Include In Details
| Section | Content |
|---------|---------|
| Prerequisites | Link to foundational concepts |
| What You'll Learn | WHAT, WHY, WHEN, HOW |
| History/Motivation | WHY this concept exists |
| Visual Diagrams | ASCII art for understanding |
| Before/After | Traditional vs modern approach |
| When to Use | ✅ Good use cases |
| When NOT to Use | ❌ Anti-patterns |
| Common Gotchas | Mistakes and how to avoid |
| Summary | Quick reference |

### 3. Java File Requirements by Layer

#### `contracts/` Layer
- Use `I` prefix for interfaces (e.g., `ICounter`, `IThreadPool`)
- Include Javadoc with `@param`, `@return`, `@throws`
- Define only method signatures, no implementation
- Keep interfaces focused (Interface Segregation Principle)

#### `impl/` Layer
- Class name reflects implementation strategy (e.g., `SynchronizedCounter`, `ReentrantLockCounter`)
- Include `@Override` annotations
- Document thread-safety guarantees where applicable
- Each implementation should be independently testable

#### `model/` Layer
- Use immutable objects where possible (`final` fields)
- Implement `equals()`, `hashCode()`, `toString()` consistently
- DTOs should contain no business logic
- Enums should be placed in `model/enums/`

#### `service/` Layer
- Coordinates between multiple implementations
- Factory classes create appropriate implementations
- Handle cross-cutting concerns (logging, validation)

#### `runner/` Layer
- `[Concept]Runner.java`: Entry point with `public static void main()`
- `scenarios/`: Each scenario class has `public void execute()` method
- `comparison/`: Side-by-side demonstrations of approaches
- Output should be clear, formatted, and educational

#### Main.java (Root)
```java
public class Main {
    public static void main(String[] args) {
        System.out.println("=== [Concept] Module ===");
        [Concept]Runner.runAllScenarios();
    }
}
```

---

## Confidence Scoring Criteria

| Score | Meaning | Action |
|-------|---------|--------|
| 0.9-1.0 | Excellent | Accept |
| 0.8-0.89 | Good | Retry-Minor revision |
| < 0.8 | Poor | Major revision |

### Scoring Checklist

#### For Notes.md
- [ ] WHY question answered with history/motivation
- [ ] WHY NOT answered with anti-patterns
- [ ] When to use is clear
- [ ] ASCII diagrams present
- [ ] Before/After examples included
- [ ] Common gotchas documented
- [ ] Progressive from previous modules
- [ ] Is notes in Details
- [ ] Is explanation in Donkey level? while using technical termanology?

#### For Java Files (by Layer)

**`contracts/` Layer**
- [ ] Interface names use `I` prefix
- [ ] Javadoc with `@param`, `@return`, `@throws`
- [ ] No implementation code in interfaces

**`impl/` Layer**
- [ ] `@Override` annotations on all overridden methods
- [ ] Class names reflect implementation strategy
- [ ] Each class independently testable

**`runner/` Layer**
- [ ] Scenarios demonstrate distinct use cases
- [ ] Output is educational and well-formatted
- [ ] Comparison demos show before/after approaches

**All Layers**
- [ ] Code compiles without errors
- [ ] Single Responsibility Principle followed
- [ ] Proper dependency direction maintained

---

## Example Prompts

### For Concurrency (p2)
```
Let's implement in LLD @[LLD-Concurrency.md]
p2_concurrency, c1 and c2.

Explain with Donkey-level clarity, visuals, detailed explanation.
Answer WHY, WHY NOT, when to use, what and how.

It's a progression from p1_oops. Users have understanding of 
classes, interfaces, inheritance.

[Include standard scoring requirements]
```

### For Design Patterns (p4)
```
Let's implement in LLD @[LLD-DesignPatterns.md]
p4_design_patterns, c1_singleton and c2_factory.

Explain with Donkey-level clarity, visuals, detailed explanation.
Answer WHY, WHY NOT, when to use, what and how.

It's a progression from p1_oops and p3_advanced_concepts.
Users understand interfaces, lambdas, and streams.

[Include standard scoring requirements]
```

### For HLD Concepts
```
Let's implement HLD notes for @[HLD-SystemDesign.md]
c1_scalability and c2_load_balancing.

Explain with Donkey-level clarity for someone with zero context.
Include visuals (ASCII diagrams), detailed explanation.
Answer WHY, WHY NOT, when to use.

[Include standard scoring requirements]
```

---

## Sub-Task Template

```
## Sub-Tasks Breakdown

### Phase 1: Setup & Planning
- [ ] Examine existing project structure
- [ ] Study source material
- [ ] Understand existing note patterns
- [ ] Create implementation plan

### Phase 2: c[Y]_[concept1] - Core Structure
- [ ] Create directory structure (all 7 layers)
- [ ] Write [Concept]Notes.md
- [ ] Define contracts/ (interfaces & abstract classes)
- [ ] Implement impl/ (concrete implementations)
- [ ] Create model/ (if domain objects needed)
- [ ] Setup runner/ with scenarios

### Phase 3: c[Y]_[concept1] - Supporting Layers (if needed)
- [ ] Add service/ layer for orchestration
- [ ] Create exception/ for custom exceptions
- [ ] Add util/ for helpers and constants
- [ ] Wire Main.java to runner

### Phase 4: c[Z]_[concept2]
- [ ] [Repeat Phase 2 & 3 steps]

### Phase 5: Integration & Polish
- [ ] Ensure progressive narrative across concepts
- [ ] Add cross-references in Notes.md
- [ ] Verify all layers follow SOLID principles
- [ ] Compile and test all code
- [ ] Final review and scoring
```

---

## Quality Checklist (Final Review)

### Notes Quality
- [ ] "Donkey-level clarity" achieved
- [ ] WHY, WHY NOT, When to Use answered
- [ ] Rich ASCII visuals included
- [ ] Progression from previous modules clear

### Structure Quality  
- [ ] All required layers present (`contracts/`, `impl/`, `runner/`)
- [ ] Optional layers included only when necessary
- [ ] Each class follows Single Responsibility Principle
- [ ] Dependency direction: `runner/` → `service/` → `impl/` → `contracts/`

### Code Quality
- [ ] All Java code compiles
- [ ] All demos run successfully
- [ ] Javadoc on all public APIs
- [ ] `@Override` annotations present

### Scoring
- [ ] All sub-tasks scored ≥ 0.8
- [ ] Final integration score ≥ 0.8