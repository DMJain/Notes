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
    ├── [Concept]Notes.md      ← Comprehensive notes
    ├── Main.java              ← Entry point
    ├── interfaces/            ← Contracts/interfaces
    │   ├── Interface1.java
    │   └── Interface2.java
    ├── model/                 ← Data classes (if needed)
    │   └── DataClass.java
    └── examples/              ← Demo implementations
        ├── Demo1.java
        └── Demo2.java
```

### 2. Notes.md Must Include
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

### 3. Each Java File Should
- Have ONE responsibility (Single Responsibility Principle)
- Include proper Javadoc comments
- Have a `runAll()` or similar entry method
- Be runnable/testable independently

---

## Confidence Scoring Criteria

| Score | Meaning | Action |
|-------|---------|--------|
| 0.9-1.0 | Excellent | Accept |
| 0.8-0.89 | Good | Accept |
| 0.7-0.79 | Needs improvement | Retry |
| < 0.7 | Poor | Major revision |

### Scoring Checklist

#### For Notes.md
- [ ] WHY question answered with history/motivation
- [ ] WHY NOT answered with anti-patterns
- [ ] When to use is clear
- [ ] ASCII diagrams present
- [ ] Before/After examples included
- [ ] Common gotchas documented
- [ ] Progressive from previous modules

#### For Java Files
- [ ] Proper package structure
- [ ] Each class has single responsibility
- [ ] Javadoc on public methods
- [ ] Code compiles without errors
- [ ] Demo runs successfully

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

### Phase 2: c[Y]_[concept1]
- [ ] Create directory structure
- [ ] Write [Concept]Notes.md
- [ ] Create interface files
- [ ] Create example files
- [ ] Create Main.java

### Phase 3: c[Z]_[concept2]
- [ ] [Same steps as Phase 2]

### Phase 4: Integration & Polish
- [ ] Ensure progressive narrative
- [ ] Add cross-references
- [ ] Compile and test all code
- [ ] Final review and scoring
```

---

## Quality Checklist (Final Review)

- [ ] All sub-tasks scored ≥ 0.8
- [ ] "Donkey-level clarity" achieved
- [ ] WHY, WHY NOT, When to Use answered
- [ ] Rich ASCII visuals included
- [ ] Progression from previous modules clear
- [ ] All Java code compiles
- [ ] All demos run successfully
- [ ] Package structure follows OOP principles
