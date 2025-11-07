# Deprecation Notice for flock-eco

This document provides instructions for publishing the deprecation warnings to npm and Maven registries.

## NPM Package Deprecation

All package.json files have been updated with a `deprecated` field. However, to show deprecation warnings to users who install these packages, you must publish this information to the npm registry.

### Publishing Deprecation to npm Registry

After publishing the packages with the updated package.json files, you can also use the `npm deprecate` command to mark all existing versions as deprecated:

```bash
# Deprecate all versions of each package
npm deprecate @flock-community/flock-eco-core "This package is end of life. No further development is happening. Please migrate away from flock-eco or create a fork."

npm deprecate @flock-community/flock-eco-feature-user "This package is end of life. No further development is happening. Please migrate away from flock-eco or create a fork."

npm deprecate @flock-community/flock-eco-feature-member "This package is end of life. No further development is happening. Please migrate away from flock-eco or create a fork."

npm deprecate @flock-community/flock-eco-feature-workspace "This package is end of life. No further development is happening. Please migrate away from flock-eco or create a fork."

npm deprecate @flock-community/flock-eco-feature-payment "This package is end of life. No further development is happening. Please migrate away from flock-eco or create a fork."

npm deprecate @flock-community/flock-eco-feature-mailchimp "This package is end of life. No further development is happening. Please migrate away from flock-eco or create a fork."

npm deprecate @flock-community/flock-eco-webpack "This package is end of life. No further development is happening. Please migrate away from flock-eco or create a fork."

npm deprecate @flock-community/flock-eco-application-example "This package is end of life. No further development is happening. Please migrate away from flock-eco or create a fork."

npm deprecate @flock-community/flock-eco-application-multi_tenant "This package is end of life. No further development is happening. Please migrate away from flock-eco or create a fork."
```

### Deprecating Specific Versions

To deprecate a specific version range:

```bash
npm deprecate @flock-community/flock-eco-core@"<=2.8.1" "This package is end of life..."
```

### Verification

After deprecating, verify by running:

```bash
npm info @flock-community/flock-eco-core
```

The output should show a deprecation warning.

## Maven Artifact Deprecation

All module pom.xml files have been updated with deprecation notices in their `<description>` tags. These will be included in the published artifacts.

### Maven Central Considerations

Maven Central does not have a built-in deprecation mechanism like npm. However, the deprecation notices in the `<description>` tags will:

1. **Appear in Maven repository browsers** (Maven Central, Sonatype, etc.)
2. **Show up in IDE dependency management tools** (IntelliJ IDEA, Eclipse)
3. **Be included in generated Maven site documentation**

### Additional Steps for Maven

1. **Stop publishing new versions** - This is the primary signal to users
2. **Update Maven metadata** - The description changes will be visible in:
   - `pom.xml` files downloaded by users
   - Repository search interfaces
   - IDE dependency information

### Publishing the Deprecated Artifacts

If you choose to publish one final version with these deprecation notices:

```bash
mvn clean deploy
```

This will upload the artifacts with the updated descriptions to your configured Maven repository.

## GitHub Repository Archival

Don't forget to archive the GitHub repository:

1. Go to: https://github.com/flock-community/flock-eco/settings
2. Scroll to "Danger Zone"
3. Click "Archive this repository"
4. This makes it read-only and displays a prominent archived banner

## Summary of Changes

### NPM Packages (9 packages updated)
- @flock-community/flock-eco-core
- @flock-community/flock-eco-feature-user
- @flock-community/flock-eco-feature-member
- @flock-community/flock-eco-feature-workspace
- @flock-community/flock-eco-feature-payment
- @flock-community/flock-eco-feature-mailchimp
- @flock-community/flock-eco-webpack
- @flock-community/flock-eco-application-example
- @flock-community/flock-eco-application-multi_tenant

### Maven Artifacts (16 artifacts updated)
- flock-eco-parent (root)
- flock-eco-core
- flock-eco-feature-user
- flock-eco-feature-member
- flock-eco-feature-workspace
- flock-eco-feature-payment
- flock-eco-feature-mailchimp
- flock-eco-feature-session
- flock-eco-feature-multi_tenant
- flock-eco-cloud-aws
- flock-eco-cloud-gcp
- flock-eco-cloud-azure
- flock-eco-cloud-stub
- flock-eco-iso-country
- flock-eco-iso-language

## User Experience

When users depend on these packages:

### NPM Users
They will see warnings like:
```
npm WARN deprecated @flock-community/flock-eco-core@2.8.1: This package is end of life. No further development is happening. Please migrate away from flock-eco or create a fork.
```

### Maven Users
They will see the deprecation notice when:
- Viewing the dependency in their IDE
- Browsing Maven Central or repository sites
- Reading the pom.xml of the dependency
