# DataJets

DataJets is an object-relational mapping framework for Java. It was designed to handle the repetitive CRUD operations that developers write over and over again, bridging the impedance mismatch between object and relational paradigms.

I built DataJets in 2003 and released it as open source on SourceForge. It was hosted at datajets.com, later moved under the anderix.com umbrella, and went through several package renames along the way (`com.datajets` to `com.anderix` to `anderix`). Three versions were publicly released (1.1, 1.2, 1.3). The last code changes were made in 2022, though active development ended much earlier when I moved into a project management role.

This repository preserves the project as a historical artifact. It is not maintained.

## What It Does

DataJets wraps JDBC and generates SQL dynamically. You define your connection, table, and fields through a `JetSet` configuration object, and the framework handles creating, reading, updating, and deleting records. Basic usage requires no SQL and minimal JDBC knowledge. For more advanced use, `SqlBuilder` provides a fluent API for constructing queries with joins, ordering, and custom conditions.

The core classes:

- **DataJet** - Abstract base class providing field storage, SQL generation, and CRUD operations
- **GenericDataJet** - Concrete implementation with static factory methods for loading records
- **JetSet** - Configuration object holding connection, table, and field definitions
- **SqlBuilder** - Fluent SQL query builder with support for joins, conditions, and ordering
- **Datum** - Universal type-conversion wrapper for database field values
- **TableDefinition** - Encapsulates DDL for creating database tables from code

## Repository Structure

```
src/                Source code (anderix.datajets package)
website/            The original datajets.com website, tutorials, and JavaDoc
```

## Context

This predates widespread adoption of Hibernate and JPA in the Java ecosystem. The approach is simpler and more lightweight than those frameworks by design. From the original FAQ:

> "I got tired of writing the same SQL code over and over again for applications that were only slightly different, and I wanted classes that would handle all the mechanics of CRUD database functionality for me. I wasn't completely happy with any of the other libraries that exist today, so I wrote my own."

And:

> "You've got too much time on your hands. Do you have a life?"
>
> "No. Thanks for rubbing it in."

## License

LGPL 2.1 (see `website/license.html` for the full text).

Copyright 2003-2022 David Anderson / Anderix
